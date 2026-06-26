package com.bicicletas.ms_bicicleta.Controller;

import com.bicicletas.ms_bicicleta.Entity.Bicicleta;
import com.bicicletas.ms_bicicleta.Service.BicicletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bicicletas")
@Tag(
        name = "Bicicletas",
        description = "Operaciones relacionadas con bicicletas"
)
public class BicicletaController {

    @Autowired
    BicicletaService service;

    @Operation(
            summary = "Listar bicicletas",
            description = "Obtiene todas las bicicletas registradas"
    )

    @GetMapping
    public List<Bicicleta> listar_todas(){
        return service.getBicycles();
    }

    @Operation(
            summary = "Buscar bicicleta por ID",
            description = "Obtiene una bicicleta específica"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Bicicleta encontrada"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Bicicleta no encontrada")
    })

    @GetMapping("/{id}")
    public Optional<Bicicleta> mostrar_id (@PathVariable Long id){
        return service.getBicycle(id);
    }

    @Operation(
            summary = "Crear bicicleta",
            description = "Registra una nueva bicicleta"
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Bicicleta registrada correctamente"),

        @ApiResponse(
                responseCode = "400",
                description = "El código de la bicicleta ya existe")

        })

    @PostMapping
    public ResponseEntity<Bicicleta>
    crearBicicleta(
            @RequestBody Bicicleta bicicleta){

        return ResponseEntity.ok(
                service.crearBicicleta(
                        bicicleta));
    }

    @Operation(
            summary = "Actualizar bicicleta",
            description = "Actualiza la información de una bicicleta"
    )

    @PutMapping("/{id}")
    public ResponseEntity<Bicicleta>
    actualizarBicicleta(
            @PathVariable Long id,
            @RequestBody Bicicleta bicicleta){

        return ResponseEntity.ok(
                service.actualizarBicicleta(
                        id,
                        bicicleta));
    }

    @Operation(
            summary = "Eliminar bicicleta",
            description = "Elimina una bicicleta por ID"
    )

    @DeleteMapping("/{id}")
    public void eliminar_bicicleta(@PathVariable Long id){
        service.deleteBicycle(id);
    }

    @Operation(
            summary = "Ocupar bicicleta",
            description = "Cambia el estado de la bicicleta a OCUPADA"
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "La bicicleta quedó en estado OCUPADA"),

        @ApiResponse(
                responseCode = "404",
                description = "Bicicleta no encontrada")

})

    @PutMapping("/{id}/ocupar")
    public Bicicleta ocuparBicicleta(
        @PathVariable Long id){

        return service.ocuparBicicleta(id);
    }

    @Operation(
            summary = "Liberar bicicleta",
            description = "Cambia el estado de la bicicleta a DISPONIBLE"
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "La bicicleta quedó disponible"),

        @ApiResponse(
                responseCode = "404",
                description = "Bicicleta no encontrada")

})


    @PutMapping("/{id}/liberar")
    public Bicicleta liberarBicicleta(
            @PathVariable Long id){

        return service.liberarBicicleta(id);
    }



}
