package com.bicicletas.ms_reserva.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "tbl_prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa un préstamo de bicicleta")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Schema(
            description = "Identificador del préstamo",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Reserva utilizada para iniciar el préstamo",
            example = "5"
    )
    private Long reservaId;

    @Schema(
            description = "Usuario que realiza el préstamo",
            example = "1"
    )
    private Long usuarioId;

    @Schema(
            description = "Bicicleta entregada",
            example = "2"
    )
    private Long bicicletaId;

    @Schema(
            description = "Fecha y hora de inicio",
            example = "2026-06-23T16:30:00"
    )
    private LocalDateTime fechaInicio;

    @Schema(
            description = "Fecha y hora de devolución",
            example = "2026-06-23T18:15:00"
    )
    private LocalDateTime fechaFin;

    @Schema(
            description = "Estado del préstamo",
            example = "ACTIVO"
    )
    private String estado;

}
