package com.matiasanastasio.biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.matiasanastasio.biblioteca.security.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // Públicos
                        .requestMatchers(HttpMethod.GET, "/api/libros/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/autores/**").permitAll()

                        // Crear usuario público
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        // Admin-only usuarios
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/*/rol").hasRole("ADMIN")

                        // Admin-only para modificar libros
                        .requestMatchers(HttpMethod.POST, "/api/libros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/libros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/libros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/libros/**").hasRole("ADMIN")

                        // Préstamos requiere login
                        .requestMatchers("/api/prestamos/**").authenticated()

                        // Usuarios requiere login (para todo lo demás)
                        .requestMatchers("/api/usuarios/**").authenticated()

                        .anyRequest().authenticated())
                .formLogin(form -> form.disable());

        // JWT filter (reemplaza httpBasic)
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
