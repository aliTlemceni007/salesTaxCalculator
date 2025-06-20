package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.Product;
import com.risf.salestax.domain.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ImportDutyTaxStrategyTest {

    private ImportDutyTaxStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ImportDutyTaxStrategy(new BigDecimal("0.05"));
    }

    @Test
    void shouldCalculateImportDutyForImportedProducts() {
        Product importedCD = new Product("Imported Music CD", new BigDecimal("14.99"), ProductCategory.OTHER, true);
        
        BigDecimal tax = strategy.calculateTax(importedCD);
        
        assertEquals(new BigDecimal("0.75"), tax);
        assertTrue(strategy.isApplicable(importedCD));
    }

    @Test
    void shouldCalculateImportDutyForImportedExemptProducts() {
        Product importedBook = new Product("Imported Book", new BigDecimal("12.49"), ProductCategory.BOOK, true);
        Product importedFood = new Product("Imported Chocolate", new BigDecimal("11.25"), ProductCategory.FOOD, true);
        
        assertEquals(new BigDecimal("0.62"), strategy.calculateTax(importedBook));
        assertEquals(new BigDecimal("0.56"), strategy.calculateTax(importedFood));
        
        assertTrue(strategy.isApplicable(importedBook));
        assertTrue(strategy.isApplicable(importedFood));
    }

    @Test
    void shouldNotCalculateImportDutyForNonImportedProducts() {
        Product localCD = new Product("Music CD", new BigDecimal("14.99"), ProductCategory.OTHER, false);
        Product localBook = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        
        assertEquals(BigDecimal.ZERO, strategy.calculateTax(localCD));
        assertEquals(BigDecimal.ZERO, strategy.calculateTax(localBook));
        
        assertFalse(strategy.isApplicable(localCD));
        assertFalse(strategy.isApplicable(localBook));
    }
}