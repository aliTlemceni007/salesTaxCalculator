package com.risf.salestax.application.mapper;

import com.risf.salestax.application.dto.ReceiptDto;
import com.risf.salestax.application.dto.ReceiptLineItemDto;
import com.risf.salestax.domain.model.Receipt;
import com.risf.salestax.domain.model.ReceiptLineItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReceiptMapper {
    
    public ReceiptDto toDto(Receipt receipt) {
        List<ReceiptLineItemDto> lineItems = receipt.getLineItems()
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
            
        return new ReceiptDto(
            lineItems, 
            receipt.getTotalSalesTaxes(), 
            receipt.getTotal()
        );
    }
    
    public ReceiptLineItemDto toDto(ReceiptLineItem lineItem) {
        return new ReceiptLineItemDto(
            lineItem.getQuantity(),
            lineItem.getProductName(),
            lineItem.getFinalPrice()
        );
    }
    
    public List<ReceiptLineItemDto> toDto(List<ReceiptLineItem> lineItems) {
        return lineItems.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}