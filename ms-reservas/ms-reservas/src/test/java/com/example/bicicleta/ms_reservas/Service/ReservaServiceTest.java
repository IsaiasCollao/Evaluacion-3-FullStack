package com.example.bicicleta.ms_reservas.Service;

import com.example.bicicleta.ms_reservas.DTO.BicicletaDTO;
import com.example.bicicleta.ms_reservas.DTO.NotificationRequest;
import com.example.bicicleta.ms_reservas.DTO.ReservaRequest;
import com.example.bicicleta.ms_reservas.DTO.UsuarioDTO;
import com.example.bicicleta.ms_reservas.Entity.Reserva;
import com.example.bicicleta.ms_reservas.Feign.BicicletaFeignClient;
import com.example.bicicleta.ms_reservas.Feign.NotificacionFeign;
import com.example.bicicleta.ms_reservas.Feign.UsuarioFeignClient;
import com.example.bicicleta.ms_reservas.Repository.ReservaRepository;
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
class ReservaServiceTest {

    @Mock private ReservaRepository repository;
    @Mock private UsuarioFeignClient usuarioFeignClient;
    @Mock private BicicletaFeignClient bicicletaFeignClient;
    @Mock private NotificacionFeign notificacionFeign;

    @InjectMocks
    private ReservaService service;

    private UsuarioDTO usuarioMock() {
        UsuarioDTO u = new UsuarioDTO();
        u.setId(1L);
        u.setEmail("juan@test.com");
        u.setEstado("ACTIVO");
        return u;
    }

    private BicicletaDTO bicicletaDisponibleMock() {
        BicicletaDTO b = new BicicletaDTO();
        b.setId(1L);
        b.setEstado("DISPONIBLE");
        return b;
    }

    private ReservaRequest requestMock() {
        ReservaRequest r = new ReservaRequest();
        r.setBicicletaId(1L);
        return r;
    }

    @Test
    void crearReserva_datosValidos_retornaReservaActiva() {
        when(usuarioFeignClient.obtenerPorEmail("juan@test.com")).thenReturn(usuarioMock());
        when(bicicletaFeignClient.obtenerBicicletaPorId(1L)).thenReturn(bicicletaDisponibleMock());
        when(repository.existsByUsuarioIdAndEstado(1L, "ACTIVA")).thenReturn(false);
        when(repository.existsByBicicletaIdAndEstado(1L, "ACTIVA")).thenReturn(false);
        Reserva guardada = new Reserva();
        guardada.setId(1L);
        guardada.setEstado("ACTIVA");
        when(repository.save(any())).thenReturn(guardada);

        Reserva result = service.crearReserva("juan@test.com", requestMock());

        assertEquals("ACTIVA", result.getEstado());
        verify(notificacionFeign).enviar(any(NotificationRequest.class));
    }

    @Test
    void crearReserva_bicicletaNoDisponible_lanzaExcepcion() {
        BicicletaDTO b = new BicicletaDTO();
        b.setEstado("OCUPADA");
        when(usuarioFeignClient.obtenerPorEmail("juan@test.com")).thenReturn(usuarioMock());
        when(bicicletaFeignClient.obtenerBicicletaPorId(1L)).thenReturn(b);
        assertThrows(RuntimeException.class,
                () -> service.crearReserva("juan@test.com", requestMock()));
    }

    @Test
    void crearReserva_usuarioYaTieneReservaActiva_lanzaExcepcion() {
        when(usuarioFeignClient.obtenerPorEmail("juan@test.com")).thenReturn(usuarioMock());
        when(bicicletaFeignClient.obtenerBicicletaPorId(1L)).thenReturn(bicicletaDisponibleMock());
        when(repository.existsByUsuarioIdAndEstado(1L, "ACTIVA")).thenReturn(true);
        assertThrows(RuntimeException.class,
                () -> service.crearReserva("juan@test.com", requestMock()));
    }

    @Test
    void crearReserva_bicicletaYaReservada_lanzaExcepcion() {
        when(usuarioFeignClient.obtenerPorEmail("juan@test.com")).thenReturn(usuarioMock());
        when(bicicletaFeignClient.obtenerBicicletaPorId(1L)).thenReturn(bicicletaDisponibleMock());
        when(repository.existsByUsuarioIdAndEstado(1L, "ACTIVA")).thenReturn(false);
        when(repository.existsByBicicletaIdAndEstado(1L, "ACTIVA")).thenReturn(true);
        assertThrows(RuntimeException.class,
                () -> service.crearReserva("juan@test.com", requestMock()));
    }

    @Test
    void listarReservas_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new Reserva()));
        assertEquals(1, service.listarReservas().size());
    }

    @Test
    void buscarPorId_existente_retornaReserva() {
        Reserva r = new Reserva();
        r.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(r));
        assertEquals(1L, service.buscarPorId(1L).getId());
    }

    @Test
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void cancelarReserva_cambiaEstadoCancelada() {
        Reserva r = new Reserva();
        r.setId(1L);
        r.setEstado("ACTIVA");
        when(repository.findById(1L)).thenReturn(Optional.of(r));
        when(repository.save(r)).thenReturn(r);
        Reserva result = service.cancelarReserva(1L);
        assertEquals("CANCELADA", result.getEstado());
    }

    @Test
    void completarReserva_activa_cambiaEstadoUtilizada() {
        Reserva r = new Reserva();
        r.setId(1L);
        r.setEstado("ACTIVA");
        when(repository.findById(1L)).thenReturn(Optional.of(r));
        when(repository.save(r)).thenReturn(r);
        Reserva result = service.completarReserva(1L);
        assertEquals("UTILIZADA", result.getEstado());
    }

    @Test
    void completarReserva_noActiva_lanzaExcepcion() {
        Reserva r = new Reserva();
        r.setEstado("CANCELADA");
        when(repository.findById(1L)).thenReturn(Optional.of(r));
        assertThrows(RuntimeException.class, () -> service.completarReserva(1L));
    }

    @Test
    void buscarReservaActiva_encontrada_retornaReserva() {
        Reserva r = new Reserva();
        r.setEstado("ACTIVA");
        when(repository.findByUsuarioIdAndBicicletaIdAndEstado(1L, 1L, "ACTIVA"))
                .thenReturn(Optional.of(r));
        Reserva result = service.buscarReservaActiva(1L, 1L);
        assertEquals("ACTIVA", result.getEstado());
    }

    @Test
    void buscarReservaActiva_noEncontrada_lanzaExcepcion() {
        when(repository.findByUsuarioIdAndBicicletaIdAndEstado(1L, 1L, "ACTIVA"))
                .thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.buscarReservaActiva(1L, 1L));
    }
}
