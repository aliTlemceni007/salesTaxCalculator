package com.risf.salestax.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.risf.salestax.application.dto.*;
import com.risf.salestax.domain.model.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SalesTaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCalculateSalesTaxForBasicScenario() throws Exception {
        CalculateTaxRequest request = createBasicScenarioRequest();
        
        mockMvc.perform(post("/api/v1/sales-tax/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lineItems").isArray())
                .andExpect(jsonPath("$.lineItems[0].productName").value("book"))
                .andExpect(jsonPath("$.lineItems[0].finalPrice").value(12.49))
                .andExpect(jsonPath("$.lineItems[1].productName").value("music CD"))
                .andExpect(jsonPath("$.lineItems[1].finalPrice").value(16.49))
                .andExpect(jsonPath("$.lineItems[2].productName").value("chocolate bar"))
                .andExpect(jsonPath("$.lineItems[2].finalPrice").value(0.85))
                .andExpect(jsonPath("$.totalSalesTaxes").value(1.50))
                .andExpect(jsonPath("$.total").value(29.83));
    }

    @Test
    void shouldCalculateSalesTaxForImportedProducts() throws Exception {
        CalculateTaxRequest request = createImportedProductsRequest();
        
        mockMvc.perform(post("/api/v1/sales-tax/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lineItems[0].productName").value("imported box of chocolates"))
                .andExpect(jsonPath("$.lineItems[0].finalPrice").value(10.50))
                .andExpect(jsonPath("$.lineItems[1].productName").value("imported bottle of perfume"))
                .andExpect(jsonPath("$.lineItems[1].finalPrice").value(54.65))
                .andExpect(jsonPath("$.totalSalesTaxes").value(7.65))
                .andExpect(jsonPath("$.total").value(65.15));
    }

    @Test
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        CalculateTaxRequest invalidRequest = new CalculateTaxRequest(List.of());
        
        mockMvc.perform(post("/api/v1/sales-tax/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void shouldReturnBadRequestForNullProductName() throws Exception {
        ProductDto invalidProduct = new ProductDto(null, new BigDecimal("12.49"), ProductCategory.BOOK, false);
        ShoppingBasketItemDto basketItem = new ShoppingBasketItemDto(1, invalidProduct);
        CalculateTaxRequest request = new CalculateTaxRequest(List.of(basketItem));
        
        mockMvc.perform(post("/api/v1/sales-tax/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors").isArray());
    }

    @Test
    void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/api/v1/sales-tax/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sales Tax Calculator Service is running"));
    }

    private CalculateTaxRequest createBasicScenarioRequest() {
        return new CalculateTaxRequest(List.of(
            new ShoppingBasketItemDto(1, new ProductDto("book", new BigDecimal("12.49"), ProductCategory.BOOK, false)),
            new ShoppingBasketItemDto(1, new ProductDto("music CD", new BigDecimal("14.99"), ProductCategory.OTHER, false)),
            new ShoppingBasketItemDto(1, new ProductDto("chocolate bar", new BigDecimal("0.85"), ProductCategory.FOOD, false))
        ));
    }

    private CalculateTaxRequest createImportedProductsRequest() {
        return new CalculateTaxRequest(List.of(
            new ShoppingBasketItemDto(1, new ProductDto("imported box of chocolates", new BigDecimal("10.00"), ProductCategory.FOOD, true)),
            new ShoppingBasketItemDto(1, new ProductDto("imported bottle of perfume", new BigDecimal("47.50"), ProductCategory.OTHER, true))
        ));
    }
}