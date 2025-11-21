package com.example.inmemory_events_api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;

    @NotBlank(message = "El nombre del evento no puede estar vacío")
    private String name;

    @NotNull(message = "El venueId es obligatorio")
    private Long venueId;

    @NotBlank(message = "La fecha no puede estar vacía")
    private String date;
}
