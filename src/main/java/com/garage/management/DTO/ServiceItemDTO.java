package com.garage.management.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ServiceItemDTO {
    private Long id;
    private Long stockId;
    private String itemName;
    private Integer quantityUsed;
    private Double price;
    private Double total;
}
