package com.example.bicicleta.ms_pago.Service;

import com.example.bicicleta.ms_pago.DTO.CalculoRequest;
import com.example.bicicleta.ms_pago.DTO.DeudaResponse;
import com.example.bicicleta.ms_pago.DTO.NotificationRequest;
import com.example.bicicleta.ms_pago.DTO.PagoRequest;
import com.example.bicicleta.ms_pago.Entity.Pago;
import com.example.bicicleta.ms_pago.Feign.NotificacionFeign;
import com.example.bicicleta.ms_pago.Repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository repository;

    private final NotificacionFeign notificationFeign;

    public Pago calcularPago(
            CalculoRequest request) {

        boolean existePago =
                repository.existsByPrestamoId(
                        request.getPrestamoId());

        if (existePago) {

            throw new RuntimeException(
                    "Ya existe un pago asociado a este préstamo");
        }

        double monto = 1000;

        if (request.getMinutosUso() > 30) {

            long bloquesExtra =
                    (request.getMinutosUso() - 30) / 30;

            monto += bloquesExtra * 500;
        }

        Pago pago = Pago.builder()
                .prestamoId(
                        request.getPrestamoId())
                .usuarioId(
                        request.getUsuarioId())
                .monto(monto)
                .estado("PENDIENTE")
                .build();

        Pago pagoGuardado =
                repository.save(pago);

        notificationFeign.enviar(
                new NotificationRequest(
                        pagoGuardado.getUsuarioId(),
                        "PAGO_GENERADO",
                        "Pago pendiente",
                        "Se ha generado un cobro asociado a su préstamo"
                )
        );

        return pagoGuardado;
    }

    public Pago pagar(Long id) {

        Pago pago =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Pago no encontrado"));

        if ("PAGADO".equalsIgnoreCase(
                pago.getEstado())) {

            throw new RuntimeException(
                    "El pago ya fue realizado");
        }

        pago.setEstado("PAGADO");

        pago.setFechaPago(
                LocalDateTime.now());

        Pago pagoActualizado =
                repository.save(pago);

        notificationFeign.enviar(
                new NotificationRequest(
                        pagoActualizado.getUsuarioId(),
                        "PAGO_REALIZADO",
                        "Pago registrado",
                        "Su pago fue registrado correctamente"
                )
        );

        return pagoActualizado;
    }

    public Pago buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pago no encontrado"));
    }

    public List<Pago> obtenerPagosUsuario(
            Long usuarioId) {

        return repository.findByUsuarioId(
                usuarioId);
    }

    public List<Pago> listar() {

        return repository.findAll();
    }
}