package com.example.bicicleta.ms_notificacion.Controller;

import com.example.bicicleta.ms_notificacion.DTO.NotificacionRequest;
import com.example.bicicleta.ms_notificacion.Service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificacion")
@RequiredArgsConstructor
@Tag(
        name = "Notificaciones",
        description = "Envío de notificaciones del sistema"
)
public class NotifacionController {

    private final NotificacionService service;

    @Operation(
        summary = "Enviar notificación",
        description = """
                Registra una notificación del sistema.

                Actualmente las notificaciones se muestran
                por consola mediante System.out.
                """)
@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Notificación registrada")

})

    @PostMapping("/enviar")
    public ResponseEntity<String> enviar(
            @RequestBody NotificacionRequest request){

        service.enviar(request);

        return ResponseEntity.ok(
                "Notificación registrada");
    }

}
