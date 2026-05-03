package com.threatstream.model;

import com.threatstream.model.enums.AlertAction;
import com.threatstream.model.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Alert {

    @Id
    private String id;

    @PrePersist
    protected void ensureId() {
        if (this.id == null) this.id = java.util.UUID.randomUUID().toString();
    }

    @Column(nullable = false)
    private String ruleId;

    @Column(nullable = false)
    private String ruleName;

    private String matchedEventId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertAction action;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Builder.Default
    private boolean acknowledged = false;

    private String acknowledgedBy;

    private Instant acknowledgedAt;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
