package com.example.inmemory_events_api.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VenueDTO {

    private Long id;

    @NotBlank(message = "El nombre del venue no puede estar vacío")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String name;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    @NotBlank(message = "La ciudad no puede estar vacía")
    private String city;

    @Min(value = 10, message = "La capacidad mínima debe ser 10 personas")
    private int capacity;
}
