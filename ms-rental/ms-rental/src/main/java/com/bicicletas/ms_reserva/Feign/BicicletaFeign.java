package com.bicicletas.ms_reserva.Feign;

import com.bicicletas.ms_reserva.Dto.BicicletaDTO;
import com.bicicletas.ms_reserva.Dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "ms-bicicleta")
public interface BicicletaFeign {

    @GetMapping("/api/bicicletas/{id}")
    BicicletaDTO obtenerBicicleta(
            @PathVariable Long id);

    @PutMapping("/api/bicicletas/{id}/ocupar")
    void ocuparBicicleta(
            @PathVariable Long id);

    @PutMapping("/api/bicicletas/{id}/liberar")
    void liberarBicicleta(
            @PathVariable Long id);

}
