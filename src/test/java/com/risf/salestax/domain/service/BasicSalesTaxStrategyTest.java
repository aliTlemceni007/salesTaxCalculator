package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.Product;
import com.risf.salestax.domain.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BasicSalesTaxStrategyTest {

    private BasicSalesTaxStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new BasicSalesTaxStrategy(new BigDecimal("0.10"));
    }

    @Test
    void shouldCalculateTaxForNonExemptProducts() {
        Product product = new Product("Music CD", new BigDecimal("14.99"), ProductCategory.OTHER, false);
        
        BigDecimal tax = strategy.calculateTax(product);
        
        assertEquals(new BigDecimal("1.50"), tax);
        assertTrue(strategy.isApplicable(product));
    }

    @Test
    void shouldNotCalculateTaxForExemptProducts() {
        Product book = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        Product food = new Product("Chocolate", new BigDecimal("0.85"), ProductCategory.FOOD, false);
        Product medical = new Product("Pills", new BigDecimal("9.75"), ProductCategory.MEDICAL, false);
        
        assertEquals(BigDecimal.ZERO, strategy.calculateTax(book));
        assertEquals(BigDecimal.ZERO, strategy.calculateTax(food));
        assertEquals(BigDecimal.ZERO, strategy.calculateTax(medical));
        
        assertFalse(strategy.isApplicable(book));
        assertFalse(strategy.isApplicable(food));
        assertFalse(strategy.isApplicable(medical));
    }

    @Test
    void shouldApplyTaxToImportedNonExemptProducts() {
        Product importedCD = new Product("Imported Music CD", new BigDecimal("14.99"), ProductCategory.OTHER, true);
        
        BigDecimal tax = strategy.calculateTax(importedCD);
        
        assertEquals(new BigDecimal("1.50"), tax);
        assertTrue(strategy.isApplicable(importedCD));
    }

    @Test
    void shouldNotApplyTaxToImportedExemptProducts() {
        Product importedBook = new Product("Imported Book", new BigDecimal("12.49"), ProductCategory.BOOK, true);
        
        assertEquals(BigDecimal.ZERO, strategy.calculateTax(importedBook));
        assertFalse(strategy.isApplicable(importedBook));
    }
}