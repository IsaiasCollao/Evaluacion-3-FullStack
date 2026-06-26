package com.example.bicicleta.ms_pago.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "tbl_pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa un pago asociado a un préstamo")
public class Pago {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Schema(
            description = "Identificador del pago",
            example = "1")
    private Long id;

    @Schema(
            description = "Préstamo asociado",
            example = "5")
    private Long prestamoId;

    @Schema(
            description = "Usuario asociado",
            example = "1")
    private Long usuarioId;

    @Schema(
            description = "Monto a pagar",
            example = "2500")
    private Double monto;

    @Schema(
            description = "Estado del pago",
            example = "PENDIENTE")
    private String estado;

    @Schema(
            description = "Fecha del pago",
            example = "2026-06-23T18:45:00")
    private LocalDateTime fechaPago;
}
