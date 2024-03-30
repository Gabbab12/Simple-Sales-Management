package com.simple_sales_management.service.implementation;

import com.simple_sales_management.dto.ProductDto;
import com.simple_sales_management.dto.UpdateProductDetailsDto;
import com.simple_sales_management.exception.ProductAlreadyExist;
import com.simple_sales_management.exception.ProductNotFoundException;
import com.simple_sales_management.model.Product;
import com.simple_sales_management.model.Transaction;
import com.simple_sales_management.repository.ProductRepository;
import com.simple_sales_management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Product> createProduct(ProductDto productDto){
        if (productRepository.existsByName(productDto.getName())){
            throw new ProductAlreadyExist("The product you are trying to register is already existing", HttpStatus.BAD_REQUEST);
        }

        Product product = modelMapper.map(productDto, Product.class);

        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Override
        public Page<Product> getAllProductsWithAvailableQuantity(Pageable pageable, List<Transaction> transactions) {
            Page<Product> productsPage = productRepository.findAll(pageable);
             productsPage.forEach(product -> {
            long availableQuantity = calculateAvailableQuantity(product, transactions);
            product.setAvailableQuantity(availableQuantity);
        });

        return productsPage;
    }

    public long calculateAvailableQuantity(Product product, List<Transaction> transactions) {
        long totalSoldQuantity = transactions.stream()
                .filter(transaction -> transaction.getProducts().getId().equals(product.getId()))
                .mapToLong(Transaction::getQuantity)
                .sum();

        return product.getInitialQuantity() - totalSoldQuantity;
    }

    @Override
    public ResponseEntity<Product> viewProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product with Id : " + productId + "not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Override
    public ResponseEntity<Product> updateProductDetails(UpdateProductDetailsDto updateProduct, Long productId){
        Optional<Product> existingProductOptional = productRepository.findById(productId);

        if (existingProductOptional.isEmpty()) {
            throw new ProductNotFoundException("Product with Id : " + productId + " not found", HttpStatus.NOT_FOUND);
        }
        Product existingProduct = existingProductOptional.get();
        modelMapper.map(updateProduct, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);

        return ResponseEntity.ok(updatedProduct);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long productId){
        Optional<Product> product = Optional.ofNullable(productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product with Id : " + productId + " not found", HttpStatus.NOT_FOUND)));
        productRepository.delete(product);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product deleted successfully");
    }
}
