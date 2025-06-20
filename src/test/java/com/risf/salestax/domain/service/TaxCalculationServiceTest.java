package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.Product;
import com.risf.salestax.domain.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCalculationServiceTest {

    @Mock
    private TaxCalculationStrategy basicTaxStrategy;
    
    @Mock
    private TaxCalculationStrategy importDutyStrategy;
    
    @Mock
    private TaxRoundingService taxRoundingService;
    
    private TaxCalculationService taxCalculationService;

    @BeforeEach
    void setUp() {
        taxCalculationService = new TaxCalculationService(
            List.of(basicTaxStrategy, importDutyStrategy), 
            taxRoundingService
        );
    }

    @Test
    void shouldCalculateTotalTaxWithMultipleStrategies() {
        Product product = new Product("Imported CD", new BigDecimal("14.99"), ProductCategory.OTHER, true);
        
        when(basicTaxStrategy.isApplicable(product)).thenReturn(true);
        when(basicTaxStrategy.calculateTax(product)).thenReturn(new BigDecimal("1.50"));
        when(importDutyStrategy.isApplicable(product)).thenReturn(true);
        when(importDutyStrategy.calculateTax(product)).thenReturn(new BigDecimal("0.75"));
        when(taxRoundingService.roundUpToNearestFiveCents(new BigDecimal("2.25"))).thenReturn(new BigDecimal("2.25"));
        
        BigDecimal totalTax = taxCalculationService.calculateTotalTax(product);
        
        assertEquals(new BigDecimal("2.25"), totalTax);
        verify(basicTaxStrategy).isApplicable(product);
        verify(basicTaxStrategy).calculateTax(product);
        verify(importDutyStrategy).isApplicable(product);
        verify(importDutyStrategy).calculateTax(product);
        verify(taxRoundingService).roundUpToNearestFiveCents(new BigDecimal("2.25"));
    }

    @Test
    void shouldCalculateFinalPrice() {
        Product product = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        
        when(basicTaxStrategy.isApplicable(product)).thenReturn(false);
        when(importDutyStrategy.isApplicable(product)).thenReturn(false);
        when(taxRoundingService.roundUpToNearestFiveCents(BigDecimal.ZERO)).thenReturn(BigDecimal.ZERO);
        
        BigDecimal finalPrice = taxCalculationService.calculateFinalPrice(product);
        
        assertEquals(new BigDecimal("12.49"), finalPrice);
    }

    @Test
    void shouldOnlyApplyApplicableStrategies() {
        Product product = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        
        when(basicTaxStrategy.isApplicable(product)).thenReturn(false);
        when(importDutyStrategy.isApplicable(product)).thenReturn(false);
        when(taxRoundingService.roundUpToNearestFiveCents(BigDecimal.ZERO)).thenReturn(BigDecimal.ZERO);
        
        BigDecimal totalTax = taxCalculationService.calculateTotalTax(product);
        
        assertEquals(BigDecimal.ZERO, totalTax);
        verify(basicTaxStrategy, never()).calculateTax(product);
        verify(importDutyStrategy, never()).calculateTax(product);
    }
}