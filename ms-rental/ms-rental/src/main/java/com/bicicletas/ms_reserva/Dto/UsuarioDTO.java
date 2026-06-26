package com.bicicletas.ms_reserva.Dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String email;
    private String estado;

}
