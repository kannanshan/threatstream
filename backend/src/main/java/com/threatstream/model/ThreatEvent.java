package com.threatstream.model;

import com.threatstream.model.enums.EventType;
import com.threatstream.model.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "threat_events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ThreatEvent {

    @Id
    private String id;

    @PrePersist
    protected void ensureId() {
        if (this.id == null) this.id = java.util.UUID.randomUUID().toString();
    }

    @Column(nullable = false)
    private String sourceIp;

    private String destinationIp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    private String geoCountry;
    private Double geoLat;
    private Double geoLon;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
