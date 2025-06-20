package com.risf.salestax.domain.service;

import com.risf.salestax.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptGenerationServiceTest {

    @Mock
    private TaxCalculationService taxCalculationService;
    
    private ReceiptGenerationService receiptGenerationService;

    @BeforeEach
    void setUp() {
        receiptGenerationService = new ReceiptGenerationService(taxCalculationService);
    }

    @Test
    void shouldGenerateReceiptForSingleItem() {
        Product book = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        ShoppingBasketItem basketItem = new ShoppingBasketItem(1, book);
        
        when(taxCalculationService.calculateFinalPrice(book)).thenReturn(new BigDecimal("12.49"));
        when(taxCalculationService.calculateTotalTax(book)).thenReturn(BigDecimal.ZERO);
        
        Receipt receipt = receiptGenerationService.generateReceipt(List.of(basketItem));
        
        assertEquals(1, receipt.getLineItems().size());
        assertEquals("Book", receipt.getLineItems().get(0).getProductName());
        assertEquals(new BigDecimal("12.49"), receipt.getLineItems().get(0).getFinalPrice());
        assertEquals(BigDecimal.ZERO, receipt.getTotalSalesTaxes());
        assertEquals(new BigDecimal("12.49"), receipt.getTotal());
    }

    @Test
    void shouldGenerateReceiptForMultipleItems() {
        Product book = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        Product cd = new Product("Music CD", new BigDecimal("14.99"), ProductCategory.OTHER, false);
        Product chocolate = new Product("Chocolate bar", new BigDecimal("0.85"), ProductCategory.FOOD, false);
        
        List<ShoppingBasketItem> basketItems = List.of(
            new ShoppingBasketItem(1, book),
            new ShoppingBasketItem(1, cd),
            new ShoppingBasketItem(1, chocolate)
        );
        
        when(taxCalculationService.calculateFinalPrice(book)).thenReturn(new BigDecimal("12.49"));
        when(taxCalculationService.calculateFinalPrice(cd)).thenReturn(new BigDecimal("16.49"));
        when(taxCalculationService.calculateFinalPrice(chocolate)).thenReturn(new BigDecimal("0.85"));
        
        when(taxCalculationService.calculateTotalTax(book)).thenReturn(BigDecimal.ZERO);
        when(taxCalculationService.calculateTotalTax(cd)).thenReturn(new BigDecimal("1.50"));
        when(taxCalculationService.calculateTotalTax(chocolate)).thenReturn(BigDecimal.ZERO);
        
        Receipt receipt = receiptGenerationService.generateReceipt(basketItems);
        
        assertEquals(3, receipt.getLineItems().size());
        assertEquals(new BigDecimal("1.50"), receipt.getTotalSalesTaxes());
        assertEquals(new BigDecimal("29.83"), receipt.getTotal());
    }

    @Test
    void shouldHandleMultipleQuantities() {
        Product book = new Product("Book", new BigDecimal("12.49"), ProductCategory.BOOK, false);
        ShoppingBasketItem basketItem = new ShoppingBasketItem(2, book);
        
        when(taxCalculationService.calculateFinalPrice(book)).thenReturn(new BigDecimal("12.49"));
        when(taxCalculationService.calculateTotalTax(book)).thenReturn(BigDecimal.ZERO);
        
        Receipt receipt = receiptGenerationService.generateReceipt(List.of(basketItem));
        
        assertEquals(1, receipt.getLineItems().size());
        assertEquals(2, receipt.getLineItems().get(0).getQuantity());
        assertEquals(new BigDecimal("24.98"), receipt.getLineItems().get(0).getFinalPrice());
        assertEquals(BigDecimal.ZERO, receipt.getTotalSalesTaxes());
        assertEquals(new BigDecimal("24.98"), receipt.getTotal());
    }

    @Test
    void shouldThrowExceptionForNullBasketItems() {
        assertThrows(IllegalArgumentException.class, () -> 
            receiptGenerationService.generateReceipt(null));
    }

    @Test
    void shouldThrowExceptionForEmptyBasketItems() {
        assertThrows(IllegalArgumentException.class, () -> 
            receiptGenerationService.generateReceipt(List.of()));
    }
}