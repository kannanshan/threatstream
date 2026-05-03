package com.threatstream.kafka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threatstream.model.ThreatEvent;
import com.threatstream.model.enums.EventType;
import com.threatstream.model.enums.Severity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

/**
 * Utility class for deserializing Kafka event messages into ThreatEvent entities.
 * Use this in your Kafka consumers to convert raw JSON strings from Kafka topics
 * into domain objects.
 */
@Component
public class EventDeserializer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Deserializes a JSON string from Kafka into a ThreatEvent entity.
     * Handles the mapping from Kafka message format to the JPA entity structure.
     *
     * @param json the raw JSON string from the Kafka topic
     * @return the deserialized ThreatEvent, or null if deserialization fails
     */
    @SuppressWarnings("unchecked")
    public ThreatEvent deserialize(String json) {
        try {
            Map<String, Object> data = objectMapper.readValue(json, Map.class);

            Map<String, Object> geo = (Map<String, Object>) data.get("geo");

            return ThreatEvent.builder()
                    .id((String) data.get("id"))
                    .sourceIp((String) data.get("sourceIp"))
                    .destinationIp((String) data.get("destinationIp"))
                    .type(EventType.valueOf((String) data.get("type")))
                    .severity(Severity.valueOf((String) data.get("severity")))
                    .geoCountry(geo.get("country").toString())
                    .geoLat(((Number) geo.get("lat")).doubleValue())
                    .geoLon(((Number) geo.get("lon")).doubleValue())
                    .description((String) data.get("description"))
                    .timestamp(Instant.parse((String) data.get("timestamp")))
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
