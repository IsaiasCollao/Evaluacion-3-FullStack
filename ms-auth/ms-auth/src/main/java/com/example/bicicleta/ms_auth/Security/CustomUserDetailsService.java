package com.example.bicicleta.ms_auth.Security;

import com.example.bicicleta.ms_auth.DTO.UsuarioDTO;
import com.example.bicicleta.ms_auth.Feign.UsuarioFeign;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioFeign usuarioFeign;

    @Override
public UserDetails loadUserByUsername(String username) {

    System.out.println("Buscando usuario: " + username);

    UsuarioDTO usuario =
            usuarioFeign.obtenerPorEmail(username);

    System.out.println("Usuario encontrado: " + usuario);

    return new CustomUserDetails(

            usuario.getEmail(),

            usuario.getPassword()

    );

}
}