package com.example.bicicleta.ms_auth.Controller;

import com.example.bicicleta.ms_auth.DTO.ValidateResponse;
import com.example.bicicleta.ms_auth.Security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Autenticación",
        description = "Validación de JWT"
)
public class ValidateController {

    private final JwtService jwtService;

    @Operation(
        summary = "Validar JWT",
        description = "Valida un token y devuelve el email asociado."
)

    @GetMapping("/validate")
    public ResponseEntity<ValidateResponse> validate(

            @RequestHeader("Authorization")
            String authorization){

        try{

            if(!authorization.startsWith("Bearer ")){

                return ResponseEntity.status(
                        HttpStatus.UNAUTHORIZED)

                        .build();

            }

            String token =
                    authorization.substring(7);

            String email =
                    jwtService
                            .validateAndExtractUsername(
                                    token);

            return ResponseEntity.ok(

                    new ValidateResponse(

                            true,

                            email

                    )

            );

        }

        catch (Exception ex){

            return ResponseEntity.status(

                    HttpStatus.UNAUTHORIZED)

                    .build();

        }

    }

}