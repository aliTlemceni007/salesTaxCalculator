package com.risf.salestax.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI receiptOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Receipt API")
                .description("A REST API for calculating sales taxes on shopping baskets")
                .version("v1.0.0"));
    }
}