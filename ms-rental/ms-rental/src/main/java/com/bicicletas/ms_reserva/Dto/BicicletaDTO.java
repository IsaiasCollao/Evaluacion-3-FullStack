package com.bicicletas.ms_reserva.Dto;

import lombok.Data;

@Data
public class BicicletaDTO {

    private Long id;

    private String codigo;

    private String marca;

    private String modelo;

    private String estado;

    private String ubicacion;
}


