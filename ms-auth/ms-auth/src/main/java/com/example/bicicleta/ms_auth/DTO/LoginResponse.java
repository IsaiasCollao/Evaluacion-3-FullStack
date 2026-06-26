package com.example.bicicleta.ms_auth.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Respuesta del proceso de autenticación")
public class LoginResponse {

    @Schema(
            description = "Token JWT generado")
    private String token;

}
