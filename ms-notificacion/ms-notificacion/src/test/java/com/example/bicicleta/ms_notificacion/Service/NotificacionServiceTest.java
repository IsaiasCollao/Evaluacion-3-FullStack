package com.example.bicicleta.ms_notificacion.Service;

import com.example.bicicleta.ms_notificacion.DTO.NotificacionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @InjectMocks
    private NotificacionService service;

    @Test
    void enviar_requestValido_noLanzaExcepcion() {
        NotificacionRequest request = new NotificacionRequest(
                1L,
                "RESERVA_CREADA",
                "Reserva creada",
                "Su reserva fue registrada correctamente"
        );
        assertDoesNotThrow(() -> service.enviar(request));
    }

    @Test
    void enviar_distintosTipos_noLanzaExcepcion() {
        String[] tipos = {
                "RESERVA_CREADA",
                "PRESTAMO_INICIADO",
                "PRESTAMO_FINALIZADO",
                "PAGO_GENERADO",
                "PAGO_REALIZADO"
        };
        for (String tipo : tipos) {
            NotificacionRequest request = new NotificacionRequest(
                    1L, tipo, "Asunto", "Mensaje de prueba"
            );
            assertDoesNotThrow(() -> service.enviar(request));
        }
    }
}
