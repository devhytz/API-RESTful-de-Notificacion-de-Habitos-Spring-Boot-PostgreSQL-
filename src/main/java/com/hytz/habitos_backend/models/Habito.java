package com.hytz.habitos_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "habitos")
@Getter
@Setter
public class Habito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;
    private String descripcion;
    private String estado;
    @NotNull(message = "El periodo es obligatorio")
    @Min(value = 1, message = "El periodo debe ser mayor o igual a 1")
    private int periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Habito() {}

}
