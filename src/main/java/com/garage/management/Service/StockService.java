package com.garage.management.Service;

import com.garage.management.Entity.Stock;
import com.garage.management.Repository.StockRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
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

