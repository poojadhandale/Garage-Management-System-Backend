package com.garage.management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryDTO {
    private Long totalCustomers;
    private Long totalServices;
    private Long totalStocks;
    private Double monthlyRevenue;
}