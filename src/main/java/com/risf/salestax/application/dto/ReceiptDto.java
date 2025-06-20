package com.risf.salestax.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDto {
    private List<ReceiptLineItemDto> lineItems;
    private BigDecimal totalSalesTaxes;
    private BigDecimal total;
}