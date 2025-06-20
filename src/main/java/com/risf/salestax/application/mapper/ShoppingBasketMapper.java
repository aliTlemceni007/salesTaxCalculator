package com.risf.salestax.application.mapper;

import com.risf.salestax.application.dto.ShoppingBasketItemDto;
import com.risf.salestax.domain.model.Product;
import com.risf.salestax.domain.model.ShoppingBasketItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShoppingBasketMapper {
    
    private final ProductMapper productMapper;
    
    public ShoppingBasketMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }
    
    public ShoppingBasketItem toDomain(ShoppingBasketItemDto dto) {
        Product product = productMapper.toDomain(dto.getProduct());
        return new ShoppingBasketItem(dto.getQuantity(), product);
    }
    
    public List<ShoppingBasketItem> toDomain(List<ShoppingBasketItemDto> dtos) {
        return dtos.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    public ShoppingBasketItemDto toDto(ShoppingBasketItem basketItem) {
        return new ShoppingBasketItemDto(
            basketItem.getQuantity(),
            productMapper.toDto(basketItem.getProduct())
        );
    }
    
    public List<ShoppingBasketItemDto> toDto(List<ShoppingBasketItem> basketItems) {
        return basketItems.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}