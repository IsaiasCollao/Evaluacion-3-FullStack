package com.bicicletas.ms_reserva.Repository;

import com.bicicletas.ms_reserva.Entity.Prestamo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo,Long> {

    Optional<Prestamo> findByUsuarioIdAndBicicletaIdAndEstado(
        Long usuarioId,
        Long bicicletaId,
        String estado);

    
}
