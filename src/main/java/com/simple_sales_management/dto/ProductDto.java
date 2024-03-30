package com.simple_sales_management.dto;

import com.simple_sales_management.enums.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private String name;
    private String description;
    private long initialQuantity;
    private ProductCategory category;
    private BigDecimal price;
}
