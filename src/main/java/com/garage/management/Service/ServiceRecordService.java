package com.garage.management.Service;


import com.garage.management.Entity.ServiceRecord;
import com.garage.management.Entity.Stock;
import com.garage.management.Entity.Vehicle;
import com.garage.management.Repository.ServiceRecordRepository;
import com.garage.management.Repository.StockRepository;
import com.garage.management.Repository.VehicleRepository;
import org.springframework.data.domain.Page;
import com.garage.management.Security.DocumentGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepo;
    private final VehicleRepository vehicleRepo;
    private final StockRepository stockRepo;
    private final TemplateEngine templateEngine;

    public ServiceRecordService(ServiceRecordRepository serviceRecordRepo, VehicleRepository vehicleRepo,StockRepository stockRepo,TemplateEngine templateEngine) {
        this.serviceRecordRepo = serviceRecordRepo;
        this.stockRepo = stockRepo;
        this.vehicleRepo = vehicleRepo;
        this.templateEngine = templateEngine;
    }


    public Page<ServiceRecord> getAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(
                page, size, Sort.by("id").ascending()
        );

        return serviceRecordRepo.findAll(pageable);
    }

    public ServiceRecord addService(ServiceRecord serviceRecord) {

        // 1️⃣ Resolve vehicle
        if (serviceRecord.getVehicleId() == null) {
            throw new RuntimeException("vehicleId is required");
        }

        Vehicle vehicle = vehicleRepo.findById(serviceRecord.getVehicleId())
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found")
                );

        serviceRecord.setVehicle(vehicle);
        serviceRecord.setCustomer(vehicle.getCustomer());

        // 2️⃣ Items Used
        if (serviceRecord.getItemsUsed() != null) {
            serviceRecord.getItemsUsed().forEach(item -> {

                if (item.getStockId() == null) {
                    throw new RuntimeException("stockId is required");
                }

                Stock stock = stockRepo.findById(item.getStockId())
                        .orElseThrow(() ->
                                new RuntimeException("Stock not found")
                        );

                item.setStock(stock);
                item.setServiceRecord(serviceRecord);
            });
        }

        // 3️⃣ Labour
        if (serviceRecord.getLabour() != null) {
            serviceRecord.getLabour()
                    .forEach(labour ->
                            labour.setServiceRecord(serviceRecord));
        }

        return serviceRecordRepo.save(serviceRecord);
    }

    @Transactional
    public void deleteService(Long serviceId) {

        ServiceRecord serviceRecord = serviceRecordRepo.findById(serviceId)
                .orElseThrow(() ->
                        new RuntimeException("Servicing record not found with id: " + serviceId)
                );

        if (serviceRecord.getItemsUsed() != null) {
            serviceRecord.getItemsUsed().forEach(item -> item.setServiceRecord(null));
            serviceRecord.getItemsUsed().clear();
        }

        if (serviceRecord.getLabour() != null) {
            serviceRecord.getLabour().forEach(labour -> labour.setServiceRecord(null));
            serviceRecord.getLabour().clear();
        }

        serviceRecord.setVehicle(null);
        serviceRecord.setCustomer(null);

        // 4️⃣ Delete service record
        serviceRecordRepo.delete(serviceRecord);
    }

    @Transactional
    public ServiceRecord updateService(Long serviceId, ServiceRecord request) {

        // 1️⃣ Fetch existing service record
        ServiceRecord existing = serviceRecordRepo.findById(serviceId)
                .orElseThrow(() ->
                        new RuntimeException("Servicing record not found with id: " + serviceId)
                );

        // 2️⃣ Resolve vehicle (mandatory, same as add)
        if (request.getVehicleId() == null) {
            throw new RuntimeException("vehicleId is required");
        }

        Vehicle vehicle = vehicleRepo.findById(request.getVehicleId())
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found")
                );

        existing.setVehicle(vehicle);
        existing.setCustomer(vehicle.getCustomer());

        // 3️⃣ Update simple fields
        existing.setServiceDate(request.getServiceDate());
        existing.setRemarks(request.getRemarks());
        existing.setTotalCost(request.getTotalCost());

        // 4️⃣ Handle Items Used
        if (existing.getItemsUsed() == null) {
            existing.setItemsUsed(new ArrayList<>());
        } else {
            existing.getItemsUsed().clear();
        }

        if (request.getItemsUsed() != null) {
            request.getItemsUsed().forEach(item -> {

                if (item.getStockId() == null) {
                    throw new RuntimeException("stockId is required");
                }

                Stock stock = stockRepo.findById(item.getStockId())
                        .orElseThrow(() ->
                                new RuntimeException("Stock not found")
                        );

                item.setStock(stock);
                item.setServiceRecord(existing);
                existing.getItemsUsed().add(item);
            });
        }

        if (existing.getLabour() == null) {
            existing.setLabour(new ArrayList<>());
        } else {
            existing.getLabour().clear();
        }

        if (request.getLabour() != null) {
            request.getLabour().forEach(labour -> {
                labour.setServiceRecord(existing);
                existing.getLabour().add(labour);
            });
        }

        return serviceRecordRepo.save(existing);
    }

    @Transactional(readOnly = true)
    public byte[] generateBillPdf(Long serviceId) {
        ServiceRecord serviceRecord = serviceRecordRepo.findBillDataById(serviceId)
                .orElseThrow(() ->
                        new RuntimeException("Servicing record not found with id: " + serviceId)
                );

        if (serviceRecord.getLabour() != null) {
            serviceRecord.getLabour().size();
        }

        Map<String, Object> customerInfo = new HashMap<>();
        if (serviceRecord.getCustomer() != null) {
            customerInfo.put("customerName", serviceRecord.getCustomer().getCustomerName());
            customerInfo.put("email", serviceRecord.getCustomer().getEmail());
            customerInfo.put("phone", serviceRecord.getCustomer().getPhone());
            customerInfo.put("address", serviceRecord.getCustomer().getAddress());
        }

        Map<String, Object> vehicleInfo = new HashMap<>();
        if (serviceRecord.getVehicle() != null) {
            vehicleInfo.put("vehicleNo", serviceRecord.getVehicle().getVehicleNo());
            vehicleInfo.put("model", serviceRecord.getVehicle().getModel());
        }

        List<Map<String, Object>> serviceItems = serviceRecord.getItemsUsed() == null ? List.of() :
                serviceRecord.getItemsUsed().stream().map(item -> {
                    Map<String, Object> itemData = new HashMap<>();
                    double unitPrice = item.getStock() != null ? item.getStock().getPrice() : 0D;
                    double lineTotal = item.getQuantityUsed() * unitPrice;

                    itemData.put("itemName", item.getStock() != null ? item.getStock().getItemName() : "N/A");
                    itemData.put("quantityUsed", item.getQuantityUsed());
                    itemData.put("price", unitPrice);
                    itemData.put("lineTotal", lineTotal);
                    return itemData;
                }).toList();

        List<Map<String, Object>> labourItems = serviceRecord.getLabour() == null ? List.of() :
                serviceRecord.getLabour().stream().map(labour -> {
                    Map<String, Object> labourData = new HashMap<>();
                    labourData.put("description", labour.getLabourDescription());
                    labourData.put("amount", labour.getAmount() == null ? 0D : labour.getAmount());
                    return labourData;
                }).toList();

        double partsTotal = serviceItems.stream()
                .mapToDouble(item -> (double) item.get("lineTotal"))
                .sum();
        double labourTotal = labourItems.stream()
                .mapToDouble(item -> (double) item.get("amount"))
                .sum();
        double subtotal = partsTotal + labourTotal;
        double gstAmount = subtotal * 0.18;
        double grandTotal = subtotal + gstAmount;

        Context context = new Context();
        context.setVariable("record", serviceRecord);
        context.setVariable("customer", customerInfo);
        context.setVariable("vehicle", vehicleInfo);
        context.setVariable("items", serviceItems);
        context.setVariable("labour", labourItems);
        context.setVariable("partsTotal", partsTotal);
        context.setVariable("labourTotal", labourTotal);
        context.setVariable("subtotal", subtotal);
        context.setVariable("gstAmount", gstAmount);
        context.setVariable("grandTotal", grandTotal);

        String html = templateEngine.process("invoice", context);
        return DocumentGenerator.generatePdfFromHtml(html);
    }

}