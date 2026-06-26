package com.example.bicicleta.ms_pago.Service;

import com.example.bicicleta.ms_pago.DTO.CalculoRequest;
import com.example.bicicleta.ms_pago.DTO.NotificationRequest;
import com.example.bicicleta.ms_pago.Entity.Pago;
import com.example.bicicleta.ms_pago.Feign.NotificacionFeign;
import com.example.bicicleta.ms_pago.Repository.PagoRepository;
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
class PagoServiceTest {

    @Mock private PagoRepository repository;
    @Mock private NotificacionFeign notificacionFeign;

    @InjectMocks
    private PagoService service;

    @Test
    void calcularPago_menosDe30Min_montoBase1000() {
        CalculoRequest request = new CalculoRequest(1L, 1L, 20L);
        when(repository.existsByPrestamoId(1L)).thenReturn(false);
        Pago pago = Pago.builder().id(1L).prestamoId(1L).usuarioId(1L).monto(1000.0).estado("PENDIENTE").build();
        when(repository.save(any())).thenReturn(pago);

        Pago result = service.calcularPago(request);

        assertEquals(1000.0, result.getMonto());
        assertEquals("PENDIENTE", result.getEstado());
        verify(notificacionFeign).enviar(any(NotificationRequest.class));
    }

    @Test
    void calcularPago_exactamente30Min_montoBase1000() {
        CalculoRequest request = new CalculoRequest(1L, 1L, 30L);
        when(repository.existsByPrestamoId(1L)).thenReturn(false);
        Pago pago = Pago.builder().id(1L).prestamoId(1L).usuarioId(1L).monto(1000.0).estado("PENDIENTE").build();
        when(repository.save(any())).thenReturn(pago);

        Pago result = service.calcularPago(request);

        assertEquals(1000.0, result.getMonto());
    }

    @Test
    void calcularPago_60Min_monto1500() {
        CalculoRequest request = new CalculoRequest(1L, 1L, 60L);
        when(repository.existsByPrestamoId(1L)).thenReturn(false);
        Pago pago = Pago.builder().id(1L).prestamoId(1L).usuarioId(1L).monto(1500.0).estado("PENDIENTE").build();
        when(repository.save(any())).thenReturn(pago);

        Pago result = service.calcularPago(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void calcularPago_pagoYaExiste_lanzaExcepcion() {
        CalculoRequest request = new CalculoRequest(1L, 1L, 20L);
        when(repository.existsByPrestamoId(1L)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> service.calcularPago(request));
        verify(repository, never()).save(any());
    }

    @Test
    void pagar_estadoPendiente_cambiaAPagado() {
        Pago pago = Pago.builder().id(1L).usuarioId(1L).monto(1000.0).estado("PENDIENTE").build();
        when(repository.findById(1L)).thenReturn(Optional.of(pago));
        when(repository.save(pago)).thenReturn(pago);

        Pago result = service.pagar(1L);

        assertEquals("PAGADO", result.getEstado());
        assertNotNull(result.getFechaPago());
        verify(notificacionFeign).enviar(any(NotificationRequest.class));
    }

    @Test
    void pagar_yaEstabaPageado_lanzaExcepcion() {
        Pago pago = Pago.builder().id(1L).estado("PAGADO").build();
        when(repository.findById(1L)).thenReturn(Optional.of(pago));
        assertThrows(RuntimeException.class, () -> service.pagar(1L));
    }

    @Test
    void pagar_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.pagar(99L));
    }

    @Test
    void buscarPorId_existente_retornaPago() {
        Pago pago = Pago.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(pago));
        assertEquals(1L, service.buscarPorId(1L).getId());
    }

    @Test
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void obtenerPagosUsuario_retornaLista() {
        when(repository.findByUsuarioId(1L)).thenReturn(List.of(new Pago()));
        assertEquals(1, service.obtenerPagosUsuario(1L).size());
    }

    @Test
    void listar_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new Pago(), new Pago()));
        assertEquals(2, service.listar().size());
    }
}
