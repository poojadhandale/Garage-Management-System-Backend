package com.garage.management.Controller;

import com.garage.management.Entity.ServiceRecord;
import com.garage.management.Security.ApiResponse;
import com.garage.management.Service.ServiceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceController {

    @Autowired
    private ServiceRecordService serviceRecordService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ServiceRecord>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<ServiceRecord> records = serviceRecordService.getAllServices(page, size);
        ApiResponse<Page<ServiceRecord>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Servicing Records Fetched Successfully",
                records
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceRecord>> addService(@RequestBody ServiceRecord serviceRecord) {
        ServiceRecord saved = serviceRecordService.addService(serviceRecord);
        ApiResponse<ServiceRecord> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Servicing record added Successfully", saved
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceRecord>> updateService(
            @PathVariable Long serviceId,
            @RequestBody ServiceRecord serviceRecord) {

        ServiceRecord updated = serviceRecordService.updateService(serviceId, serviceRecord);

        ApiResponse<ServiceRecord> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Servicing record updated successfully",
                updated
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long serviceId) {

        serviceRecordService.deleteService(serviceId);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Servicing record deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serviceId}/bill")
    public ResponseEntity<byte[]> generateBill(@PathVariable Long serviceId) {
        byte[] billPdf = serviceRecordService.generateBillPdf(serviceId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=service-bill-" + serviceId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(billPdf);
    }
}
