package com.hytz.habitos_backend.services;

import com.hytz.habitos_backend.models.Habito;
import com.hytz.habitos_backend.models.Usuario;
import com.hytz.habitos_backend.repositories.HabitoRepository;
import com.hytz.habitos_backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabitoService {

    private final HabitoRepository habitoRepository;
    private final UsuarioRepository usuarioRepository;

    public Habito crearHabito(Long usuarioId, Habito habito) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("No se encontró el usuario con ID: " + usuarioId));

        habito.setUsuario(usuario);
        return habitoRepository.save(habito);
    }

    public List<Habito> obtenerHabitosPorUsuario(Long usuarioId) {
        return habitoRepository.findByUsuarioId(usuarioId);
    }

    public void eliminarHabito(Long id) {
        habitoRepository.deleteById(id);
    }

    public Optional<Habito> buscarPorId(Long id) {
        return habitoRepository.findById(id);
    }

}
