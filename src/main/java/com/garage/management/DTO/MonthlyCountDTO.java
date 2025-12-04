package com.garage.management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MonthlyCountDTO {
    private List<String> months;
    private List<Integer> counts;
    private List<Double> revenue;

}