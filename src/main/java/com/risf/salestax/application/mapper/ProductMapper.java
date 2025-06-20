package com.risf.salestax.application.mapper;

import com.risf.salestax.application.dto.ProductDto;
import com.risf.salestax.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public Product toDomain(ProductDto dto) {
        return new Product(
            dto.getName(),
            dto.getPrice(),
            dto.getCategory(),
            dto.isImported()
        );
    }
    
    public ProductDto toDto(Product product) {
        return new ProductDto(
            product.getName(),
            product.getPrice(),
            product.getCategory(),
            product.isImported()
        );
    }
}