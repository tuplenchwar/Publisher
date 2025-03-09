package org.example.controller;

import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/publisher")
public class PublisherController {


    @Autowired
    private PublisherService publisherService;


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
