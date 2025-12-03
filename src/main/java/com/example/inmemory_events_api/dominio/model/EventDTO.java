package com.example.inmemory_events_api.dominio.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio para Event.
 * Este modelo NO tiene dependencias de frameworks (JPA, Spring, etc.)
 * Solo contiene lógica de negocio pura.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;
    private String name;
    private Long venueId;
    private LocalDate date;

    /**
     * Valida que el evento tenga los datos mínimos requeridos
     */
    public boolean isValid() {
        return name != null && !name.isBlank()
                && venueId != null
                && date != null;
    }
}
