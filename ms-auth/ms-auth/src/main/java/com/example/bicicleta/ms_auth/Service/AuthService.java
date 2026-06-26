package com.example.bicicleta.ms_auth.Service;

import com.example.bicicleta.ms_auth.DTO.LoginRequest;
import com.example.bicicleta.ms_auth.DTO.LoginResponse;
import com.example.bicicleta.ms_auth.Security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

    System.out.println("===== INICIO LOGIN =====");

    try {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),

                        request.getPassword()

                )

        );

        System.out.println("Autenticación correcta");

    } catch (Exception ex) {

        System.out.println("ERROR AUTENTICANDO:");
        ex.printStackTrace();

        throw ex;

    }

    String token = jwtService.generateToken(
            request.getEmail());

    System.out.println("JWT generado");

    return new LoginResponse(token);

}
}