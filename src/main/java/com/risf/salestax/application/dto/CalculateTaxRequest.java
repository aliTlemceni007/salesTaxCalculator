package com.risf.salestax.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculateTaxRequest {
    
    @NotEmpty(message = "Shopping basket cannot be empty")
    @Valid
    private List<ShoppingBasketItemDto> basketItems;
}