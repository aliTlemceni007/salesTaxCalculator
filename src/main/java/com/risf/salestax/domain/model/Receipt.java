package com.risf.salestax.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class Receipt {
    private final List<ReceiptLineItem> lineItems;
    private final BigDecimal totalSalesTaxes;
    private final BigDecimal total;

    public Receipt(List<ReceiptLineItem> lineItems, BigDecimal totalSalesTaxes, BigDecimal total) {
        this.lineItems = Collections.unmodifiableList(
            Objects.requireNonNull(lineItems, "Line items cannot be null")
        );
        this.totalSalesTaxes = validateAmount(totalSalesTaxes, "Total sales taxes");
        this.total = validateAmount(total, "Total");
        validateTotalConsistency();
    }

    private BigDecimal validateAmount(BigDecimal amount, String fieldName) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be null or negative");
        }
        return amount;
    }

    private void validateTotalConsistency() {
        BigDecimal calculatedSubtotal = lineItems.stream()
            .map(ReceiptLineItem::getFinalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (calculatedSubtotal.compareTo(total) != 0) {
            throw new IllegalArgumentException("Total does not match sum of line item prices");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return Objects.equals(lineItems, receipt.lineItems) && 
               Objects.equals(totalSalesTaxes, receipt.totalSalesTaxes) && 
               Objects.equals(total, receipt.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineItems, totalSalesTaxes, total);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        lineItems.forEach(item -> sb.append(item).append("\n"));
        sb.append(String.format("Sales Taxes: %.2f", totalSalesTaxes));
        sb.append(String.format(" Total: %.2f", total));
        return sb.toString();
    }
}