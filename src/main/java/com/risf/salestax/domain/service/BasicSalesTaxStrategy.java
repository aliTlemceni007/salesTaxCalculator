package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BasicSalesTaxStrategy implements TaxCalculationStrategy {
    
    private final BigDecimal basicTaxRate;

    public BasicSalesTaxStrategy(@Value("${tax.basic.rate:0.10}") BigDecimal basicTaxRate) {
        this.basicTaxRate = basicTaxRate;
    }

    @Override
    public BigDecimal calculateTax(Product product) {
        if (!isApplicable(product)) {
            return BigDecimal.ZERO;
        }
        return product.getPrice().multiply(basicTaxRate).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean isApplicable(Product product) {
        return !product.isExemptFromBasicTax();
    }
}