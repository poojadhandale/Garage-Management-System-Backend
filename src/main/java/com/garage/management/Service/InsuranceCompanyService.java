package com.garage.management.Service;

import com.garage.management.Entity.InsuranceCompany;
import com.garage.management.Repository.InsuranceCompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InsuranceCompanyService {

    private final InsuranceCompanyRepository repo;

    public InsuranceCompanyService(InsuranceCompanyRepository repo) {
        this.repo = repo;
    }

    public InsuranceCompany addCompany(InsuranceCompany company) {
        return repo.save(company);
    }

    @Transactional(readOnly = true)
    public Page<InsuranceCompany> getAllCompanies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return repo.findAll(pageable);
    }

    public InsuranceCompany updateCompany(Long id, InsuranceCompany updatedCompany){
        InsuranceCompany existingCompany = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company Already Exist"));

        existingCompany.setCompanyName(updatedCompany.getCompanyName());
        existingCompany.setEmail(updatedCompany.getEmail());
        existingCompany.setContactNumber(updatedCompany.getContactNumber());
        existingCompany.setActive(updatedCompany.isActive());

        return repo.save(existingCompany);
    }

}
