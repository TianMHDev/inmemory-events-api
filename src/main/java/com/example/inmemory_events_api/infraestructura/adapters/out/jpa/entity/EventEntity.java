package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @NotNull(message = "La fecha no puede estar vacía")
    private LocalDate date;


    @ManyToOne
    @JoinColumn(name = "venue_id") // Crea la FK en la tabla events
    private VenueEntity venue;
}
