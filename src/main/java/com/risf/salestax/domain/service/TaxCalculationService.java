package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TaxCalculationService {
    
    private final List<TaxCalculationStrategy> taxStrategies;
    private final TaxRoundingService taxRoundingService;

    public TaxCalculationService(List<TaxCalculationStrategy> taxStrategies, 
                               TaxRoundingService taxRoundingService) {
        this.taxStrategies = taxStrategies;
        this.taxRoundingService = taxRoundingService;
    }

    public BigDecimal calculateTotalTax(Product product) {
        BigDecimal totalTax = taxStrategies.stream()
            .filter(strategy -> strategy.isApplicable(product))
            .map(strategy -> strategy.calculateTax(product))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return taxRoundingService.roundUpToNearestFiveCents(totalTax);
    }

    public BigDecimal calculateFinalPrice(Product product) {
        BigDecimal tax = calculateTotalTax(product);
        return product.getPrice().add(tax);
    }
}