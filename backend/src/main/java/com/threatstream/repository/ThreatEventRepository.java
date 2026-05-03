package com.threatstream.repository;

import com.threatstream.model.ThreatEvent;
import com.threatstream.model.enums.EventType;
import com.threatstream.model.enums.Severity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ThreatEventRepository extends JpaRepository<ThreatEvent, String> {

    Page<ThreatEvent> findBySeverity(Severity severity, Pageable pageable);

    Page<ThreatEvent> findByType(EventType type, Pageable pageable);

    Page<ThreatEvent> findBySeverityAndType(Severity severity, EventType type, Pageable pageable);

    @Query("SELECT e FROM ThreatEvent e WHERE e.timestamp BETWEEN :from AND :to")
    Page<ThreatEvent> findByTimestampBetween(@Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    @Query("SELECT e FROM ThreatEvent e WHERE e.timestamp >= :since")
    List<ThreatEvent> findEventsSince(@Param("since") Instant since);

    long countBySeverity(Severity severity);

    long countByType(EventType type);
}
