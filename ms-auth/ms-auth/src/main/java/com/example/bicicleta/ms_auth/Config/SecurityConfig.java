package com.example.bicicleta.ms_auth.Config;

import com.example.bicicleta.ms_auth.Security.CustomUserDetailsService;
import com.example.bicicleta.ms_auth.Security.JwtAuthenticationEntryPoint;
import com.example.bicicleta.ms_auth.Security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    private final JwtAuthenticationEntryPoint entryPoint;

    private final CustomUserDetailsService userDetailsService;

    /**
     * Configuración principal de Spring Security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->

                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception ->

                        exception.authenticationEntryPoint(
                                entryPoint))

                .authenticationProvider(
                        authenticationProvider())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(

                                "/api/auth/**",

                                "/swagger-ui/**",

                                "/swagger-ui.html",

                                "/v3/api-docs/**",

                                "/swagger-resources/**",

                                "/webjars/**"

                        )

                        .permitAll()

                        .anyRequest()

                        .authenticated()

                )

                .addFilterBefore(

                        jwtFilter,

                        UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    /**
     * BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    /**
     * AuthenticationProvider
     */
@Bean
public AuthenticationProvider authenticationProvider() {

    DaoAuthenticationProvider provider =
            new DaoAuthenticationProvider();

    provider.setUserDetailsService(userDetailsService);

    provider.setPasswordEncoder(passwordEncoder());

    return provider;
}

    /**
     * AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();

    }

}