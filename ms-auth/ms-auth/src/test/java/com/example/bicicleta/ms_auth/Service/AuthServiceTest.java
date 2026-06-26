package com.example.bicicleta.ms_auth.Service;

import com.example.bicicleta.ms_auth.DTO.LoginRequest;
import com.example.bicicleta.ms_auth.DTO.LoginResponse;
import com.example.bicicleta.ms_auth.Security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService service;

    @Test
    void login_credencialesValidas_retornaToken() {
        LoginRequest request = new LoginRequest("juan@test.com", "123456");
        when(jwtService.generateToken("juan@test.com")).thenReturn("jwt.token.mock");

        LoginResponse response = service.login(request);

        assertNotNull(response);
        assertEquals("jwt.token.mock", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken("juan@test.com");
    }

    @Test
    void login_credencialesInvalidas_lanzaExcepcion() {
        LoginRequest request = new LoginRequest("juan@test.com", "wrongpassword");
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThrows(BadCredentialsException.class, () -> service.login(request));
        verify(jwtService, never()).generateToken(any());
    }
}
