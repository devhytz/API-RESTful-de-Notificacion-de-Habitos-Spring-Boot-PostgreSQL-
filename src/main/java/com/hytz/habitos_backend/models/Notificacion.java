package com.hytz.habitos_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El mensaje de la notificación no puede estar vacío")
    private String mensaje;

    @NotNull(message = "La fecha y hora programada es obligatoria")
    @Column(name = "fecha_hora_programada")
    private LocalDateTime fechaHoraProgramada;

    // Inicializamos en false por defecto para que al crearse siempre esté pendiente
    private Boolean completado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habito_id", nullable = false)
    @JsonIgnore
    private Habito habito;

    public Notificacion() {}
}