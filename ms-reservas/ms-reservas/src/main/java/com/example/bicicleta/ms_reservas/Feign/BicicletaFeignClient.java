package com.example.bicicleta.ms_reservas.Feign;

import com.example.bicicleta.ms_reservas.DTO.BicicletaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-bicicleta")
public interface BicicletaFeignClient {

    @GetMapping("/api/bicicletas/{id}")
    BicicletaDTO obtenerBicicletaPorId(
            @PathVariable Long id);
}
