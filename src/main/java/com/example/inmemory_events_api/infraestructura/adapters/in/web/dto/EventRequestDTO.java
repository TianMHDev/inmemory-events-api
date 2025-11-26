package com.example.inmemory_events_api.infraestructura.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para crear/actualizar eventos en la API REST.
 * Contiene validaciones de Jakarta para la capa web.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {

    @NotBlank(message = "El nombre del evento no puede estar vacío")
    private String name;

    @NotNull(message = "El venueId es obligatorio")
    private Long venueId;

    @NotBlank(message = "La fecha no puede estar vacía")
    private String date;
}
