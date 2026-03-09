package com.garage.management.Service;

import com.garage.management.DTOs.DashboardSummaryDTO;
import com.garage.management.DTOs.MonthlyCountDTO;
import com.garage.management.DTOs.RecentServiceDTO;
import com.garage.management.DTOs.StockUsageDTO;
import com.garage.management.Repository.CustomerRepository;
import com.garage.management.Repository.ServiceItemRepository;
import com.garage.management.Repository.ServiceRecordRepository;
import com.garage.management.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CustomerRepository customerRepo;

    private final ServiceRecordRepository serviceRecordRepo;
    private final ServiceItemRepository serviceItemRepo;
    private final StockRepository stockRepo;


    public DashboardSummaryDTO getSummary() {
        Long totalCustomers = customerRepo.count();
        Long totalServices = serviceRecordRepo.count();
        Long totalStocks = stockRepo.count();

        Double monthlyRevenue = serviceRecordRepo.getCurrentMonthRevenue();

        return new DashboardSummaryDTO(
                totalCustomers,
                totalServices,
                totalStocks,
                monthlyRevenue == null ? 0.0 : monthlyRevenue
        );
    }

    public MonthlyCountDTO getMonthlyServiceCount() {
        List<Object[]> raw = serviceRecordRepo.getMonthlyServiceCount();

        List<String> months = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (Object[] row : raw) {
            months.add(row[0].toString());
            counts.add(((Number) row[1]).intValue());
        }

        return new MonthlyCountDTO(months, counts, null);
    }

    public MonthlyCountDTO getMonthlyRevenue() {
        List<Object[]> raw = serviceRecordRepo.getMonthlyRevenue();

        List<String> months = new ArrayList<>();
        List<Double> revenue = new ArrayList<>();

        for (Object[] row : raw) {
            months.add(row[0].toString());
            revenue.add(((Number) row[1]).doubleValue());
        }

        return new MonthlyCountDTO(months, null, revenue);
    }

    public StockUsageDTO getStockUsage() {
        List<Object[]> raw = serviceItemRepo.getStockUsageCounts();

        List<String> labels = new ArrayList<>();
        List<Integer> usage = new ArrayList<>();

        for (Object[] row : raw) {
            labels.add(row[0].toString());
            usage.add(((Number) row[1]).intValue());
        }

        return new StockUsageDTO(labels, usage);
    }

    public List<RecentServiceDTO> getRecentServices(int limit) {
        List<Object[]> raw = serviceRecordRepo.getRecentServices(limit);
        List<RecentServiceDTO> response = new ArrayList<>();

        for (Object[] row : raw) {
            response.add(new RecentServiceDTO(
                    ((Number) row[0]).longValue(),
                    row[1] != null ? row[1].toString() : null,
                    row[2] != null ? row[2].toString() : null,
                    row[3] != null ? row[3].toString() : null,
                    row[4] != null ? ((Number) row[4]).doubleValue() : 0.0,
                    row[5] != null ? row[5].toString() : null
            ));
        }

        return response;
    }
}