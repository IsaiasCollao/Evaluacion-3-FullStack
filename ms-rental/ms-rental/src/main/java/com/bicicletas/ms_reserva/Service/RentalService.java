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

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final PrestamoRepository repository;

    private final UsuarioFeign usuarioFeign;

    private final ReservaFeign reservaFeign;

    private final BicicletaFeign bicicletaFeign;

    private final PagoFeign pagoFeign;

    private final NotificacionesFeign notificationFeign;

    /**
     * Inicia un préstamo utilizando el usuario autenticado.
     */
    public Prestamo iniciarPrestamo(
            String email,
            RentalRequest request) {

        // Obtener usuario autenticado
        UsuarioDTO usuario =
                usuarioFeign.obtenerPorEmail(email);

        if (usuario == null) {

            throw new RuntimeException(
                    "Usuario no encontrado");

        }

        // Buscar reserva activa del usuario
        ReservaDTO reserva =
                reservaFeign.obtenerReservaActiva(

                        usuario.getId(),

                        request.getBicicletaId());

        if (reserva == null) {

            throw new RuntimeException(
                    "No existe una reserva activa");

        }

        if (!"ACTIVA".equalsIgnoreCase(
                reserva.getEstado())) {

            throw new RuntimeException(
                    "La reserva no se encuentra activa");

        }

        // Buscar bicicleta
        BicicletaDTO bicicleta =
                bicicletaFeign.obtenerBicicleta(
                        reserva.getBicicletaId());

        if (bicicleta == null) {

            throw new RuntimeException(
                    "Bicicleta no encontrada");

        }

        if (!"DISPONIBLE".equalsIgnoreCase(
                bicicleta.getEstado())) {

            throw new RuntimeException(
                    "La bicicleta no está disponible");

        }

        // Crear préstamo
        Prestamo prestamo = Prestamo.builder()

                .reservaId(reserva.getId())

                .usuarioId(usuario.getId())

                .bicicletaId(reserva.getBicicletaId())

                .fechaInicio(LocalDateTime.now())

                .estado("ACTIVO")

                .build();

        Prestamo guardado =
                repository.save(prestamo);

        // Cambiar bicicleta a ocupada
        bicicletaFeign.ocuparBicicleta(
                reserva.getBicicletaId());

        // Marcar reserva como utilizada
        reservaFeign.completarReserva(
                reserva.getId());

        // Notificar
        notificationFeign.enviar(

                new NotificationRequest(

                        usuario.getId(),

                        "PRESTAMO_INICIADO",

                        "Préstamo iniciado",

                        "La bicicleta fue entregada correctamente"

                )

        );

        return guardado;

    }

    /**
     * Finaliza un préstamo.
     */
    public Prestamo finalizarPrestamo(Long prestamoId) {

        Prestamo prestamo = repository.findById(prestamoId)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Préstamo no encontrado"));

        if (!"ACTIVO".equalsIgnoreCase(
                prestamo.getEstado())) {

            throw new RuntimeException(
                    "El préstamo ya fue finalizado");

        }

        prestamo.setFechaFin(
                LocalDateTime.now());

        prestamo.setEstado(
                "FINALIZADO");

        Prestamo actualizado =
                repository.save(prestamo);

        bicicletaFeign.liberarBicicleta(
                prestamo.getBicicletaId());

        long minutosUso =

                ChronoUnit.MINUTES.between(

                        actualizado.getFechaInicio(),

                        actualizado.getFechaFin());

        pagoFeign.calcularPago(

                new CalculoRequest(

                        actualizado.getId(),

                        actualizado.getUsuarioId(),

                        minutosUso));

        notificationFeign.enviar(

                new NotificationRequest(

                        actualizado.getUsuarioId(),

                        "PRESTAMO_FINALIZADO",

                        "Préstamo finalizado",

                        "Gracias por utilizar el servicio"));

        return actualizado;

    }

    public Prestamo buscarPorId(Long id) {

        return repository.findById(id)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Préstamo no encontrado"));

    }

    public List<Prestamo> listar() {

        return repository.findAll();

    }

}