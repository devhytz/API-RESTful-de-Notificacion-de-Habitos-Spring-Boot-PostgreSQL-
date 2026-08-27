package com.hytz.habitos_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "habits_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;
    @NotNull
    private LocalDate executionDate;
    @NotBlank
    private String status;
}
