package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity;

/**
 * Enum para el estado de un evento.
 * Permite filtrar eventos por su estado actual.
 */
public enum EventStatus {
    ACTIVO("Activo"),
    CANCELADO("Cancelado"),
    FINALIZADO("Finalizado"),
    POSPUESTO("Pospuesto");

    private final String displayName;

    EventStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
