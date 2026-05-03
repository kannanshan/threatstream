package com.threatstream.repository;

import com.threatstream.model.Alert;
import com.threatstream.model.enums.AlertAction;
import com.threatstream.model.enums.Severity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {

    Page<Alert> findBySeverity(Severity severity, Pageable pageable);

    Page<Alert> findByAction(AlertAction action, Pageable pageable);

    Page<Alert> findBySeverityAndAction(Severity severity, AlertAction action, Pageable pageable);

    long countByAcknowledgedFalse();
}
