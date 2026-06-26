package com.bicicletas.ms_user.Service;

import com.bicicletas.ms_user.DTO.UsuarioDTO;
import com.bicicletas.ms_user.Entity.Usuario;
import com.bicicletas.ms_user.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
        @Autowired
        private  PasswordEncoder passwordEncoder;

    @Autowired
    UsuarioRepository repository;

    public List<Usuario> getClients(){
        return repository.findAll();
    }

    public Optional<Usuario> getClient(Long id){
        return repository.findById(id);
    }

public Usuario creUsuario(Usuario usuario){

    if(repository.existsByEmail(usuario.getEmail())){

        throw new RuntimeException(
                "El email ya existe");

    }

    usuario.setPassword(

            passwordEncoder.encode(

                    usuario.getPassword()

            )

    );

    return repository.save(usuario);

        }

    public Usuario actualizarUsuario(
        Long id,
        Usuario usuarioActualizado){

    Usuario usuarioExistente =
            repository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuario no encontrado"));


    if(usuarioActualizado.getPassword()!=null &&
        !usuarioActualizado.getPassword().isBlank()){

    usuarioExistente.setPassword(

            passwordEncoder.encode(

                    usuarioActualizado.getPassword()

            )

        );

        }       

    usuarioExistente.setNombre(
            usuarioActualizado.getNombre());

    usuarioExistente.setEmail(
            usuarioActualizado.getEmail());

    usuarioExistente.setEstado(
            usuarioActualizado.getEstado());

    return repository.save(
            usuarioExistente);
}

    public void deleteClient(Long id){
        repository.deleteById(id);
    }

    public UsuarioDTO obtenerPorEmail(String email){

    Usuario usuario = repository.findByEmail(email)

            .orElseThrow(() ->

                    new RuntimeException(

                            "Usuario no encontrado"));

    return new UsuarioDTO(

            usuario.getId(),

            usuario.getNombre(),

            usuario.getEmail(),

            usuario.getPassword(),

            usuario.getEstado()

    );

}

}
