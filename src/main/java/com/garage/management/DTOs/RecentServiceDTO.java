package com.garage.management.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecentServiceDTO {
    private Long id;
    private String customerName;
    private String vehicleNumber;
    private String serviceDate;
    private Double totalCost;
    private String remarks;
}