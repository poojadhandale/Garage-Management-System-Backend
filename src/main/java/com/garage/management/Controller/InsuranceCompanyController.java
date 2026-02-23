package com.garage.management.Controller;


import com.garage.management.Entity.InsuranceCompany;
import com.garage.management.Entity.Stock;
import com.garage.management.Security.ApiResponse;
import com.garage.management.Service.InsuranceCompanyService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance-companies")
@CrossOrigin(origins = "http://localhost:4200")
public class InsuranceCompanyController {

    private final InsuranceCompanyService companyService;

    public InsuranceCompanyController(InsuranceCompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCompany(
            @RequestBody InsuranceCompany company) {

        InsuranceCompany saved = companyService.addCompany(company);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(
                        201,
                        "Insurance company added successfully",
                        saved
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<InsuranceCompany>>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<InsuranceCompany> insuranceCompanyPage = companyService.getAllCompanies(page, size);
        ApiResponse<Page<InsuranceCompany>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Companies fetched successfully",
                insuranceCompanyPage
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InsuranceCompany>> updateCompany(@PathVariable Long id, @RequestBody InsuranceCompany updatedCompany) {
        try {
            InsuranceCompany savedCompany = companyService.updateCompany(id, updatedCompany);
            ApiResponse<InsuranceCompany> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Stock updated successfully",
                    savedCompany
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<InsuranceCompany> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

