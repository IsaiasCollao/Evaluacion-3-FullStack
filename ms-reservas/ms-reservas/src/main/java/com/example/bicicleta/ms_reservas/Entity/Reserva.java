package com.example.bicicleta.ms_reservas.Entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Entidad que representa una reserva de bicicleta"
)
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Schema(
            description = "Identificador de la reserva",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Usuario asociado a la reserva",
            example = "1"
    )
    private Long usuarioId;

    @Schema(
            description = "Bicicleta reservada",
            example = "1"
    )
    private Long bicicletaId;

    @Schema(
            description = "Fecha de creación de la reserva",
            example = "2026-06-23"
    )
    private LocalDate fechaReserva;

    @Schema(
            description = "Estado de la reserva",
            example = "ACTIVA"
    )
    private String estado;
}
