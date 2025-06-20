package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ImportDutyTaxStrategy implements TaxCalculationStrategy {
    
    private final BigDecimal importDutyRate;

    public ImportDutyTaxStrategy(@Value("${tax.import.rate:0.05}") BigDecimal importDutyRate) {
        this.importDutyRate = importDutyRate;
    }

    @Override
    public BigDecimal calculateTax(Product product) {
        if (!isApplicable(product)) {
            return BigDecimal.ZERO;
        }
        return product.getPrice().multiply(importDutyRate).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean isApplicable(Product product) {
        return product.isImported();
    }
}