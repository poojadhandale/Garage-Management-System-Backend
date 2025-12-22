package com.garage.management.DTO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ServiceRecordDTO {
    private Long id;
    private CustomerDTO customer;
    private LocalDate serviceDate;
    private Double totalCost;
    private String remarks;
    private List<ServiceItemDTO> stocks;
    private List<LabourDTO> labourCharges;
}

