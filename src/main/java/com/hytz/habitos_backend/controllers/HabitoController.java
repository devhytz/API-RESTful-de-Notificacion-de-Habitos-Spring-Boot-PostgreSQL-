package com.hytz.habitos_backend.controllers;

import com.hytz.habitos_backend.models.Habito;
import com.hytz.habitos_backend.models.Usuario;
import com.hytz.habitos_backend.repositories.UsuarioRepository;
import com.hytz.habitos_backend.services.HabitoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitos")
@RequiredArgsConstructor
public class HabitoController {

    private final HabitoService habitoService;
    private final UsuarioRepository usuarioRepository; // Lo inyectamos para buscar tu ID

    // Fíjate que aquí ya no dice "/usuario/{usuarioId}"
    @PostMapping
    public ResponseEntity<Habito> crearHabito(@Valid @RequestBody Habito habito, Authentication authentication) {
        String email = authentication.getName(); // Saca el email del Token
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        Habito nuevoHabito = habitoService.crearHabito(usuario.getId(), habito);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHabito);
    }

    // Aquí tampoco dice "/usuario/{usuarioId}"
    @GetMapping
    public ResponseEntity<List<Habito>> obtenerHabitos(Authentication authentication) {
        String email = authentication.getName(); // Saca el email del Token
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        List<Habito> habitos = habitoService.obtenerHabitosPorUsuario(usuario.getId());
        return ResponseEntity.ok(habitos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habito> obtenerHabitoPorId(@PathVariable Long id) {
        Habito habito = habitoService.buscarPorId(id);
        return ResponseEntity.ok(habito);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHabito(@PathVariable Long id) {
        habitoService.buscarPorId(id);
        habitoService.eliminarHabito(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habito> actualizarHabito(@PathVariable Long id, @Valid @RequestBody Habito habito) {
        try {
            Habito habitoActualizado = habitoService.actualizarHabito(id, habito);
            return ResponseEntity.ok(habitoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}