package com.garage.management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StockUsageDTO {
    private List<String> labels;
    private List<Integer> usage;
}