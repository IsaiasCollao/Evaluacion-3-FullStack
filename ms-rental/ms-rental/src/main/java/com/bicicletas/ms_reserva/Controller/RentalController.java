package com.bicicletas.ms_reserva.Controller;

import com.bicicletas.ms_reserva.Dto.RentalRequest;
import com.bicicletas.ms_reserva.Entity.Prestamo;
import com.bicicletas.ms_reserva.Service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Tag(
        name = "Préstamos",
        description = "Gestión de préstamos de bicicletas"
)
public class RentalController {

    @Autowired
    private  RentalService service;

    @Operation(
            summary = "Iniciar préstamo",
            description = """
                    Inicia un préstamo utilizando una reserva activa.

                    Validaciones:
                    - La reserva debe existir.
                    - La reserva debe estar ACTIVA.
                    - La bicicleta debe existir.
                    - La bicicleta cambia a estado OCUPADA.
                    - La reserva cambia a UTILIZADA.
                    - Se envía una notificación.
                    """
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Préstamo iniciado correctamente"),

        @ApiResponse(
                responseCode = "400",
                description = "La reserva no está activa"),

        @ApiResponse(
                responseCode = "404",
                description = "Reserva no encontrada"),

        @ApiResponse(
                responseCode = "404",
                description = "Bicicleta no encontrada")

})
@PostMapping("/iniciar")
public ResponseEntity<Prestamo> iniciar(

        @RequestHeader("X-Authenticated-User")
        String email,

        @RequestBody RentalRequest request){

    return ResponseEntity.ok(
            service.iniciarPrestamo(
                    email,
                    request));

}


    @Operation(
            summary = "Finalizar préstamo",
            description = """
                    Finaliza un préstamo.

                    Acciones:
                    - Cambia estado a FINALIZADO.
                    - Libera la bicicleta.
                    - Calcula el tiempo utilizado.
                    - Genera una deuda pendiente.
                    - Envía una notificación.
                    """
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Préstamo finalizado correctamente"),

        @ApiResponse(
                responseCode = "400",
                description = "El préstamo ya fue finalizado"),

        @ApiResponse(
                responseCode = "404",
                description = "Préstamo no encontrado")

})
    @PostMapping("/end/{prestamoId}")
    public ResponseEntity<Prestamo> finalizar(
            @PathVariable Long prestamoId){

        return ResponseEntity.ok(
                service.finalizarPrestamo(prestamoId));
    }

    @Operation(
            summary = "Buscar préstamo por ID"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Préstamo encontrado"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Préstamo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> buscar(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @Operation(
            summary = "Listar préstamos",
            description = "Obtiene todos los préstamos registrados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de préstamos")
    })
    @GetMapping
    public ResponseEntity<List<Prestamo>> listar(){

        return ResponseEntity.ok(
                service.listar());
    }
}
