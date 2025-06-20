package com.risf.salestax.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptLineItemDto {
    private int quantity;
    private String productName;
    private BigDecimal finalPrice;
}