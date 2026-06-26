package com.bicicletas.ms_reserva.Dto;

import lombok.Data;

@Data
public class ReservaDTO {

    private Long id;

    private Long usuarioId;

    private Long bicicletaId;

    private String estado;

}