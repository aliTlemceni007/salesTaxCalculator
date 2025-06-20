package com.risf.salestax.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateValidProduct() {
        Product product = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        
        assertEquals("Book", product.getName());
        assertEquals(new BigDecimal("12.49"), product.getPrice());
        assertEquals(ProductCategory.BOOK, product.getCategory());
        assertFalse(product.isImported());
    }

    @Test
    void shouldTrimProductName() {
        Product product = new Product("  Book  ", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        
        assertEquals("Book", product.getName());
    }

    @Test
    void shouldThrowExceptionForNullName() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Product(null, new BigDecimal("12.49"), ProductCategory.BOOK, false));
    }

    @Test
    void shouldThrowExceptionForEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Product("", new BigDecimal("12.49"), ProductCategory.BOOK, false));
    }

    @Test
    void shouldThrowExceptionForNullPrice() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Product("Book", null, ProductCategory.BOOK, false));
    }

    @Test
    void shouldThrowExceptionForNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Product("Book", new BigDecimal("-1.00"), ProductCategory.BOOK, false));
    }

    @Test
    void shouldThrowExceptionForNullCategory() {
        assertThrows(NullPointerException.class, () -> 
            new Product("Book", new BigDecimal("12.49"), null, false));
    }

    @Test
    void shouldReturnTrueForExemptCategories() {
        assertTrue(new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false).isExemptFromBasicTax());
        assertTrue(new Product("Food", new BigDecimal("0.85"), ProductCategory.FOOD, false).isExemptFromBasicTax());
        assertTrue(new Product("Medicine", new BigDecimal("9.75"), ProductCategory.MEDICAL, false).isExemptFromBasicTax());
    }

    @Test
    void shouldReturnFalseForNonExemptCategories() {
        assertFalse(new Product("CD", new BigDecimal("14.99"), ProductCategory.OTHER, false).isExemptFromBasicTax());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        Product product1 = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        Product product2 = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        Product product3 = new Product("CD", new BigDecimal("14.99"), ProductCategory.OTHER, false);
        
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1, product3);
    }

    @Test
    void shouldHaveToStringMethod() {
        Product product = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        String toString = product.toString();
        
        assertTrue(toString.contains("Book"));
        assertTrue(toString.contains("12.49"));
        assertTrue(toString.contains("BOOK"));
    }
}