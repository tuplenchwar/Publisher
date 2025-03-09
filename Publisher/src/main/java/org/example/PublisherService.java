package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
@Service
public class PublisherService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final AtomicInteger MESSAGE_ID_COUNTER = new AtomicInteger(1);
    private final String publisherId;
    private final String coordinatorUrl = "http://coordinator-sports-notification.click:8080/";
    private String leaderBrokerUrl;
    private Map<String, List<Packet>> topicMessages;

    public PublisherService() {
        this.publisherId = UUID.randomUUID().toString();
        System.out.println("Publisher started with ID: " + this.publisherId);
    }

    @PostConstruct
    public void initialize() {
        fetchLeaderBroker();
        loadDummyData();
        //startPublishingProcess();
    }

    private void fetchLeaderBroker() {
        String leaderUrl = coordinatorUrl + "/coordinator/leader";
        ResponseEntity<Broker> response = restTemplate.getForEntity(leaderUrl, Broker.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            leaderBrokerUrl = response.getBody().getConnectionUrl();
            System.out.println("Leader Broker URL: " + leaderBrokerUrl);
        } else {
            throw new RuntimeException("Failed to fetch leader broker from coordinator.");
        }
    }

    private void loadDummyData() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dummy_messages.json")) {
            if (inputStream == null) {
                throw new RuntimeException("File not found in resources!");
            }
            topicMessages = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            System.out.println("Loaded topics: " + topicMessages.keySet());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dummy message data.", e);
        }
    }

    private void startPublishingProcess() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select a topic to publish messages:");
            topicMessages.keySet().forEach(System.out::println);

            String topic = scanner.nextLine();
            if (!topicMessages.containsKey(topic)) {
                System.out.println("Invalid topic! Try again.");
                continue;
            }

            System.out.println("Enter number of messages to publish for " + topic + ":");
            int count = Integer.parseInt(scanner.nextLine());
            List<Packet> messages = topicMessages.get(topic);

            for (int i = 0; i < Math.min(count, messages.size()); i++) {
                publishMessage(messages.get(i));
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void publishMessage(Packet packet) {
        if (leaderBrokerUrl == null) {
            fetchLeaderBroker();
        }

        packet.setPid(publisherId);
        packet.setMid(MESSAGE_ID_COUNTER.getAndIncrement());
        packet.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Packet> request = new HttpEntity<>(packet, headers);

        try {
            System.out.println("Publishing message: " + packet.getMessage());
            System.out.println("Publishing Topic: " + packet.getTopic());
            ResponseEntity<String> response = restTemplate.postForEntity(
                    leaderBrokerUrl + "/broker/publish", request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Message published: " + packet.getMessage());
            } else {
                throw new RuntimeException("Publish failed: " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Leader broker unavailable. Fetching new leader...");
            fetchLeaderBroker();
            publishMessage(packet);
        }
    }
}
