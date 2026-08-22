package com.hytz.habitos_backend.services;

import com.hytz.habitos_backend.dtos.AuthResponse;
import com.hytz.habitos_backend.dtos.LoginRequest;
import com.hytz.habitos_backend.dtos.RegistroRequest;
import com.hytz.habitos_backend.models.Usuario;
import com.hytz.habitos_backend.repositories.UsuarioRepository;
import com.hytz.habitos_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Magia de Lombok: crea el constructor por nosotros
public class AuthService {

    // Ponemos "final" para que Lombok sepa que debe meterlos en el constructor
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegistroRequest request) {

        // 1. Creamos un nuevo usuario (entidad de base de datos)
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());

        // 2. ¡MUY IMPORTANTE! Encriptamos la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Guardamos el usuario en la base de datos
        usuarioRepository.save(usuario);

        // 4. Fabricamos su gafete (Token) pasándole el usuario a nuestra máquina
        String jwtToken = jwtService.generarToken(usuario);

        // 5. Se lo entregamos en el "sobre" de respuesta (AuthResponse)
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = jwtService.generarToken(usuario);
        return AuthResponse.builder().token(jwtToken).build();
    }
}