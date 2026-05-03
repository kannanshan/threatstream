package com.threatstream.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    // TODO: Inject AlertService

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAlerts(
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        // TODO: Implement paginated, filtered alert retrieval
        return ResponseEntity.ok(Map.of(
                "content", Collections.emptyList(),
                "totalElements", 0,
                "totalPages", 0,
                "page", page
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAlert(@PathVariable String id) {
        // TODO: Return single alert with linked event
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<Map<String, Object>> acknowledgeAlert(@PathVariable String id) {
        // TODO: Acknowledge alert (ANALYST+ only)
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
