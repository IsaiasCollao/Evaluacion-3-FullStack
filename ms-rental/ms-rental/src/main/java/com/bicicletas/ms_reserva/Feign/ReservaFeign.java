package com.bicicletas.ms_reserva.Feign;

import com.bicicletas.ms_reserva.Dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "ms-reservas")
public interface ReservaFeign {

@GetMapping("/api/reservas/usuario/{usuarioId}/bicicleta/{bicicletaId}")
ReservaDTO obtenerReservaActiva(
        @PathVariable Long usuarioId,
        @PathVariable Long bicicletaId);

    @PutMapping("/api/reservas/{id}/utilizar")
    ReservaDTO completarReserva(
            @PathVariable Long id);
    
}
