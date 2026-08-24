package com.hytz.habitos_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer; // <-- NUEVA IMPORTACIÓN
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // <-- NUEVA IMPORTACIÓN
import org.springframework.web.cors.CorsConfigurationSource; // <-- NUEVA IMPORTACIÓN
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // <-- NUEVA IMPORTACIÓN
import java.util.Arrays; // <-- NUEVA IMPORTACIÓN

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                // Configuramos las reglas en el orden correcto
                .authorizeHttpRequests(auth -> auth
                        // 1. Primero las rutas públicas (Login, Registro y la ruta de errores)
                        .requestMatchers("/api/auth/**", "/error").permitAll()

                        // 2. Al final, cualquier otra petición exige autenticación
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // NUEVO: Aquí definimos las reglas de quién tiene permiso de entrar
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Le damos permiso exclusivo a tu frontend de Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // Permitimos los métodos HTTP necesarios
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permitimos que pasen encabezados clave (el token viajará en el 'Authorization')
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicamos estas reglas a TODAS las rutas de nuestra API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}