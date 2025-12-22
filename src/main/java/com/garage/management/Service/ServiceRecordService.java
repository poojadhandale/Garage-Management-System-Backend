package com.garage.management.Service;

import com.garage.management.DTO.CustomerDTO;
import com.garage.management.DTO.LabourDTO;
import com.garage.management.DTO.ServiceItemDTO;
import com.garage.management.DTO.ServiceRecordDTO;
import com.garage.management.Entity.*;
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

        return records.stream().map(serviceRecord -> {

            ServiceRecordDTO dto = new ServiceRecordDTO();
            dto.setId(serviceRecord.getId());
            dto.setServiceDate(serviceRecord.getServiceDate());
            dto.setRemarks(serviceRecord.getRemarks());
            dto.setTotalCost(serviceRecord.getTotalCost());

            // Customer
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(serviceRecord.getCustomer().getId());
            customerDTO.setCustomerName(serviceRecord.getCustomer().getCustomerName());
            customerDTO.setEmail(serviceRecord.getCustomer().getEmail());
            customerDTO.setPhone(serviceRecord.getCustomer().getPhone());
            customerDTO.setVehicleNo(serviceRecord.getCustomer().getVehicleNo());
            dto.setCustomer(customerDTO);

            // Stock Items
            List<ServiceItemDTO> items = new ArrayList<>();
            for (ServiceItem serviceItem : serviceRecord.getItemsUsed()) {

                ServiceItemDTO itemDTO = new ServiceItemDTO();
                itemDTO.setId(serviceItem.getId());
                itemDTO.setStockId(serviceItem.getStock().getId());
                itemDTO.setItemName(serviceItem.getStock().getItemName());
                itemDTO.setQuantityUsed(serviceItem.getQuantityUsed());
                itemDTO.setPrice(serviceItem.getStock().getPrice());

                items.add(itemDTO);
            }
            dto.setStocks(items);

            // Labour Items
            List<LabourDTO> labourChargeList = new ArrayList<>();
            for (Labour labour : serviceRecord.getLabour()) {

                LabourDTO labourDTO = new LabourDTO();
                labourDTO.setId(labourDTO.getId());
                labourDTO.setDescription(labour.getLabourDescription());
                labourDTO.setPrice(labour.getAmount());
                labourChargeList.add(labourDTO);
            }

            dto.setLabourCharges(labourChargeList);

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
