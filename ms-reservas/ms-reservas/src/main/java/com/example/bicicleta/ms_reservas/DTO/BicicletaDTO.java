package com.example.bicicleta.ms_reservas.DTO;

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

