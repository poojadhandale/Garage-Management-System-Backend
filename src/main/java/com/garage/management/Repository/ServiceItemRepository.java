package com.garage.management.Repository;

import com.garage.management.Entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    @Query(value = """
        SELECT st.item_name AS itemName,
        SUM(si.quantity_used) AS used
        FROM service_items si
        JOIN stocks st ON si.stock_id = st.id
        GROUP BY st.item_name
        ORDER BY used DESC
    """, nativeQuery = true)
    List<Object[]> getStockUsageCounts();

    @Modifying
    @Transactional
    @Query("DELETE FROM ServiceItem si WHERE si.serviceRecord.id = :serviceId")
    void deleteByServiceRecordId(Long serviceId);
}
