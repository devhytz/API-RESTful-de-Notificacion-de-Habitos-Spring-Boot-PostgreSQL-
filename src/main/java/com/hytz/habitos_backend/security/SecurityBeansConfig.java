package com.hytz.habitos_backend.security;

import com.hytz.habitos_backend.repositories.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansConfig {

    private final UsuarioRepository usuarioRepository;

    public SecurityBeansConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Fíjate que aquí usamos "userDetailsService()" con "u" minúscula y los paréntesis ()
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // El gerente que usaremos luego para el Login
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // El algoritmo de encriptación que usaremos
    }
}
