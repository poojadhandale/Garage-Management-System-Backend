package com.garage.management.DTO;

import com.garage.management.Entity.Customer;
import com.garage.management.Entity.ServiceItem;

import java.util.List;

public interface RecentServiceDTO {
    String getCustomerName();
    String getVehicleNo();
    String getDate();
    Double getTotalCost();
}