package com.simple_sales_management.service.implementation;

import com.simple_sales_management.dto.TransactionDto;
import com.simple_sales_management.dto.UpdateSaleDetailsDto;
import com.simple_sales_management.enums.Roles;
import com.simple_sales_management.exception.*;
import com.simple_sales_management.model.Product;
import com.simple_sales_management.model.Sale;
import com.simple_sales_management.model.Transaction;
import com.simple_sales_management.model.User;
import com.simple_sales_management.repository.ProductRepository;
import com.simple_sales_management.repository.SaleRepository;
import com.simple_sales_management.repository.TransactionRepository;
import com.simple_sales_management.repository.UserRepository;
import com.simple_sales_management.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<Sale> createNewSale(Long clientId, Long sellerId, List<TransactionDto> transactionDtos) {
        validateClientAndSellerIds(clientId, sellerId);

        User client = getUserByIdAndRole(clientId, Roles.CLIENT);
        User seller = getUserByIdAndRole(sellerId, Roles.SELLER);

        validateClientAndSeller(client, seller);

        Sale sale = new Sale();
        sale.setClient(client);
        sale.setSeller(seller);

        BigDecimal totalPrice = calculateTotalPriceAndCreateTransactions(transactionDtos, sale);

        sale.setTotal(totalPrice);
        saleRepository.save(sale);

        return ResponseEntity.ok(sale);
    }

    private void validateClientAndSellerIds(Long clientId, Long sellerId) {
        if (clientId == null || sellerId == null) {
            throw new BadRequestException("Please provide both client and seller IDs", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateClientAndSeller(User client, User seller) {
        if (client == null || seller == null) {
            throw new BadRequestException("Invalid client or seller", HttpStatus.BAD_REQUEST);
        }
    }

    private BigDecimal calculateTotalPriceAndCreateTransactions(List<TransactionDto> transactionDtos, Sale sale) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<Transaction> transactions = new ArrayList<>();
        for (TransactionDto transactionDto : transactionDtos) {
            Product product = getProductById(transactionDto.getProductId());
            BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(transactionDto.getQuantity()));
            totalPrice = totalPrice.add(price);

            Transaction transaction = createTransaction(product, transactionDto.getQuantity(), price, sale);
            transactions.add(transaction);
        }
        transactionRepository.saveAll(transactions);
        return totalPrice;
    }

    private Transaction createTransaction(Product product, Long quantity, BigDecimal price, Sale sale) {
        Transaction transaction = new Transaction();
        transaction.setSale(sale);
        transaction.setProducts(product);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        return transaction;
}

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found", HttpStatus.NOT_FOUND));
    }
    private User getUserByIdAndRole(Long userId, Roles role) {
        return userRepository.findByIdAndRoles(userId, role)
                .orElseThrow(() -> new UserNotFoundException("User with Id: " + userId + " and role: " + role + " not found", HttpStatus.NOT_FOUND));
    }


    @Override
    public Page<Sale> getAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    @Transactional
    public ResponseEntity<Sale> updateSaleDetails(Long transactionId, UpdateSaleDetailsDto updateDto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with Id: " + transactionId + " not found", HttpStatus.NOT_FOUND));

        Sale sale = transaction.getSale();
        if (sale == null) {
            throw new SaleNotFoundException("Sale not found for transaction with Id: " + transactionId, HttpStatus.NOT_FOUND);
        }

        transaction.setQuantity(updateDto.getQuantity());
        transaction.setPrice(updateDto.getPrice());

        BigDecimal totalPrice = calculateTotalPrice(sale);
        sale.setTotal(totalPrice);

        saleRepository.save(sale);

        return ResponseEntity.ok(sale);
    }

    private BigDecimal calculateTotalPrice(Sale sale) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        List<Transaction> transactions = transactionRepository.findBySale(sale);
        for (Transaction transaction : transactions) {
            totalPrice = totalPrice.add(transaction.getPrice());
        }
        return totalPrice;
    }
}
