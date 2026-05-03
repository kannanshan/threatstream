package com.threatstream.controller;

import com.threatstream.model.DashboardMetrics;
import com.threatstream.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetrics> getMetrics() {
        // TODO: Return latest system metrics + aggregated event counts
        return ResponseEntity.ok(dashboardService.getMetrics());
    }
}
