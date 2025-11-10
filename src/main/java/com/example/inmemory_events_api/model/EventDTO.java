package com.example.inmemory_events_api.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EventDTO {

    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, message = "El título debe tener al menos 3 caracteres")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @Future(message = "La fecha debe ser en el futuro")
    private LocalDate date;

    @NotBlank(message = "La categoría no puede estar vacía")
    private String category;

    @NotBlank(message = "La ciudad no puede estar vacía")
    private String city;

    private Long venueId;
}
