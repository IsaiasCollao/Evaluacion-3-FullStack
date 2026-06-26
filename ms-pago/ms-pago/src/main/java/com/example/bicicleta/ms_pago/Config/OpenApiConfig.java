package com.example.bicicleta.ms_pago.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Pagos")
                        .version("1.0")
                        .description("Gestión de cobros y pagos asociados a préstamos de bicicletas"));
    }
}
