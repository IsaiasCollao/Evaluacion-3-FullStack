package com.bicicletas.ms_reserva.Feign;

import com.bicicletas.ms_reserva.Dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-user")
public interface UsuarioFeign {

    @GetMapping("/api/usuarios/email/{email}")
    UsuarioDTO obtenerPorEmail(
            @PathVariable String email);

}