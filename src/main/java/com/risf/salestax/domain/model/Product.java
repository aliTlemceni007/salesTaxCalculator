package com.risf.salestax.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Product {
    private final String name;
    private final BigDecimal price;
    private final ProductCategory category;
    private final boolean imported;

    public Product(String name, BigDecimal price, ProductCategory category, boolean imported) {
        this.name = validateName(name);
        this.price = validatePrice(price);
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.imported = imported;
    }

    public boolean isExemptFromBasicTax() {
        return category == ProductCategory.BOOK || 
               category == ProductCategory.FOOD || 
               category == ProductCategory.MEDICAL;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        return name.trim();
    }

    private BigDecimal validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be null or negative");
        }
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return imported == product.imported && 
               Objects.equals(name, product.name) && 
               Objects.equals(price, product.price) && 
               category == product.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category, imported);
    }

    @Override
    public String toString() {
        return String.format("Product{name='%s', price=%s, category=%s, imported=%s}", 
                           name, price, category, imported);
    }
}