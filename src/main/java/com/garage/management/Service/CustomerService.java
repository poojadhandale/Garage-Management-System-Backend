package com.garage.management.Service;


import com.garage.management.Entity.Customer;
import com.garage.management.Entity.Vehicle;
import com.garage.management.Repository.CustomerRepository;
import com.garage.management.Repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public CustomerService(CustomerRepository customerRepository, VehicleRepository vehicleRepository) {
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Page<Customer> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").ascending()
        );
        return customerRepository.findAll(pageable);
    }

    public Customer addCustomer(Customer customer) {

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException(
                    "Customer already exists with email: " + customer.getEmail()
            );
        }
        if (customer.getPhone() == null
                || !customer.getPhone().matches("^[0-9]{10}$")) {
            throw new RuntimeException("Phone number must be exactly 10 digits");
        }

        if (customerRepository.existsByPhone(customer.getPhone())) {
            throw new RuntimeException(
                    "Customer already exists with phone: " + customer.getPhone()
            );
        }

        List<Vehicle> incomingVehicles = Optional.ofNullable(customer.getVehicles())
                .orElse(Collections.emptyList());


        // Extract vehicle numbers
        List<String> vehicleNos = incomingVehicles
                .stream()
                .map(Vehicle::getVehicleNo)
                .toList();

        // Check duplicate vehicle numbers in DB
        List<Vehicle> existingVehicles = vehicleRepository.findByVehicleNoIn(vehicleNos);

        if (!existingVehicles.isEmpty()) {
            throw new RuntimeException(
                    "Vehicle number already exists: " +
                            existingVehicles.get(0).getVehicleNo()
            );
        }

        incomingVehicles.forEach(v -> v.setCustomer(customer));

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found with ID: " + id));

        // Validate phone format
        if (updatedCustomer.getPhone() == null
                || !updatedCustomer.getPhone().matches("^[0-9]{10}$")) {
            throw new RuntimeException("Phone number must be exactly 10 digits");
        }

        // Email uniqueness
        if (!existingCustomer.getEmail().equals(updatedCustomer.getEmail())
                && customerRepository.existsByEmail(updatedCustomer.getEmail())) {
            throw new RuntimeException("Email already in use: " + updatedCustomer.getEmail());
        }

        // Phone uniqueness
        if (!existingCustomer.getPhone().equals(updatedCustomer.getPhone())
                && customerRepository.existsByPhone(updatedCustomer.getPhone())) {
            throw new RuntimeException("Phone already in use: " + updatedCustomer.getPhone());
        }

        existingCustomer.setCustomerName(updatedCustomer.getCustomerName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setAddress(updatedCustomer.getAddress());

        List<Vehicle> incomingVehicles =
                Optional.ofNullable(updatedCustomer.getVehicles())
                        .orElse(Collections.emptyList());

        Map<Long, Vehicle> existingVehicleMapById =
                existingCustomer.getVehicles()
                        .stream()
                        .filter(v -> v.getId() != null)
                        .collect(Collectors.toMap(Vehicle::getId, v -> v));

        Map<String, Vehicle> existingVehicleMapByNo =
                existingCustomer.getVehicles()
                        .stream()
                        .collect(Collectors.toMap(Vehicle::getVehicleNo, v -> v, (first, second) -> first));

        List<String> incomingVehicleNos =
                incomingVehicles.stream()
                        .map(Vehicle::getVehicleNo)
                        .toList();

        // Duplicate inside request
        if (new HashSet<>(incomingVehicleNos).size() != incomingVehicleNos.size()) {
            throw new RuntimeException("Duplicate vehicle numbers in request");
        }

        // Duplicate in DB
        List<Vehicle> dbDuplicates =
                vehicleRepository.findByVehicleNoInAndCustomerIdNot(
                        incomingVehicleNos, existingCustomer.getId());

        if (!dbDuplicates.isEmpty()) {
            throw new RuntimeException(
                    "Vehicle number already exists: " +
                            dbDuplicates.get(0).getVehicleNo());
        }

        List<Vehicle> updatedVehicleList = new ArrayList<>();

        for (Vehicle incoming : incomingVehicles) {
            Vehicle existingVehicle = null;

            if (incoming.getId() != null) {
                existingVehicle = existingVehicleMapById.get(incoming.getId());
            }

            if (existingVehicle == null) {
                existingVehicle = existingVehicleMapByNo.get(incoming.getVehicleNo());
            }
            if (existingVehicle != null) {
                existingVehicle.setVehicleNo(incoming.getVehicleNo());
                existingVehicle.setModel(incoming.getModel());
                updatedVehicleList.add(existingVehicle);
            } else {
                incoming.setCustomer(existingCustomer);
                updatedVehicleList.add(incoming);
                Vehicle newVehicle = Vehicle.builder()
                        .vehicleNo(incoming.getVehicleNo())
                        .model(incoming.getModel())
                        .customer(existingCustomer)
                        .build();
                updatedVehicleList.add(newVehicle);
            }
        }

        existingCustomer.getVehicles().removeIf(vehicle -> !updatedVehicleList.contains(vehicle));
        updatedVehicleList.stream()
                .filter(vehicle -> !existingCustomer.getVehicles().contains(vehicle))
                .forEach(vehicle -> {
                    vehicle.setCustomer(existingCustomer);
                    existingCustomer.getVehicles().add(vehicle);
                });

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomerById(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException(
                    "Customer not found with id " + id
            );
        }
        customerRepository.deleteById(id);
    }
}
