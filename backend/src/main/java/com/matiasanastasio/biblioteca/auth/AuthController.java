package com.matiasanastasio.biblioteca.auth;

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


    public AuthController(AuthenticationManager authManager, JwtService jwtService){
        this.authManager=authManager;
        this.jwtService=jwtService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req){
        Authentication auth= authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email(),req.password())
        );

        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtService.generarToken(user);
        return new TokenResponse(token);
    }
}
