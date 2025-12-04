package com.garage.management.Service;

import com.garage.management.DTO.CustomerDTO;
import com.garage.management.DTO.ServiceItemDTO;
import com.garage.management.DTO.ServiceRecordDTO;
import com.garage.management.Entity.Customer;
import com.garage.management.Entity.ServiceItem;
import com.garage.management.Entity.ServiceRecord;
import com.garage.management.Entity.Stock;
import com.garage.management.Repository.CustomerRepository;
import com.garage.management.Repository.ServiceItemRepository;
import com.garage.management.Repository.ServiceRecordRepository;
import com.garage.management.Repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRecordService {

    @Autowired
    private ServiceRecordRepository recordRepo;

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private ServiceItemRepository serviceItemRepo;

    @Autowired
    private CustomerRepository customerRepo;

    public List<ServiceRecordDTO> getAllServices() {

        List<ServiceRecord> records = recordRepo.findAll();

        return records.stream().map(record -> {

            ServiceRecordDTO dto = new ServiceRecordDTO();
            dto.setId(record.getId());
            dto.setServiceDate(record.getServiceDate());
            dto.setRemarks(record.getRemarks());
            dto.setTotalCost(record.getTotalCost());

            // Customer
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(record.getCustomer().getId());
            customerDTO.setCustomerName(record.getCustomer().getCustomerName());
            customerDTO.setEmail(record.getCustomer().getEmail());
            customerDTO.setPhone(record.getCustomer().getPhone());
            customerDTO.setVehicleNo(record.getCustomer().getVehicleNo());
            dto.setCustomer(customerDTO);

            // Stock Items
            List<ServiceItemDTO> items = record.getItemsUsed().stream().map(item -> {

                ServiceItemDTO itemDTO = new ServiceItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setStockId(item.getStock().getId());
                itemDTO.setItemName(item.getStock().getItemName());
                itemDTO.setQuantityUsed(item.getQuantityUsed());
                itemDTO.setPrice(item.getStock().getPrice());

                return itemDTO;

            }).collect(Collectors.toList());

            dto.setStocks(items);

            return dto;

        }).collect(Collectors.toList());
    }


    @Transactional
    public ServiceRecord addService(ServiceRecordDTO record) {

        // 1. Get customer
        Customer customer = customerRepo.findById(record.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Create ServiceRecord entity
        ServiceRecord newRecord = new ServiceRecord();
        newRecord.setCustomer(customer);
        newRecord.setServiceDate(record.getServiceDate());
        newRecord.setRemarks(record.getRemarks());
        newRecord.setTotalCost(record.getTotalCost());

        ServiceRecord savedRecord = recordRepo.save(newRecord);

        // 3. Insert each stock item
        for (ServiceItemDTO itemDTO : record.getStocks()) {

            Stock stock = stockRepo.findById(itemDTO.getStockId())
                    .orElseThrow(() -> new RuntimeException("Stock not found"));

            ServiceItem item = new ServiceItem();
            item.setServiceRecord(savedRecord);
            item.setStock(stock);
            item.setQuantityUsed(itemDTO.getQuantityUsed());

            serviceItemRepo.save(item);
        }

        return savedRecord;
    }

    @Transactional
    public ServiceRecordDTO updateService(Long id, ServiceRecordDTO dto) {

        ServiceRecord record = recordRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service record not found"));

        Customer customer = customerRepo.findById(dto.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // UPDATE BASE FIELDS
        record.setCustomer(customer);
        record.setServiceDate(dto.getServiceDate());
        record.setTotalCost(dto.getTotalCost());
        record.setRemarks(dto.getRemarks());

        // DELETE old items
        serviceItemRepo.deleteByServiceRecordId(record.getId());

        // ADD updated stock items
        List<ServiceItem> updatedItems = new ArrayList<>();

        for (ServiceItemDTO itemDTO : dto.getStocks()) {

            Stock stock = stockRepo.findById(itemDTO.getStockId())
                    .orElseThrow(() -> new RuntimeException("Stock not found"));

            ServiceItem item = new ServiceItem();
            item.setServiceRecord(record);
            item.setStock(stock);
            item.setQuantityUsed(itemDTO.getQuantityUsed());

            updatedItems.add(item);
        }

        serviceItemRepo.saveAll(updatedItems);

        // save updated base record
        recordRepo.save(record);

        // Return updated DTO
        return toDTO(record, updatedItems);
    }

    private ServiceRecordDTO toDTO(ServiceRecord record, List<ServiceItem> items) {

        ServiceRecordDTO dto = new ServiceRecordDTO();
        dto.setId(record.getId());
        dto.setCustomer(null);
        dto.setServiceDate(record.getServiceDate());
        dto.setTotalCost(record.getTotalCost());
        dto.setRemarks(record.getRemarks());

        List<ServiceItemDTO> itemDTOs = new ArrayList<>();
        for (ServiceItem it : items) {
            ServiceItemDTO obj = new ServiceItemDTO();
            obj.setId(it.getId());
            obj.setStockId(it.getStock().getId());
            obj.setItemName(it.getStock().getItemName());
            obj.setQuantityUsed(it.getQuantityUsed());
            obj.setPrice(it.getStock().getPrice());
            itemDTOs.add(obj);
        }

        dto.setStocks(itemDTOs);
        return dto;
    }

    public void deleteService(Long id) {
        recordRepo.deleteById(id);
    }

}
