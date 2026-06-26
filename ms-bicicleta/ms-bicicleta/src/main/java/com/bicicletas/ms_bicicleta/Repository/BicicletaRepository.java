package com.bicicletas.ms_bicicleta.Repository;

import com.bicicletas.ms_bicicleta.Entity.Bicicleta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BicicletaRepository  extends JpaRepository<Bicicleta,Long> {

        
    boolean existsByCodigo(String codigo);

    Optional<Bicicleta> findByCodigo(String codigo);
}
