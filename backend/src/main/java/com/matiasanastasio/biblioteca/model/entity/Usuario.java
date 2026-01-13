package com.matiasanastasio.biblioteca.model.entity;

import com.matiasanastasio.biblioteca.dto.UsuarioResponse;
import com.matiasanastasio.biblioteca.model.enums.RolUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable=false)
    private String nombre;

    @NotBlank
    @Column(nullable=false,unique = true)
    private String email;

    @NotBlank
    private String contrasena;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private RolUsuario rol;

    protected Usuario() {
    }
    public Usuario(String nombre, String email, String contrasena, RolUsuario rol) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
    }
    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getEmail() {
        return email;
    }
    public String getContrasena() {
        return contrasena;
    }
    public RolUsuario getRol() {
        return rol;
    }
    public void cambiarRol(RolUsuario nuevoRol) {
        if(nuevoRol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        this.rol = nuevoRol;
    }

    public UsuarioResponse toUsuarioResponse() {
        return new UsuarioResponse(id, nombre, email, rol);
    }

}
