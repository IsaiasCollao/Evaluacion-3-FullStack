package com.example.bicicleta.ms_pago.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bicicleta.ms_pago.DTO.CalculoRequest;
import com.example.bicicleta.ms_pago.Entity.Pago;
import com.example.bicicleta.ms_pago.Service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(
        name = "Pagos",
        description = "Gestión de cobros y pagos asociados a préstamos de bicicletas"
)
public class PagoController {
        @Autowired
        private  PagoService service;

        @Operation(
        summary = "Calcular pago",
        description = """
                Calcula el monto asociado a un préstamo finalizado.

                Reglas:
                • Tarifa base.
                • Cobro adicional por tiempo excedido.
                • Genera el pago en estado PENDIENTE.
                • Envía una notificación.
                """)
@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Pago generado correctamente"),

        @ApiResponse(
                responseCode = "400",
                description = "Ya existe un pago asociado al préstamo")

})

    @PostMapping("/calcular")
    public ResponseEntity<Pago> calcularPago(
            @RequestBody CalculoRequest request){

        return ResponseEntity.ok(
                service.calcularPago(request));
    }

    @Operation(
        summary = "Registrar pago",
        description = """
                Cambia el estado del pago a PAGADO y registra la fecha del pago.
                Además envía una notificación al usuario.
                """)
@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Pago realizado correctamente"),

        @ApiResponse(
                responseCode = "400",
                description = "El pago ya fue realizado"),

        @ApiResponse(
                responseCode = "404",
                description = "Pago no encontrado")

})

    @PostMapping("/pagar/{id}")
    public ResponseEntity<Pago> pagar(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.pagar(id));
    }
    @Operation(
        summary = "Listar pagos de un usuario",
        description = "Obtiene todos los pagos asociados a un usuario")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pagos del usuario")
    })
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Pago>> obtenerPagosUsuario(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.obtenerPagosUsuario(id));
    }

    @Operation(
        summary = "Buscar pago por ID",
        description = "Obtiene el detalle de un pago específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }
    
}
