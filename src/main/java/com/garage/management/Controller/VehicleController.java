package com.garage.management.Controller;


import com.garage.management.Entity.Vehicle;
import com.garage.management.Security.ApiResponse;
import com.garage.management.Service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Vehicle>>> getVehicles() {
        List<Vehicle> vehicle = vehicleService.getAllVehicles();
        System.out.println(vehicle);
        ApiResponse<List<Vehicle>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vehicles fetched successfully",
                vehicle
        );
        return ResponseEntity.ok(response);
    }

}
