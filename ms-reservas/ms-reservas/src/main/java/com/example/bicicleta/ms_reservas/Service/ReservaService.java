package com.example.bicicleta.ms_reservas.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bicicleta.ms_reservas.DTO.BicicletaDTO;
import com.example.bicicleta.ms_reservas.DTO.NotificationRequest;
import com.example.bicicleta.ms_reservas.DTO.ReservaRequest;
import com.example.bicicleta.ms_reservas.DTO.UsuarioDTO;
import com.example.bicicleta.ms_reservas.Entity.Reserva;
import com.example.bicicleta.ms_reservas.Feign.BicicletaFeignClient;
import com.example.bicicleta.ms_reservas.Feign.NotificacionFeign;
import com.example.bicicleta.ms_reservas.Feign.UsuarioFeignClient;
import com.example.bicicleta.ms_reservas.Repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {

    public static final String DISPONIBLE = "DISPONIBLE";

    private final ReservaRepository repository;

    private final UsuarioFeignClient usuarioFeignClient;

    private final BicicletaFeignClient bicicletaFeignClient;

    private final NotificacionFeign notificationFeign;

    public Reserva crearReserva(
            String email,
            ReservaRequest request) {

        UsuarioDTO usuario =
                usuarioFeignClient.obtenerPorEmail(email);

        if (usuario == null) {

            throw new RuntimeException(
                    "Usuario no encontrado");

        }

        BicicletaDTO bicicleta =
                bicicletaFeignClient.obtenerBicicletaPorId(
                        request.getBicicletaId());

        if (bicicleta == null) {

            throw new RuntimeException(
                    "Bicicleta no encontrada");

        }

        if (!DISPONIBLE.equalsIgnoreCase(
                bicicleta.getEstado())) {

            throw new RuntimeException(
                    "Bicicleta no disponible");

        }

        boolean reservaActiva =
                repository.existsByUsuarioIdAndEstado(
                        usuario.getId(),
                        "ACTIVA");

        if (reservaActiva) {

            throw new RuntimeException(
                    "Usuario ya posee una reserva activa");

        }

        boolean bicicletaReservada =
                repository.existsByBicicletaIdAndEstado(
                        request.getBicicletaId(),
                        "ACTIVA");

        if (bicicletaReservada) {

            throw new RuntimeException(
                    "La bicicleta ya está reservada");

        }

        Reserva reserva = new Reserva();

        reserva.setUsuarioId(
                usuario.getId());

        reserva.setBicicletaId(
                request.getBicicletaId());

        reserva.setEstado("ACTIVA");

        reserva.setFechaReserva(
                LocalDate.now());

        Reserva reservaGuardada =
                repository.save(reserva);

        notificationFeign.enviar(

                new NotificationRequest(

                        usuario.getId(),

                        "RESERVA_CREADA",

                        "Reserva creada",

                        "Su reserva fue registrada correctamente"

                )

        );

        return reservaGuardada;

    }

    public List<Reserva> listarReservas() {

        return repository.findAll();

    }

    public Reserva buscarPorId(Long id) {

        return repository.findById(id)

                .orElseThrow(() ->

                        new RuntimeException(

                                "Reserva no encontrada"));

    }

    public Reserva cancelarReserva(Long id) {

        Reserva reserva = buscarPorId(id);

        reserva.setEstado("CANCELADA");

        return repository.save(reserva);

    }

    public Reserva completarReserva(Long id) {

        Reserva reserva = buscarPorId(id);

        if (!"ACTIVA".equalsIgnoreCase(
                reserva.getEstado())) {

            throw new RuntimeException(
                    "La reserva no se encuentra activa");

        }

        reserva.setEstado("UTILIZADA");

        return repository.save(reserva);

    }

    public Reserva buscarReservaActiva(
        Long usuarioId,
        Long bicicletaId){

    return repository

            .findByUsuarioIdAndBicicletaIdAndEstado(

                    usuarioId,

                    bicicletaId,

                    "ACTIVA")

            .orElseThrow(() ->

                    new RuntimeException(

                            "No existe una reserva activa para esta bicicleta"));

}

}