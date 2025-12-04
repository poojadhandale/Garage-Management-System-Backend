package com.garage.management.Controller;

import com.garage.management.DTO.ServiceRecordDTO;
import com.garage.management.Entity.ServiceRecord;
import com.garage.management.Security.ApiResponse;
import com.garage.management.Service.ServiceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceController {

    @Autowired
    private ServiceRecordService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceRecordDTO>>> getAll() {
        List<ServiceRecordDTO> servicingRecord = service.getAllServices();
        ApiResponse<List<ServiceRecordDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Servicing Records Fetched Successfully",
                servicingRecord
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceRecord>> addService(@RequestBody ServiceRecordDTO records) {
        ServiceRecord saved = service.addService(records);
        ApiResponse<ServiceRecord> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Servicing record added Successfully", saved
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceRecordDTO>> updateService(@PathVariable Long id, @RequestBody ServiceRecordDTO dto) {

        ServiceRecordDTO saved = service.updateService(id, dto);
        ApiResponse<ServiceRecordDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Servicing record added Successfully", saved
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteService(id);
        return ResponseEntity.ok("Deleted");
    }




}
