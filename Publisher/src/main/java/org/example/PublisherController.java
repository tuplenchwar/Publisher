package org.example;

import dto.Packet;
import org.example.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/publish")
    public ResponseEntity<Packet> publishMessage(@RequestBody Packet packet) {
        publisherService.publishMessage(packet);
        return ResponseEntity.ok(packet);
    }
}
