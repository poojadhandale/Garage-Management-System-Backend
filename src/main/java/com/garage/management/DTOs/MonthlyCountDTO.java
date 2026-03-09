package com.garage.management.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyCountDTO {
    private List<String> months;
    private List<Integer> counts;
    private List<Double> revenue;
}