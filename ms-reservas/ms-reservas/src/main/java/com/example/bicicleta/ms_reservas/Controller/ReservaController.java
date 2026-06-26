package com.example.bicicleta.ms_reservas.Controller;

import com.example.bicicleta.ms_reservas.DTO.ReservaRequest;
import com.example.bicicleta.ms_reservas.Entity.Reserva;
import com.example.bicicleta.ms_reservas.Service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/api/reservas")
@RestController
@Tag(
        name = "Reservas",
        description = "Operaciones relacionadas con reservas"
)
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @Operation(
            summary = "Listar reservas",
            description = "Obtiene todas las reservas registradas"
    )
    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {

        return ResponseEntity.ok(
                service.listarReservas());
    }

    @Operation(
            summary = "Buscar reserva por ID",
            description = "Obtiene una reserva específica"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @Operation(
            summary = "Crear reserva",
            description =
                    "Valida usuario existente, bicicleta disponible y ausencia de reservas activas"
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Reserva creada correctamente"),

        @ApiResponse(
                responseCode = "400",
                description = "El usuario ya posee una reserva activa"),

        @ApiResponse(
                responseCode = "400",
                description = "La bicicleta no está disponible"),

        @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado"),

        @ApiResponse(
                responseCode = "404",
                description = "Bicicleta no encontrada")

})

@PostMapping
public ResponseEntity<Reserva> crear(

        @RequestHeader("X-Authenticated-User")

        String email,

        @RequestBody ReservaRequest request){

    return ResponseEntity.ok(

            service.crearReserva(

                    email,

                    request));

}

    @Operation(
            summary = "Cancelar reserva",
            description =
                    "Cambia el estado de una reserva a CANCELADA"
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Reserva cancelada correctamente"),

        @ApiResponse(
                responseCode = "404",
                description = "Reserva no encontrada")

})
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.cancelarReserva(id));
    }

    @Operation(
            summary = "Marcar reserva utilizada",
            description =
                    "Cambia el estado de la reserva a UTILIZADA cuando inicia un préstamo"
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Reserva marcada como UTILIZADA"),

        @ApiResponse(
                responseCode = "404",
                description = "Reserva no encontrada")

})
    @PutMapping("/{id}/utilizar")
    public ResponseEntity<Reserva> utilizar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.completarReserva(id));
    }

    @Operation(
        summary = "Buscar reserva activa"
)
@GetMapping("/usuario/{usuarioId}/bicicleta/{bicicletaId}")
public ResponseEntity<Reserva> buscarReservaActiva(

        @PathVariable Long usuarioId,

        @PathVariable Long bicicletaId){

    return ResponseEntity.ok(

            service.buscarReservaActiva(

                    usuarioId,

                    bicicletaId));

}




}
