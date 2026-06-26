package com.bicicletas.ms_bicicleta.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_bicicletas")

@Schema(
        description = "Entidad que representa una bicicleta"
)
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Schema(
            description = "Identificador único",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Código interno de la bicicleta",
            example = "B001"
    )
    private String codigo;

    @Schema(
            description = "Marca de la bicicleta",
            example = "Oxford"
    )
    private String marca;

    @Schema(
            description = "Modelo de la bicicleta",
            example = "Orion"
    )
    private String modelo;

    @Schema(
            description = "Estado actual",
            example = "DISPONIBLE"
    )
    private String estado;

    @Schema(
            description = "Ubicación física",
            example = "Maipú"
    )
    private String ubicacion;
}