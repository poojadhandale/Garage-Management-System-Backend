package com.garage.management.Controller;

import com.garage.management.DTO.DashboardSummaryDTO;
import com.garage.management.DTO.MonthlyCountDTO;
import com.garage.management.DTO.RecentServiceDTO;
import com.garage.management.DTO.StockUsageDTO;
import com.garage.management.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/monthly-service-count")
    public ResponseEntity<MonthlyCountDTO> getMonthlyServiceCount() {
        return ResponseEntity.ok(dashboardService.getMonthlyServiceCount());
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<MonthlyCountDTO> getMonthlyRevenue() {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenue());
    }

    @GetMapping("/stock-usage")
    public ResponseEntity<StockUsageDTO> getStockUsage() {
        return ResponseEntity.ok(dashboardService.getStockUsage());
    }

    @GetMapping("/recent-services")
    public ResponseEntity<List<RecentServiceDTO>> getRecentServices(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(dashboardService.getRecentServices(limit));
    }

}
