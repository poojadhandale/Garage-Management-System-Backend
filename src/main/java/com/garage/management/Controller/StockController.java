package com.garage.management.Controller;

import com.garage.management.Entity.Stock;
import com.garage.management.Security.ApiResponse;
import com.garage.management.Service.StockService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:4200")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Stock>>> getAllStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Stock> stocksPage = stockService.getAllStocks(page, size);
        ApiResponse<Page<Stock>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Stocks fetched successfully",
                stocksPage
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Stock>> addStock(@RequestBody Stock stock) {
        try {
            Stock savedStock = stockService.addStock(stock);
            ApiResponse<Stock> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Stock added successfully",
                    savedStock
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<Stock> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error adding stock: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Stock>> updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) {
        try {
            Stock savedStock = stockService.updateStock(id, updatedStock);
            ApiResponse<Stock> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Stock updated successfully",
                    savedStock
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Stock> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStock(@PathVariable Long id) {
        try {
            stockService.deleteStockById(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Stock deleted successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
