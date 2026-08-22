package com.hytz.habitos_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                // 1. Apagamos la protección CSRF (No la necesitamos porque usaremos Tokens)
                .csrf(csrf -> csrf.disable())

                // 2. Configuramos cuáles rutas son públicas y cuáles privadas
                .authorizeHttpRequests(auth -> auth
                        // Todos los endpoints que empiecen con /api/auth/ serán de acceso libre (Login y Registro)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Cualquier otra ruta obligatoriamente pedirá que el usuario esté autenticado
                        .anyRequest().authenticated()
                )

                // 3. Le decimos que NO guarde sesiones en memoria (STATELESS), cada petición debe traer su propio token
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Le asignamos nuestro detective (Provider)
                .authenticationProvider(authProvider)

                // 5. ¡AQUÍ ESTÁ LA MAGIA! Le decimos a Spring: "Pon a MI Guardia (jwtAuthFilter) antes que el tuyo"
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
