package com.hytz.habitos_backend.controllers;

import com.hytz.habitos_backend.models.Habito;
import com.hytz.habitos_backend.models.Usuario;
import com.hytz.habitos_backend.services.HabitoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitos")
@RequiredArgsConstructor
public class HabitoController {
    private final HabitoService habitoService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Habito> crearHabito(@PathVariable Long usuarioId, @Valid @RequestBody Habito habito) {
        Habito nuevoHabito = habitoService.crearHabito(usuarioId, habito);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHabito);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Habito>> obtenerHabitos(@PathVariable Long usuarioId) {
        List<Habito> habitos = habitoService.obtenerHabitosPorUsuario(usuarioId);
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
