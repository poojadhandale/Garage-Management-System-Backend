package com.garage.management.Service;

import com.garage.management.Entity.Stock;
import com.garage.management.Repository.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Page<Stock> getAllStocks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return stockRepository.findAll(pageable);
    }

    public Stock updateStock(Long id, Stock updatedStock) {
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + id));

        existingStock.setItemName(updatedStock.getItemName());
        existingStock.setCategory(updatedStock.getCategory());
        existingStock.setQuantity(updatedStock.getQuantity());
        existingStock.setPrice(updatedStock.getPrice());

        return stockRepository.save(existingStock);
    }

    public Stock addStock(Stock addStock) {
        return stockRepository.save(addStock);
    }

    public void deleteStockById(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new RuntimeException("Stock not found with id " + id);
        }
        stockRepository.deleteById(id);
    }
}

