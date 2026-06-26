package com.bicicletas.ms_user.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_usuarios")
@Schema(description = "Entidad que representa un usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Schema(
            description = "Identificador único",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nombre del usuario",
            example = "Juan Perez"
    )
    private String nombre;

    @Schema(
            description = "Correo electrónico",
            example = "juan@gmail.com"
    )
    private String email;

    @Schema(
            description = "Contraseña del usuario",
            example = "123456"
    )
    private String password;

    @Schema(
            description = "Estado del usuario",
            example = "ACTIVO"
    )
    private String estado;

}
