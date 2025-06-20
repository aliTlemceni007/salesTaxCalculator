package com.risf.salestax.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class ReceiptLineItem {
    private final int quantity;
    private final String productName;
    private final BigDecimal finalPrice;

    public ReceiptLineItem(int quantity, String productName, BigDecimal finalPrice) {
        this.quantity = validateQuantity(quantity);
        this.productName = validateProductName(productName);
        this.finalPrice = validateFinalPrice(finalPrice);
    }

    private int validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        return quantity;
    }

    private String validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        return productName.trim();
    }

    private BigDecimal validateFinalPrice(BigDecimal finalPrice) {
        if (finalPrice == null || finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Final price cannot be null or negative");
        }
        return finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptLineItem that = (ReceiptLineItem) o;
        return quantity == that.quantity && 
               Objects.equals(productName, that.productName) && 
               Objects.equals(finalPrice, that.finalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, productName, finalPrice);
    }

    @Override
    public String toString() {
        return String.format("%d %s: %.2f", quantity, productName, finalPrice);
    }
}