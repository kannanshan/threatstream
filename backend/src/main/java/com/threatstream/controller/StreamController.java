package com.threatstream.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stream")
public class StreamController {

    // TODO: Implement WebSocket or SSE endpoint
    // This controller should bridge Kafka consumers to connected browser clients
    //
    // Option A: WebSocket (STOMP over SockJS)
    //   - Use @MessageMapping and SimpMessagingTemplate to push to /topic/events
    //   - Authenticate via STOMP CONNECT headers
    //
    // Option B: Server-Sent Events
    //   - Return SseEmitter from a GET endpoint
    //   - Push events as they arrive from Kafka consumers
    //
    // Message format should distinguish event types:
    //   { "type": "THREAT_EVENT", "data": {...} }
    //   { "type": "ALERT", "data": {...} }
    //   { "type": "METRICS", "data": {...} }
}
