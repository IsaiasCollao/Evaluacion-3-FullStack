package com.bicicletas.ms_reserva.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud de envío de notificación")
public class NotificationRequest {

    @Schema(example = "1")
    private Long usuarioId;

    @Schema(example = "PRESTAMO_FINALIZADO")
    private String tipo;

    @Schema(example = "Préstamo finalizado")
    private String asunto;

    @Schema(example = "Gracias por utilizar nuestro servicio.")
    private String mensaje;
}
