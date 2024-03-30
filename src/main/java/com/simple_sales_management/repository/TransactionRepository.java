package com.simple_sales_management.repository;

import com.simple_sales_management.model.Sale;
import com.simple_sales_management.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findBySaleId(Long saleId);

    List<Transaction> findBySale(Sale sale);

}
