package com.hytz.habitos_backend.controllers;

import com.hytz.habitos_backend.models.Habito;
import com.hytz.habitos_backend.models.Usuario;
import com.hytz.habitos_backend.services.HabitoService;
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
    public ResponseEntity<Habito> crearHabito(@PathVariable Long usuarioId, @RequestBody Habito habito) {
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
        return habitoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHabito(@PathVariable Long id) {
        return habitoService.buscarPorId(id)
                .map(habito -> {
                    habitoService.eliminarHabito(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
