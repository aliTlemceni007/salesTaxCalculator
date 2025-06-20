package com.risf.salestax.application.service;

import com.risf.salestax.application.dto.*;
import com.risf.salestax.application.mapper.ReceiptMapper;
import com.risf.salestax.application.mapper.ShoppingBasketMapper;
import com.risf.salestax.domain.model.*;
import com.risf.salestax.domain.service.ReceiptGenerationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesTaxApplicationService {
    
    private final ReceiptGenerationService receiptGenerationService;
    private final ShoppingBasketMapper shoppingBasketMapper;
    private final ReceiptMapper receiptMapper;

    public SalesTaxApplicationService(ReceiptGenerationService receiptGenerationService,
                                    ShoppingBasketMapper shoppingBasketMapper,
                                    ReceiptMapper receiptMapper) {
        this.receiptGenerationService = receiptGenerationService;
        this.shoppingBasketMapper = shoppingBasketMapper;
        this.receiptMapper = receiptMapper;
    }

    public ReceiptDto calculateSalesTax(CalculateTaxRequest request) {
        List<ShoppingBasketItem> basketItems = shoppingBasketMapper.toDomain(request.getBasketItems());
        Receipt receipt = receiptGenerationService.generateReceipt(basketItems);
        return receiptMapper.toDto(receipt);
    }

}