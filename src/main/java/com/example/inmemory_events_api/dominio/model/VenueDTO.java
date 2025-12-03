package com.example.inmemory_events_api.dominio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio para Venue.
 * Este modelo NO tiene dependencias de frameworks (JPA, Spring, etc.)
 * Solo contiene lógica de negocio pura.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private Integer capacity;

    /**
     * Valida que el venue tenga los datos mínimos requeridos
     */
    public boolean isValid() {
        return name != null && !name.isBlank();
    }
}