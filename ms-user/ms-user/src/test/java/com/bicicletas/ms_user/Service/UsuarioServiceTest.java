package com.bicicletas.ms_user.Service;

import com.bicicletas.ms_user.DTO.UsuarioDTO;
import com.bicicletas.ms_user.Entity.Usuario;
import com.bicicletas.ms_user.Repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuarioMock() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Juan Pérez");
        u.setEmail("juan@test.com");
        u.setPassword("$2a$10$hashed");
        u.setEstado("ACTIVO");
        return u;
    }

    @Test
    void getClients_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(usuarioMock()));
        List<Usuario> result = service.getClients();
        assertEquals(1, result.size());
    }

    @Test
    void getClient_idExistente_retornaOptional() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuarioMock()));
        assertTrue(service.getClient(1L).isPresent());
    }

    @Test
    void creUsuario_emailNuevo_hashPasswordYGuarda() {
        Usuario u = usuarioMock();
        u.setPassword("123456");
        when(repository.existsByEmail("juan@test.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$hashed");
        when(repository.save(u)).thenReturn(u);
        Usuario result = service.creUsuario(u);
        assertNotNull(result);
        verify(passwordEncoder).encode("123456");
        verify(repository).save(u);
    }

    @Test
    void creUsuario_emailDuplicado_lanzaExcepcion() {
        Usuario u = usuarioMock();
        when(repository.existsByEmail("juan@test.com")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> service.creUsuario(u));
        verify(repository, never()).save(any());
    }

    @Test
    void actualizarUsuario_existente_actualizaDatos() {
        Usuario existente = usuarioMock();
        Usuario datos = new Usuario();
        datos.setNombre("Pedro");
        datos.setEmail("pedro@test.com");
        datos.setEstado("ACTIVO");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);
        Usuario result = service.actualizarUsuario(1L, datos);
        assertNotNull(result);
        verify(repository).save(existente);
    }

    @Test
    void actualizarUsuario_conNuevaPassword_hashNuevaPassword() {
        Usuario existente = usuarioMock();
        Usuario datos = new Usuario();
        datos.setNombre("Juan");
        datos.setEmail("juan@test.com");
        datos.setEstado("ACTIVO");
        datos.setPassword("nueva123");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(passwordEncoder.encode("nueva123")).thenReturn("$2a$10$nuevohash");
        when(repository.save(any())).thenReturn(existente);
        service.actualizarUsuario(1L, datos);
        verify(passwordEncoder).encode("nueva123");
    }

    @Test
    void actualizarUsuario_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> service.actualizarUsuario(99L, new Usuario()));
    }

    @Test
    void obtenerPorEmail_existente_retornaDTOConPassword() {
        when(repository.findByEmail("juan@test.com")).thenReturn(Optional.of(usuarioMock()));
        UsuarioDTO dto = service.obtenerPorEmail("juan@test.com");
        assertEquals("juan@test.com", dto.getEmail());
        assertEquals("$2a$10$hashed", dto.getPassword());
        assertEquals("ACTIVO", dto.getEstado());
    }

    @Test
    void obtenerPorEmail_noExiste_lanzaExcepcion() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> service.obtenerPorEmail("noexiste@test.com"));
    }

    @Test
    void deleteClient_llamaDeleteById() {
        service.deleteClient(1L);
        verify(repository).deleteById(1L);
    }
}
