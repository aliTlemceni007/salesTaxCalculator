package com.risf.salestax.domain.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TaxRoundingService {
    
    private static final BigDecimal ROUNDING_INCREMENT = new BigDecimal("0.05");
    private static final BigDecimal TWENTY = new BigDecimal("20");

    public BigDecimal roundUpToNearestFiveCents(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal remainder = amount.remainder(ROUNDING_INCREMENT);
        if (remainder.compareTo(BigDecimal.ZERO) == 0) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }
        
        return amount.subtract(remainder).add(ROUNDING_INCREMENT).setScale(2, RoundingMode.HALF_UP);
    }
}