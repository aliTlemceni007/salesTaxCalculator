package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptGenerationService {
    
    private final TaxCalculationService taxCalculationService;

    public ReceiptGenerationService(TaxCalculationService taxCalculationService) {
        this.taxCalculationService = taxCalculationService;
    }

    public Receipt generateReceipt(List<ShoppingBasketItem> basketItems) {
        if (basketItems == null || basketItems.isEmpty()) {
            throw new IllegalArgumentException("Shopping basket cannot be null or empty");
        }

        List<ReceiptLineItem> lineItems = basketItems.stream()
            .map(this::createReceiptLineItem)
            .collect(Collectors.toList());

        BigDecimal totalSalesTaxes = calculateTotalSalesTaxes(basketItems);
        BigDecimal total = calculateTotal(lineItems);

        return new Receipt(lineItems, totalSalesTaxes, total);
    }

    private ReceiptLineItem createReceiptLineItem(ShoppingBasketItem basketItem) {
        Product product = basketItem.getProduct();
        BigDecimal finalPrice = taxCalculationService.calculateFinalPrice(product);
        
        if (basketItem.getQuantity() > 1) {
            finalPrice = finalPrice.multiply(new BigDecimal(basketItem.getQuantity()));
        }
        
        return new ReceiptLineItem(
            basketItem.getQuantity(),
            product.getName(),
            finalPrice
        );
    }

    private BigDecimal calculateTotalSalesTaxes(List<ShoppingBasketItem> basketItems) {
        return basketItems.stream()
            .map(item -> {
                BigDecimal taxPerUnit = taxCalculationService.calculateTotalTax(item.getProduct());
                return taxPerUnit.multiply(new BigDecimal(item.getQuantity()));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotal(List<ReceiptLineItem> lineItems) {
        return lineItems.stream()
            .map(ReceiptLineItem::getFinalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}