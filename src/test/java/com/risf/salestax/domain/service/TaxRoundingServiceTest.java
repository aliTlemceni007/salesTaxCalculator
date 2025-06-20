package com.risf.salestax.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxRoundingServiceTest {

    private TaxRoundingService taxRoundingService;

    @BeforeEach
    void setUp() {
        taxRoundingService = new TaxRoundingService();
    }

    @Test
    void shouldRoundUpToNearestFiveCents() {
        assertEquals(new BigDecimal("1.50"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("1.49")));
        assertEquals(new BigDecimal("1.45"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("1.45")));
        assertEquals(new BigDecimal("1.50"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("1.46")));
        assertEquals(new BigDecimal("1.50"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("1.50")));
    }

    @Test
    void shouldHandleSmallAmounts() {
        assertEquals(new BigDecimal("0.05"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("0.01")));
        assertEquals(new BigDecimal("0.05"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("0.04")));
        assertEquals(new BigDecimal("0.05"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("0.05")));
    }

    @Test
    void shouldHandleExactMultiples() {
        assertEquals(new BigDecimal("0.10"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("0.10")));
        assertEquals(new BigDecimal("0.15"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("0.15")));
        assertEquals(new BigDecimal("1.00"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("1.00")));
    }

    @Test
    void shouldReturnZeroForNullOrNegativeInput() {
        assertEquals(BigDecimal.ZERO, taxRoundingService.roundUpToNearestFiveCents(null));
        assertEquals(BigDecimal.ZERO, taxRoundingService.roundUpToNearestFiveCents(BigDecimal.ZERO));
        assertEquals(BigDecimal.ZERO, taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("-0.10")));
    }

    @Test
    void shouldHandleSpecificTestCases() {
        assertEquals(new BigDecimal("1.50"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("1.4999")));
        assertEquals(new BigDecimal("0.75"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("0.7125")));
        assertEquals(new BigDecimal("0.05"), taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("0.042")));
    }
}