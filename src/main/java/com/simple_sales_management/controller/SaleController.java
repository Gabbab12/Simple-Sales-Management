package com.simple_sales_management.controller;

import com.simple_sales_management.dto.TransactionDto;
import com.simple_sales_management.dto.UpdateSaleDetailsDto;
import com.simple_sales_management.model.Sale;
import com.simple_sales_management.service.implementation.SaleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleServiceImpl saleService;

    @PostMapping("/create/{clientId}/{sellerId}")
    public ResponseEntity<Sale> createNewSale(@PathVariable Long clientId, @PathVariable Long sellerId, @RequestBody List<TransactionDto> transaction) {
       return saleService.createNewSale(clientId, sellerId, transaction);
    }

    @PutMapping("/update-sale/{saleId}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long saleId, UpdateSaleDetailsDto updateSaleDetailsDto){
        return saleService.updateSaleDetails(saleId, updateSaleDetailsDto);
    }

    @GetMapping("/get-all-sales")
    public Page<Sale> getAllSales(@PageableDefault(page = 0, size = 10)Pageable pageable){
        return saleService.getAllSales(pageable);
    }
}
