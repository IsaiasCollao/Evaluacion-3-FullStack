package com.bicicletas.api_gateway.Feign;

import com.bicicletas.api_gateway.DTO.ValidateResponse;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth")
public interface AuthFeign {

    @GetMapping("/api/auth/validate")
    ValidateResponse validarToken(

            @RequestHeader("Authorization")

            String authorization

    );

}