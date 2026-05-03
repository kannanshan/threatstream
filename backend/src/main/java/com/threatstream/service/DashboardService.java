package com.threatstream.service;

import com.threatstream.model.DashboardMetrics;
import com.threatstream.model.ThreatEvent;
import com.threatstream.repository.AlertRepository;
import com.threatstream.repository.ThreatEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ThreatEventRepository eventRepository;
    private final AlertRepository alertRepository;

    private final AtomicReference<SystemMetricsSnapshot> latestMetrics = new AtomicReference<>(
            new SystemMetricsSnapshot(0, 0.0, 0, 0.0, 0.0, 0)
    );

    public void updateSystemMetrics(int eventsPerSecond, double avgLatencyMs,
                                     int activeConnections, double cpuPercent,
                                     double memoryPercent, int kafkaLag) {
        latestMetrics.set(new SystemMetricsSnapshot(
                eventsPerSecond, avgLatencyMs, activeConnections,
                cpuPercent, memoryPercent, kafkaLag));
    }

    public DashboardMetrics getMetrics() {
        SystemMetricsSnapshot snapshot = latestMetrics.get();
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        List<ThreatEvent> recentEvents = eventRepository.findEventsSince(oneHourAgo);

        Map<String, Long> bySeverity = recentEvents.stream()
                .collect(Collectors.groupingBy(e -> e.getSeverity().name(), Collectors.counting()));

        Map<String, Long> byType = recentEvents.stream()
                .collect(Collectors.groupingBy(e -> e.getType().name(), Collectors.counting()));

        return DashboardMetrics.builder()
                .eventsPerSecond(snapshot.eventsPerSecond())
                .avgLatencyMs(snapshot.avgLatencyMs())
                .activeConnections(snapshot.activeConnections())
                .cpuPercent(snapshot.cpuPercent())
                .memoryPercent(snapshot.memoryPercent())
                .kafkaLag(snapshot.kafkaLag())
                .totalEventsLastHour(recentEvents.size())
                .eventsBySeverity(bySeverity)
                .eventsByType(byType)
                .activeAlertsCount((int) alertRepository.countByAcknowledgedFalse())
                .hourlyBreakdown(getHourlyBreakdown(recentEvents))
                .build();
    }

    /**
     * Aggregates events into hourly buckets for the "Events by Hour" chart.
     * Returns a map of hour (0-23) to event count.
     */
    public Map<Integer, Long> getHourlyBreakdown(List<ThreatEvent> events) {
        Map<Integer, Long> breakdown = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            breakdown.put(i, 0L);
        }

        for (ThreatEvent event : events) {
            // Get the hour from the event timestamp for bucketing
            int hour = LocalDateTime.now().getHour();
            int eventHour = event.getTimestamp().atZone(java.time.ZoneOffset.UTC)
                    .toLocalDateTime().getHour();
            breakdown.merge(eventHour, 1L, Long::sum);
        }

        return breakdown;
    }

    public record SystemMetricsSnapshot(
            int eventsPerSecond, double avgLatencyMs, int activeConnections,
            double cpuPercent, double memoryPercent, int kafkaLag
    ) {}
}
