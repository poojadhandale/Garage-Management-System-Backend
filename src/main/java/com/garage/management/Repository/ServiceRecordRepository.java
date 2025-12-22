package com.garage.management.Repository;

import com.garage.management.DTO.RecentServiceDTO;
import com.garage.management.DTO.ServiceRecordDTO;
import com.garage.management.Entity.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long> {

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
        SELECT 
            c.customer_name AS customerName,
            c.vehicle_no AS vehicleNo,
            s.service_date AS date,
            s.total_cost AS totalCost
        FROM service_records s
        JOIN customers c ON s.customer_id = c.id
        ORDER BY s.service_date DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<RecentServiceDTO> getRecentServices(int limit);


    @Query("SELECT s FROM ServiceRecord s WHERE s.id = :id")
    ServiceRecordDTO  findDTOById(@Param("id") Long id);

//    @Query("""
//    SELECT sr FROM ServiceRecord sr
//    LEFT JOIN FETCH sr.items
//    WHERE sr.id = :id
//""")
//    ServiceRecord getRecordWithItems(@Param("id") Long id);

}

