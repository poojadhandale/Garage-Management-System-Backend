package com.garage.management.Repository;

import com.garage.management.Entity.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceCompanyRepository
        extends JpaRepository<InsuranceCompany, Long> {
}