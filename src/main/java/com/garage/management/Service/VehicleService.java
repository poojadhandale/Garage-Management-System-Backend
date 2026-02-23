package com.garage.management.Service;

import com.garage.management.Entity.Vehicle;
import com.garage.management.Repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VehicleService {

    private final VehicleRepository vehicleRepo;

    public VehicleService(VehicleRepository vehicleRepo){
        this.vehicleRepo = vehicleRepo;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAllWithCustomer();

    }
}
