package com.garage.management.Repository;

import com.garage.management.Entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StockRepository extends JpaRepository<Stock, Long> {
    long countByQuantityLessThan(int qty);

    List<Stock> findTop5ByOrderByIdDesc();

}
