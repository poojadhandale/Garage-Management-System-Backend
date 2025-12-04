package com.garage.management.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    private Long id;
    private String customerName;
    private String email;
    private String phone;
    private String vehicleNo;
}
