package com.hytz.habitos_backend.services;

import com.hytz.habitos_backend.exception.ResourceNotFoundException;
import com.hytz.habitos_backend.models.Habito;
import com.hytz.habitos_backend.repositories.HabitoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final HabitoRepository habitoRepository;

    public Notificacion crearNotificacion(Long habitoId) {
        Habito habito = habitoRepository.findById(habitoId)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito no encontrado con ID: " + habitoId));

        Notificacion notificacion = new Notificacion();
        notificacion.setHabito(habito);
        notificacion.setCompletado(false);

        return notificacionRepository.save(notificacion);
    }

    public Optional<Notificacion> buscarPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    public void eliminarNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }
}