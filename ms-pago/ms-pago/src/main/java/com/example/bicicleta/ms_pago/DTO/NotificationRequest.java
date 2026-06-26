package com.example.bicicleta.ms_pago.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class NotificationRequest {

    private Long usuarioId;

    private String tipo;

    private String asunto;

    private String mensaje;
}
