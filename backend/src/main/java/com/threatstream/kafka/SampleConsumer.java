package com.threatstream.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Sample Kafka consumer demonstrating the Spring Kafka listener pattern.
 * This consumes from the pre-configured "hello-topic" and logs each message.
 *
 * Use this as a reference when implementing your own consumers for:
 *   - threat-events topic
 *   - alert-rules topic
 *   - system-metrics topic
 */
@Component
@Slf4j
public class SampleConsumer {

    @KafkaListener(topics = "hello-topic", groupId = "sample-group")
    public void listen(String message) {
        log.info("[SampleConsumer] Received: {}", message);
    }
}
