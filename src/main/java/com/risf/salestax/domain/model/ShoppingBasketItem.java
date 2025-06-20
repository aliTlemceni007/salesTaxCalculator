package com.risf.salestax.domain.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ShoppingBasketItem {
    private final int quantity;
    private final Product product;

    public ShoppingBasketItem(int quantity, Product product) {
        this.quantity = validateQuantity(quantity);
        this.product = Objects.requireNonNull(product, "Product cannot be null");
    }

    private int validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingBasketItem that = (ShoppingBasketItem) o;
        return quantity == that.quantity && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, product);
    }

    @Override
    public String toString() {
        return String.format("ShoppingBasketItem{quantity=%d, product=%s}", quantity, product);
    }
}