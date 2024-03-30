package com.simple_sales_management.service;

import com.simple_sales_management.dto.ProductDto;
import com.simple_sales_management.dto.UpdateProductDetailsDto;
import com.simple_sales_management.model.Product;
import com.simple_sales_management.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<Product> createProduct(ProductDto productDto);

    Page<Product> getAllProductsWithAvailableQuantity(Pageable pageable, List<Transaction> transactions);

    ResponseEntity<Product> viewProduct(Long productId);

    ResponseEntity<Product> updateProductDetails(UpdateProductDetailsDto updateProduct, Long productId);

    ResponseEntity<String> deleteProduct(Long productId);
}
