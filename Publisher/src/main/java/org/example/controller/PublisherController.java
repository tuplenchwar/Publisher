package org.example.controller;

import org.example.service.PublisherService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController  // Keep @RestController
@RequestMapping("/publisher")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    // API to Get Topics
    @GetMapping("/topics")
    public List<String> getTopics() {
        return publisherService.getTopics();
    }

    // API to Publish Messages
    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestParam String topic, @RequestParam int count) {
        return ResponseEntity.ok(publisherService.publishMessagesForTopic(topic, count));
    }
}
