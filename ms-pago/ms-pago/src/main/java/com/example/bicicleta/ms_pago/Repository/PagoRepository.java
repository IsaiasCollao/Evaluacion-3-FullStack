package com.example.bicicleta.ms_pago.Repository;

import com.example.bicicleta.ms_pago.Entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago,Long> {

    List<Pago> findByUsuarioId(Long usuarioId);
        boolean existsByPrestamoId(
            Long prestamoId);


}
