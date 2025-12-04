package com.garage.management.Controller;


import com.garage.management.Entity.Customer;
import com.garage.management.Security.ApiResponse;
import com.garage.management.Service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Customer>>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();

        ApiResponse<List<Customer>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Customers fetched successfully",
                customers
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> addCustomer(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.addCustomer(customer);
            ApiResponse<Customer> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Customer added successfully",
                    savedCustomer
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<Customer> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error adding customer: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        try {
            Customer savedCustomer = customerService.updateCustomer(id, updatedCustomer);
            ApiResponse<Customer> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Customer updated successfully",
                    savedCustomer
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Customer> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomerById(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Customer deleted successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}