package com.example.inmemory_events_api.dominio.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO {
    private Long id;

    @NotBlank(message = "El nombre del venue no puede estar vacío")
    private String name;

    private String location;
}