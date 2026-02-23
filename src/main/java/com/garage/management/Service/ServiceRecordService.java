package com.garage.management.Service;


import com.garage.management.Entity.ServiceRecord;
import com.garage.management.Entity.Stock;
import com.garage.management.Entity.Vehicle;
import com.garage.management.Repository.ServiceRecordRepository;
import com.garage.management.Repository.StockRepository;
import com.garage.management.Repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepo;
    private final VehicleRepository vehicleRepo;
    private final StockRepository stockRepo;

    public ServiceRecordService(ServiceRecordRepository serviceRecordRepo, VehicleRepository vehicleRepo,StockRepository stockRepo) {
        this.serviceRecordRepo = serviceRecordRepo;
        this.stockRepo = stockRepo;
        this.vehicleRepo = vehicleRepo;
    }


    public Page<ServiceRecord> getAllServices(int page, int size) {
        ServiceRecord newRecord = new ServiceRecord();
        Pageable pageable = PageRequest.of(
                page, size, Sort.by("id").ascending()
        );
        Page<ServiceRecord> records = serviceRecordRepo.findAll(pageable);





        return records;
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

        // 3️⃣ Update simple fields (example – adjust as per entity)
        existing.setServiceDate(request.getServiceDate());
        existing.setRemarks(request.getRemarks());
        existing.setTotalCost(request.getTotalCost());

        // 4️⃣ Handle Items Used (FULL REPLACE strategy)
        existing.getItemsUsed().clear();

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

        // 5️⃣ Handle Labour (FULL REPLACE strategy)
        existing.getLabour().clear();

        if (request.getLabour() != null) {
            request.getLabour().forEach(labour -> {
                labour.setServiceRecord(existing);
                existing.getLabour().add(labour);
            });
        }

        return serviceRecordRepo.save(existing);
    }

}
