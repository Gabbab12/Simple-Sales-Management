package com.simple_sales_management.service;

import com.simple_sales_management.dto.TransactionDto;
import com.simple_sales_management.dto.UpdateProductDetailsDto;
import com.simple_sales_management.dto.UpdateSaleDetailsDto;
import com.simple_sales_management.model.Sale;
import com.simple_sales_management.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SaleService {

    @Transactional
    ResponseEntity<Sale> createNewSale(Long clientId, Long sellerId, List<TransactionDto> transactionDtos);

    Page<Sale> getAllSales(Pageable pageable);
}
