package com.bicicletas.api_gateway.Config;

import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gatewayOpenAPI(){

        return new OpenAPI()

                .info(

                        new Info()

                                .title("API Gateway")

                                .version("1.0")

                                .description("Gateway del sistema de bicicletas")

                );

    }

}