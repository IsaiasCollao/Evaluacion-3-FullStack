package com.example.bicicleta.ms_reservas.Repository;

import com.example.bicicleta.ms_reservas.Entity.Reserva;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByUsuarioIdAndEstado(
            Long usuarioId,
            String estado);

    boolean existsByBicicletaIdAndEstado(
            Long bicicletaId,
            String estado);

    Optional<Reserva> findByUsuarioIdAndBicicletaIdAndEstado(
            Long usuarioId,
            Long bicicletaId,
            String estado);
}
