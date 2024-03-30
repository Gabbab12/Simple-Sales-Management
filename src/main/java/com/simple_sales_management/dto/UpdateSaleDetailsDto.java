package com.simple_sales_management.dto;

import com.simple_sales_management.model.Transaction;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateSaleDetailsDto {
    private long Quantity;
    private BigDecimal Price;
}
