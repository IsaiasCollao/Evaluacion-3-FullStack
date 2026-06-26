package com.bicicletas.ms_reserva.Service;

import com.bicicletas.ms_reserva.Dto.BicicletaDTO;
import com.bicicletas.ms_reserva.Dto.CalculoRequest;
import com.bicicletas.ms_reserva.Dto.NotificationRequest;
import com.bicicletas.ms_reserva.Dto.RentalRequest;
import com.bicicletas.ms_reserva.Dto.ReservaDTO;
import com.bicicletas.ms_reserva.Dto.UsuarioDTO;
import com.bicicletas.ms_reserva.Entity.Prestamo;
import com.bicicletas.ms_reserva.Feign.BicicletaFeign;
import com.bicicletas.ms_reserva.Feign.NotificacionesFeign;
import com.bicicletas.ms_reserva.Feign.PagoFeign;
import com.bicicletas.ms_reserva.Feign.ReservaFeign;
import com.bicicletas.ms_reserva.Feign.UsuarioFeign;
import com.bicicletas.ms_reserva.Repository.PrestamoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock private PrestamoRepository repository;
    @Mock private UsuarioFeign usuarioFeign;
    @Mock private ReservaFeign reservaFeign;
    @Mock private BicicletaFeign bicicletaFeign;
    @Mock private PagoFeign pagoFeign;
    @Mock private NotificacionesFeign notificacionFeign;

    @InjectMocks
    private RentalService service;

    private UsuarioDTO usuarioMock() {
        UsuarioDTO u = new UsuarioDTO();
        u.setId(1L);
        u.setEmail("juan@test.com");
        return u;
    }

    private ReservaDTO reservaActivaMock() {
        ReservaDTO r = new ReservaDTO();
        r.setId(1L);
        r.setBicicletaId(1L);
        r.setEstado("ACTIVA");
        return r;
    }

    private BicicletaDTO bicicletaDisponibleMock() {
        BicicletaDTO b = new BicicletaDTO();
        b.setId(1L);
        b.setEstado("DISPONIBLE");
        return b;
    }

    private RentalRequest rentalRequestMock() {
        RentalRequest r = new RentalRequest();
        r.setBicicletaId(1L);
        return r;
    }

    @Test
    void iniciarPrestamo_datosValidos_creaPrestamoActivo() {
        when(usuarioFeign.obtenerPorEmail("juan@test.com")).thenReturn(usuarioMock());
        when(reservaFeign.obtenerReservaActiva(1L, 1L)).thenReturn(reservaActivaMock());
        when(bicicletaFeign.obtenerBicicleta(1L)).thenReturn(bicicletaDisponibleMock());
        Prestamo guardado = Prestamo.builder()
                .id(1L).usuarioId(1L).bicicletaId(1L).reservaId(1L)
                .fechaInicio(LocalDateTime.now()).estado("ACTIVO").build();
        when(repository.save(any())).thenReturn(guardado);

        Prestamo result = service.iniciarPrestamo("juan@test.com", rentalRequestMock());

        assertEquals("ACTIVO", result.getEstado());
        verify(bicicletaFeign).ocuparBicicleta(1L);
        verify(reservaFeign).completarReserva(1L);
        verify(notificacionFeign).enviar(any(NotificationRequest.class));
    }

    @Test
    void iniciarPrestamo_usuarioNoEncontrado_lanzaExcepcion() {
        when(usuarioFeign.obtenerPorEmail("juan@test.com")).thenReturn(null);
        assertThrows(RuntimeException.class,
                () -> service.iniciarPrestamo("juan@test.com", rentalRequestMock()));
    }

    @Test
    void iniciarPrestamo_sinReservaActiva_lanzaExcepcion() {
        when(usuarioFeign.obtenerPorEmail("juan@test.com")).thenReturn(usuarioMock());
        when(reservaFeign.obtenerReservaActiva(1L, 1L)).thenReturn(null);
        assertThrows(RuntimeException.class,
                () -> service.iniciarPrestamo("juan@test.com", rentalRequestMock()));
    }

    @Test
    void iniciarPrestamo_bicicletaNoDisponible_lanzaExcepcion() {
        BicicletaDTO b = new BicicletaDTO();
        b.setEstado("OCUPADA");
        when(usuarioFeign.obtenerPorEmail("juan@test.com")).thenReturn(usuarioMock());
        when(reservaFeign.obtenerReservaActiva(1L, 1L)).thenReturn(reservaActivaMock());
        when(bicicletaFeign.obtenerBicicleta(1L)).thenReturn(b);
        assertThrows(RuntimeException.class,
                () -> service.iniciarPrestamo("juan@test.com", rentalRequestMock()));
    }

    @Test
    void finalizarPrestamo_activo_cambiaAFinalizado() {
        LocalDateTime inicio = LocalDateTime.now().minusMinutes(45);
        Prestamo prestamo = Prestamo.builder()
                .id(1L).usuarioId(1L).bicicletaId(1L)
                .fechaInicio(inicio).estado("ACTIVO").build();
        Prestamo finalizado = Prestamo.builder()
                .id(1L).usuarioId(1L).bicicletaId(1L)
                .fechaInicio(inicio).fechaFin(LocalDateTime.now())
                .estado("FINALIZADO").build();
        when(repository.findById(1L)).thenReturn(Optional.of(prestamo));
        when(repository.save(any())).thenReturn(finalizado);

        Prestamo result = service.finalizarPrestamo(1L);

        assertEquals("FINALIZADO", result.getEstado());
        verify(bicicletaFeign).liberarBicicleta(1L);
        verify(pagoFeign).calcularPago(any(CalculoRequest.class));
        verify(notificacionFeign).enviar(any(NotificationRequest.class));
    }

    @Test
    void finalizarPrestamo_yaFinalizado_lanzaExcepcion() {
        Prestamo prestamo = Prestamo.builder().id(1L).estado("FINALIZADO").build();
        when(repository.findById(1L)).thenReturn(Optional.of(prestamo));
        assertThrows(RuntimeException.class, () -> service.finalizarPrestamo(1L));
    }

    @Test
    void finalizarPrestamo_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.finalizarPrestamo(99L));
    }

    @Test
    void buscarPorId_existente_retornaPrestamo() {
        Prestamo p = Prestamo.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        assertEquals(1L, service.buscarPorId(1L).getId());
    }

    @Test
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void listar_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(new Prestamo(), new Prestamo()));
        assertEquals(2, service.listar().size());
    }
}
