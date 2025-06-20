package com.risf.salestax.infrastructure.web;

import com.risf.salestax.application.dto.CalculateTaxRequest;
import com.risf.salestax.application.dto.ReceiptDto;
import com.risf.salestax.application.service.SalesTaxApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sales-tax")
@Tag(name = "Sales Tax Calculator", description = "API for calculating sales taxes on shopping baskets")
public class SalesTaxController {
    
    private final SalesTaxApplicationService salesTaxApplicationService;

    public SalesTaxController(SalesTaxApplicationService salesTaxApplicationService) {
        this.salesTaxApplicationService = salesTaxApplicationService;
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calculate sales tax for a shopping basket", 
               description = "Calculates sales taxes for a list of products and returns a detailed receipt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sales tax calculated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReceiptDto> calculateSalesTax(@Valid @RequestBody CalculateTaxRequest request) {
        ReceiptDto receipt = salesTaxApplicationService.calculateSalesTax(request);
        return ResponseEntity.ok(receipt);
    }
}