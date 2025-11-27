package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA para Venue (Lugar/Recinto).
 * Relación OneToMany con Event: Un Venue puede tener múltiples eventos.
 * 
 * Configuraciones importantes:
 * - mappedBy: Indica que Event es el dueño de la relación (tiene la FK
 * venue_id)
 * - cascade: Propaga operaciones (persist, merge, remove, etc.) a los eventos
 * - orphanRemoval: Elimina automáticamente eventos que ya no están asociados al
 * venue
 * - fetch: LAZY para evitar cargar todos los eventos cuando solo necesitamos el
 * venue
 */
@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del venue no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Column(nullable = false)
    private String address;

    @Positive(message = "La capacidad debe ser un número positivo")
    @Column(nullable = false)
    private int capacity;

    /**
     * Relación OneToMany con Event.
     * - mappedBy: "venue" indica que EventEntity.venue es el dueño de la relación
     * - cascade: ALL propaga todas las operaciones (persist, merge, remove,
     * refresh, detach)
     * - orphanRemoval: true elimina eventos que se remueven de la colección
     * - fetch: LAZY (por defecto en OneToMany) - solo carga eventos cuando se
     * accede a ellos
     */
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Evita serialización circular en JSON
    private List<EventEntity> events = new ArrayList<>();

    // ========== Métodos de conveniencia para manejar la relación bidireccional
    // ==========

    /**
     * Agrega un evento al venue manteniendo la consistencia bidireccional.
     * Este método asegura que ambos lados de la relación estén sincronizados.
     */
    public void addEvent(EventEntity event) {
        events.add(event);
        event.setVenue(this);
    }

    /**
     * Remueve un evento del venue manteniendo la consistencia bidireccional.
     * Gracias a orphanRemoval=true, el evento será eliminado de la BD
     * automáticamente.
     */
    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setVenue(null);
    }
}
