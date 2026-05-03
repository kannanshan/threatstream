package com.threatstream.model;

import lombok.*;

import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardMetrics {
    private int eventsPerSecond;
    private double avgLatencyMs;
    private int activeConnections;
    private double cpuPercent;
    private double memoryPercent;
    private int kafkaLag;
    private long totalEventsLastHour;
    private Map<String, Long> eventsBySeverity;
    private Map<String, Long> eventsByType;
    private int activeAlertsCount;
    private Map<Integer, Long> hourlyBreakdown;
}
