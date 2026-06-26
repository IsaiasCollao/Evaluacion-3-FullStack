package com.example.bicicleta.ms_notificacion.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud de notificación")
public class NotificacionRequest {

@Schema(example = "1")
private Long usuarioId;

@Schema(example = "RESERVA_CREADA")
private String tipo;

@Schema(example = "Reserva creada")
private String asunto;

@Schema(example = "Su reserva fue creada correctamente")
private String mensaje;
}
