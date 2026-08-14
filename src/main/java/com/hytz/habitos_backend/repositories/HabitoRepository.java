package com.hytz.habitos_backend.repositories;

import com.hytz.habitos_backend.models.Habito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitoRepository extends JpaRepository<Habito, Long> {
    List<Habito> findByUsuarioId(Long usuarioId);
}
