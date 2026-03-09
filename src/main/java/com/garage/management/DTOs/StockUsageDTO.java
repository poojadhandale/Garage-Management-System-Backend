package com.garage.management.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StockUsageDTO {
    private List<String> labels;
    private List<Integer> usage;
}