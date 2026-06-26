package com.bicicletas.ms_reserva.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para calcular el pago asociado a un préstamo")
public class CalculoRequest {
    @Schema(example = "1")
    private Long prestamoId;

    @Schema(example = "1")
    private Long usuarioId;

    @Schema(example = "95")
    private Long minutosUso;
    
}
