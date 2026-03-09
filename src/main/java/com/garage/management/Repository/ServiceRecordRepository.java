package com.garage.management.Repository;

import com.garage.management.Entity.ServiceRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long> {


    @EntityGraph(attributePaths = {"customer", "vehicle", "itemsUsed", "itemsUsed.stock"})
    @Query("SELECT sr FROM ServiceRecord sr WHERE sr.id = :id")
    Optional<ServiceRecord> findBillDataById(Long id);

    @Query(value = """
                SELECT DATE_FORMAT(service_date, '%b') AS month,
                       COUNT(*) AS count
                FROM service_records
                WHERE YEAR(service_date) = YEAR(CURRENT_DATE)
                GROUP BY MONTH(service_date), month
                ORDER BY MONTH(service_date)
            """, nativeQuery = true)
    List<Object[]> getMonthlyServiceCount();

    @Query(value = """
                SELECT DATE_FORMAT(service_date, '%b') AS month,
                       SUM(total_cost) AS revenue
                FROM service_records
                WHERE YEAR(service_date) = YEAR(CURRENT_DATE)
                GROUP BY MONTH(service_date), month
                ORDER BY MONTH(service_date)
            """, nativeQuery = true)
    List<Object[]> getMonthlyRevenue();

    @Query(value = """
                SELECT SUM(total_cost)
                FROM service_records
                WHERE MONTH(service_date) = MONTH(CURRENT_DATE)
                AND YEAR(service_date) = YEAR(CURRENT_DATE)
            """, nativeQuery = true)
    Double getCurrentMonthRevenue();

    @Query(value = """
            SELECT sr.id,
                   c.name AS customer_name,
                   v.vehicle_number,
                   sr.service_date,
                   sr.total_cost,
                   sr.remarks
            FROM service_records sr
            LEFT JOIN customers c ON sr.customer_id = c.id
            LEFT JOIN vehicles v ON sr.vehicle_id = v.id
            ORDER BY sr.service_date DESC, sr.id DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> getRecentServices(@Param("limit") int limit);
}

