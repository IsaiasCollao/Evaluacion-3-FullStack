package com.example.bicicleta.ms_auth.Controller;

import com.example.bicicleta.ms_auth.DTO.LoginRequest;
import com.example.bicicleta.ms_auth.DTO.LoginResponse;
import com.example.bicicleta.ms_auth.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Autenticación",
        description = "Inicio de sesión y generación de JWT"
)
public class AuthController {

    private final AuthService service;

    @Operation(
            summary = "Iniciar sesión",
            description = "Valida las credenciales del usuario y genera un JWT"
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación correcta"),

            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas")

    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request){

        return ResponseEntity.ok(
                service.login(request));

    }
}
