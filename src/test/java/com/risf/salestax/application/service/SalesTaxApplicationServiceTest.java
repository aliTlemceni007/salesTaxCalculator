package com.risf.salestax.application.service;

import com.risf.salestax.application.dto.*;
import com.risf.salestax.application.mapper.ReceiptMapper;
import com.risf.salestax.application.mapper.ShoppingBasketMapper;
import com.risf.salestax.domain.model.*;
import com.risf.salestax.domain.service.ReceiptGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesTaxApplicationServiceTest {

    @Mock
    private ReceiptGenerationService receiptGenerationService;
    
    @Mock
    private ShoppingBasketMapper shoppingBasketMapper;
    
    @Mock
    private ReceiptMapper receiptMapper;
    
    private SalesTaxApplicationService salesTaxApplicationService;

    @BeforeEach
    void setUp() {
        salesTaxApplicationService = new SalesTaxApplicationService(receiptGenerationService, shoppingBasketMapper, receiptMapper);
    }

    @Test
    void shouldCalculateSalesTaxAndReturnReceiptDto() {
        ProductDto productDto = new ProductDto("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        ShoppingBasketItemDto basketItemDto = new ShoppingBasketItemDto(1, productDto);
        CalculateTaxRequest request = new CalculateTaxRequest(List.of(basketItemDto));
        
        ReceiptLineItem lineItem = new ReceiptLineItem(1, "Book", new BigDecimal("12.49"));
        Receipt receipt = new Receipt(List.of(lineItem), BigDecimal.ZERO, new BigDecimal("12.49"));
        
        when(shoppingBasketMapper.toDomain(anyList())).thenReturn(List.of(new ShoppingBasketItem(1, new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false))));
        when(receiptGenerationService.generateReceipt(any())).thenReturn(receipt);
        when(receiptMapper.toDto(receipt)).thenReturn(new ReceiptDto(List.of(new ReceiptLineItemDto(1, "Book", new BigDecimal("12.49"))), BigDecimal.ZERO, new BigDecimal("12.49")));
        
        ReceiptDto result = salesTaxApplicationService.calculateSalesTax(request);
        
        assertNotNull(result);
        assertEquals(1, result.getLineItems().size());
        assertEquals("Book", result.getLineItems().get(0).getProductName());
        assertEquals(new BigDecimal("12.49"), result.getLineItems().get(0).getFinalPrice());
        assertEquals(BigDecimal.ZERO, result.getTotalSalesTaxes());
        assertEquals(new BigDecimal("12.49"), result.getTotal());
        
        verify(shoppingBasketMapper).toDomain(anyList());
        verify(receiptGenerationService).generateReceipt(any());
        verify(receiptMapper).toDto(receipt);
    }

    @Test
    void shouldCorrectlyMapDtoToModel() {
        ProductDto productDto = new ProductDto("Imported CD", new BigDecimal("14.99"), ProductCategory.OTHER, true);
        ShoppingBasketItemDto basketItemDto = new ShoppingBasketItemDto(2, productDto);
        CalculateTaxRequest request = new CalculateTaxRequest(List.of(basketItemDto));
        
        ReceiptLineItem lineItem = new ReceiptLineItem(2, "Imported CD", new BigDecimal("32.19"));
        Receipt receipt = new Receipt(List.of(lineItem), new BigDecimal("2.21"), new BigDecimal("32.19"));
        
        when(shoppingBasketMapper.toDomain(anyList())).thenReturn(List.of(new ShoppingBasketItem(2, new Product("Imported CD", new BigDecimal("14.99"), ProductCategory.OTHER, true))));
        when(receiptGenerationService.generateReceipt(any())).thenReturn(receipt);
        when(receiptMapper.toDto(receipt)).thenReturn(new ReceiptDto(List.of(new ReceiptLineItemDto(2, "Imported CD", new BigDecimal("32.19"))), new BigDecimal("2.21"), new BigDecimal("32.19")));
        
        ReceiptDto result = salesTaxApplicationService.calculateSalesTax(request);
        
        assertEquals(2, result.getLineItems().get(0).getQuantity());
        assertEquals("Imported CD", result.getLineItems().get(0).getProductName());
        assertEquals(new BigDecimal("2.21"), result.getTotalSalesTaxes());
        
        verify(shoppingBasketMapper).toDomain(anyList());
        verify(receiptGenerationService).generateReceipt(any());
        verify(receiptMapper).toDto(receipt);
    }
}