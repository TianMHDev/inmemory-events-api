package com.example.inmemory_events_api.infraestructura.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para crear/actualizar venues en la API REST.
 * Contiene validaciones de Jakarta para la capa web.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequestDTO {

    @NotBlank(message = "El nombre del venue no puede estar vacío")
    private String name;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    @NotBlank(message = "La ciudad no puede estar vacía")
    private String city;

    private Integer capacity;
}
