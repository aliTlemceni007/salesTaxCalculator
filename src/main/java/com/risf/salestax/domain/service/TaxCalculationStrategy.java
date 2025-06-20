package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.Product;

import java.math.BigDecimal;

public interface TaxCalculationStrategy {
    BigDecimal calculateTax(Product product);
    boolean isApplicable(Product product);
}