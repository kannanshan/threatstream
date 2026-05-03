package com.threatstream.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    // TODO: Inject EventService

    @GetMapping
    public ResponseEntity<Map<String, Object>> getEvents(
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        // TODO: Implement paginated, filtered event retrieval
        return ResponseEntity.ok(Map.of(
                "content", Collections.emptyList(),
                "totalElements", 0,
                "totalPages", 0,
                "page", page
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEvent(@PathVariable String id) {
        // TODO: Return single event with linked alerts
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<Map<String, Object>> addNote(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        // TODO: Add note to event (ANALYST+ only)
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
