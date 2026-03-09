package com.garage.management.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardSummaryDTO {
    private Long totalCustomers;
    private Long totalServices;
    private Long totalStocks;
    private Double monthlyRevenue;
}