package com.example.bicicleta.ms_auth.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Credenciales del usuario")
public class LoginRequest {

    @Schema(
            description = "Correo del usuario",
            example = "admin@gmail.com")
    private String email;

    @Schema(
            description = "Contraseña",
            example = "123456")
    private String password;
}
