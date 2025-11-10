package com.example.inmemory_events_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "events")
public class EventDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título del evento no puede estar vacío")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @Future(message = "La fecha del evento debe ser una fecha futura")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @NotNull(message = "El evento debe estar asociado a un venue")
    private VenueDTO venue;
}
