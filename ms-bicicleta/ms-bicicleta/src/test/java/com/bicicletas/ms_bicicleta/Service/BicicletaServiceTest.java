package com.bicicletas.ms_bicicleta.Service;

import com.bicicletas.ms_bicicleta.Entity.Bicicleta;
import com.bicicletas.ms_bicicleta.Repository.BicicletaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BicicletaServiceTest {

    @Mock
    private BicicletaRepository repository;

    @InjectMocks
    private BicicletaService service;

    private Bicicleta bicicletaMock() {
        Bicicleta b = new Bicicleta();
        b.setId(1L);
        b.setCodigo("B001");
        b.setMarca("Oxford");
        b.setModelo("Orion");
        b.setEstado("DISPONIBLE");
        b.setUbicacion("Maipú");
        return b;
    }

    @Test
    void getBicycles_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(bicicletaMock()));
        List<Bicicleta> result = service.getBicycles();
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void getBicycle_idExistente_retornaOptional() {
        when(repository.findById(1L)).thenReturn(Optional.of(bicicletaMock()));
        Optional<Bicicleta> result = service.getBicycle(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void crearBicicleta_codigoNuevo_guardaYRetorna() {
        Bicicleta b = bicicletaMock();
        when(repository.existsByCodigo("B001")).thenReturn(false);
        when(repository.save(b)).thenReturn(b);
        Bicicleta result = service.crearBicicleta(b);
        assertNotNull(result);
        assertEquals("B001", result.getCodigo());
        verify(repository).save(b);
    }

    @Test
    void crearBicicleta_codigoDuplicado_lanzaExcepcion() {
        Bicicleta b = bicicletaMock();
        when(repository.existsByCodigo("B001")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> service.crearBicicleta(b));
        verify(repository, never()).save(any());
    }

    @Test
    void actualizarBicicleta_bicicletaExiste_actualizaYGuarda() {
        Bicicleta existente = bicicletaMock();
        Bicicleta datos = bicicletaMock();
        datos.setMarca("Scott");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.findByCodigo("B001")).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);
        Bicicleta result = service.actualizarBicicleta(1L, datos);
        assertNotNull(result);
        verify(repository).save(existente);
    }

    @Test
    void actualizarBicicleta_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> service.actualizarBicicleta(99L, bicicletaMock()));
    }

    @Test
    void actualizarBicicleta_codigoUsadoPorOtra_lanzaExcepcion() {
        Bicicleta existente = bicicletaMock();
        Bicicleta otra = bicicletaMock();
        otra.setId(2L);
        Bicicleta datos = bicicletaMock();
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.findByCodigo("B001")).thenReturn(Optional.of(otra));
        assertThrows(RuntimeException.class,
                () -> service.actualizarBicicleta(1L, datos));
    }

    @Test
    void ocuparBicicleta_existente_cambiaEstadoOcupada() {
        Bicicleta b = bicicletaMock();
        when(repository.findById(1L)).thenReturn(Optional.of(b));
        when(repository.save(b)).thenReturn(b);
        Bicicleta result = service.ocuparBicicleta(1L);
        assertEquals("OCUPADA", result.getEstado());
    }

    @Test
    void ocuparBicicleta_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.ocuparBicicleta(99L));
    }

    @Test
    void liberarBicicleta_existente_cambiaEstadoDisponible() {
        Bicicleta b = bicicletaMock();
        b.setEstado("OCUPADA");
        when(repository.findById(1L)).thenReturn(Optional.of(b));
        when(repository.save(b)).thenReturn(b);
        Bicicleta result = service.liberarBicicleta(1L);
        assertEquals("DISPONIBLE", result.getEstado());
    }

    @Test
    void liberarBicicleta_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.liberarBicicleta(99L));
    }

    @Test
    void deleteBicycle_llamaDeleteById() {
        service.deleteBicycle(1L);
        verify(repository).deleteById(1L);
    }
}
