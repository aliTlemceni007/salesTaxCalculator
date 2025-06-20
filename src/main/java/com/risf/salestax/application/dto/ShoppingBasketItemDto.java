package com.risf.salestax.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingBasketItemDto {
    
    @Positive(message = "Quantity must be positive")
    private int quantity;
    
    @NotNull(message = "Product is required")
    @Valid
    private ProductDto product;
}