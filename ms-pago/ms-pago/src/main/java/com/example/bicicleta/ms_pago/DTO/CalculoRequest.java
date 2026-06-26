package com.example.bicicleta.ms_pago.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoRequest {

    private Long prestamoId;

    private Long usuarioId;

    private Long minutosUso;
    
}
