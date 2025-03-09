package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PublisherApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(PublisherApplication.class)
                .web(WebApplicationType.SERVLET)  // Ensure it runs as a Web App
                .run(args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
