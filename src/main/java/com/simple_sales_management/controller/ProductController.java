package com.simple_sales_management.controller;

import com.simple_sales_management.dto.ProductDto;
import com.simple_sales_management.dto.UpdateProductDetailsDto;
import com.simple_sales_management.model.Product;
import com.simple_sales_management.model.Sale;
import com.simple_sales_management.model.Transaction;
import com.simple_sales_management.model.User;
import com.simple_sales_management.service.implementation.ProductServiceImpl;
import com.simple_sales_management.service.implementation.SaleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productService;
    private final SaleServiceImpl saleService;


    @PostMapping("/create-product")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
        return productService.createProduct(productDto);
    }

    @GetMapping("/view-product/{productId}")
    public ResponseEntity<Product> viewUser(@PathVariable Long productId){
        return productService.viewProduct(productId);
    }

    @GetMapping("/withAvailableQuantity")
    public ResponseEntity<Page<Product>> getAllProductsWithAvailableQuantity(Pageable pageable, List<Transaction> transactions) {
        Page<Product> productsPage = productService.getAllProductsWithAvailableQuantity(pageable, transactions);
        return ResponseEntity.ok(productsPage);    }

    @GetMapping("/view-product/{productId}")
    public ResponseEntity<Product> viewProduct(@PathVariable Long productId){
        return productService.viewProduct(productId);
    }

    @PutMapping("/update-product-details/{productId}")
    public ResponseEntity<Product> updateProductDetails(@PathVariable Long productId, @RequestBody UpdateProductDetailsDto updateProduct){
        return productService.updateProductDetails(updateProduct, productId);
    }

    @DeleteMapping("delete-product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        return productService.deleteProduct(productId);
    }
}
