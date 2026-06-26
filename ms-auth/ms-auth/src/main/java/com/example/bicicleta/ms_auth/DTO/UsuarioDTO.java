package com.example.bicicleta.ms_auth.DTO;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;

    private String nombre;

    private String email;

    private String password;

    private String estado;
}
