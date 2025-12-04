package com.garage.management.Service;


import com.garage.management.Entity.Customer;
import com.garage.management.Repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;


    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));

        existingCustomer.setCustomerName(updatedCustomer.getCustomerName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setVehicleNo(updatedCustomer.getVehicleNo());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomerById(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id " + id);
        }
        customerRepository.deleteById(id);
    }
}