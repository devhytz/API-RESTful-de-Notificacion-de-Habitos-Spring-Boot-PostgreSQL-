package com.hytz.habitos_backend.repositories;

import com.hytz.habitos_backend.models.Habito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitoRepository extends JpaRepository<Habito, Long> {
}
