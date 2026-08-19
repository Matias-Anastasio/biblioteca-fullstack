package com.matiasanastasio.biblioteca.auth;

import com.matiasanastasio.biblioteca.exception.NotFoundException;
import com.matiasanastasio.biblioteca.model.entity.Usuario;
import com.matiasanastasio.biblioteca.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matiasanastasio.biblioteca.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;


    public AuthController(AuthenticationManager authManager, JwtService jwtService, UsuarioRepository usuarioRepository){
        this.authManager=authManager;
        this.jwtService=jwtService;
        this.usuarioRepository=usuarioRepository;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        String token = jwtService.generarToken(user, usuario.getId());
        return new TokenResponse(token);
    }
}
