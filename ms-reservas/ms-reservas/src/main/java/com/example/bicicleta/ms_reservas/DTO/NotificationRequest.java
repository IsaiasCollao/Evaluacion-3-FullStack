package com.example.bicicleta.ms_reservas.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Solicitud de envío de notificaciones"
)
public class NotificationRequest {
    @Schema(
            description = "Identificador del usuario",
            example = "1"
    )
    private Long usuarioId;

    @Schema(
            description = "Tipo de evento",
            example = "RESERVA_CREADA"
    )
    private String tipo;

    @Schema(
            description = "Asunto de la notificación",
            example = "Reserva creada"
    )
    private String asunto;

    @Schema(
            description = "Mensaje enviado al usuario",
            example = "Su reserva fue registrada correctamente"
    )
    private String mensaje;
    
}
