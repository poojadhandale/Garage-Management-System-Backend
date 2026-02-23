package com.garage.management.Repository;

import com.garage.management.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByVehicleNoIn(List<String> vehicleNos);

    List<Vehicle> findByVehicleNoInAndCustomerIdNot(
            List<String> vehicleNos,
            Long customerId
    );

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.customer")
    List<Vehicle> findAllWithCustomer();
}
