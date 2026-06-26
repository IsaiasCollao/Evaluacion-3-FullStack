package com.bicicletas.ms_bicicleta.Service;

import com.bicicletas.ms_bicicleta.Entity.Bicicleta;
import com.bicicletas.ms_bicicleta.Repository.BicicletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BicicletaService {

    @Autowired
    BicicletaRepository repository;

    public List<Bicicleta> getBicycles(){
        return repository.findAll();
    }

    public Optional<Bicicleta> getBicycle(Long id){
        return repository.findById(id);
    }

public Bicicleta crearBicicleta(
        Bicicleta bicicleta){

    if(repository.existsByCodigo(
            bicicleta.getCodigo())){

        throw new RuntimeException(
                "El código ya se encuentra registrado");
    }

    return repository.save(
            bicicleta);
    }

    public Bicicleta actualizarBicicleta(
        Long id,
        Bicicleta bicicletaActualizada){

    Bicicleta bicicletaExistente =
            repository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Bicicleta no encontrada"));

    Optional<Bicicleta> bicicletaConMismoCodigo =
            repository.findByCodigo(
                    bicicletaActualizada.getCodigo());

    if(bicicletaConMismoCodigo.isPresent()
            && !bicicletaConMismoCodigo.get()
                    .getId()
                    .equals(id)){

        throw new RuntimeException(
                "El código ya se encuentra registrado");
    }

    bicicletaExistente.setCodigo(
            bicicletaActualizada.getCodigo());

    bicicletaExistente.setMarca(
            bicicletaActualizada.getMarca());

    bicicletaExistente.setModelo(
            bicicletaActualizada.getModelo());

    bicicletaExistente.setEstado(
            bicicletaActualizada.getEstado());

    bicicletaExistente.setUbicacion(
            bicicletaActualizada.getUbicacion());

    return repository.save(
            bicicletaExistente);
    }

    public void deleteBicycle(Long id){
        repository.deleteById(id);
    }

    public Bicicleta ocuparBicicleta(Long id){

    Bicicleta bicicleta =
            repository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Bicicleta no encontrada"));

    bicicleta.setEstado("OCUPADA");

    return repository.save(bicicleta);
    }
    public Bicicleta liberarBicicleta(Long id){

    Bicicleta bicicleta =
            repository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Bicicleta no encontrada"));

    bicicleta.setEstado("DISPONIBLE");

    return repository.save(bicicleta);
    }

}
