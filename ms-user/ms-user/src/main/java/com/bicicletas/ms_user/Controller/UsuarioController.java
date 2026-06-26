package com.bicicletas.ms_user.Controller;

import com.bicicletas.ms_user.DTO.UsuarioDTO;
import com.bicicletas.ms_user.Entity.Usuario;
import com.bicicletas.ms_user.Service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/usuarios")
@RestController
@Tag(
        name = "Usuarios",
        description = "Operaciones relacionadas con usuarios"
)
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene todos los usuarios registrados"
    )

    @GetMapping
    public List<Usuario> listarTodos(){
        return service.getClients();
    }

    @Operation(
            summary = "Buscar usuario",
            description = "Obtiene un usuario mediante su ID"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado")
    })

    @GetMapping("/{id}")
    public Optional<Usuario> buscar_id(@PathVariable Long id){
        return service.getClient(id);
    }

        @Operation(
                summary = "Registrar usuario",
                description = "Registra un nuevo usuario en el sistema."
        )
        @ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Usuario registrado correctamente"),

        @ApiResponse(
                responseCode = "400",
                description = "El correo electrónico ya se encuentra registrado")

        })

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(
                @RequestBody Usuario usuario){

        return ResponseEntity.ok(
                    service.creUsuario(usuario));
        }

    

        @Operation(
                summary = "Actualizar usuario",
                description = "Actualiza la información de un usuario existente."
        )
        @ApiResponses(value = {

                @ApiResponse(
                        responseCode = "200",
                        description = "Usuario actualizado correctamente"),

                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado")

        })

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuario){

        return ResponseEntity.ok(
                service.actualizarUsuario(
                        id,
                        usuario));
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario por ID"
    )@ApiResponses(value = {

        @ApiResponse(
                responseCode = "200",
                description = "Usuario eliminado correctamente"),

        @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado")

})

    @DeleteMapping("/{id}")
    public void eliminar_cliente(@PathVariable Long id){
        service.deleteClient(id);
    }

    @Operation(
            summary = "Buscar usuario por email"
    )


        @GetMapping("/email/{email}")

        public ResponseEntity<UsuarioDTO>

        obtenerPorEmail(

        @PathVariable String email){

        return ResponseEntity.ok(

                service.obtenerPorEmail(email));

        }
}
