package com.example.bicicleta.ms_auth.Feign;

import com.example.bicicleta.ms_auth.DTO.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-user")
public interface UsuarioFeign {

    @GetMapping("/api/usuarios/email/{email}")
    UsuarioDTO obtenerPorEmail(
            @PathVariable String email);
}
