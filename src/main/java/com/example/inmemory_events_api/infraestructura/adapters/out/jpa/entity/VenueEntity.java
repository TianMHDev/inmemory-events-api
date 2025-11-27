package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA optimizada para Venue (Lugar/Recinto).
 * 
 * Optimizaciones implementadas:
 * - @BatchSize para reducir N+1 queries al cargar múltiples venues
 * - FetchType.LAZY en OneToMany para evitar carga innecesaria
 * - orphanRemoval para gestión automática del ciclo de vida
 * - Índices en columnas frecuentemente consultadas
 */
@Entity
@Table(name = "venues", indexes = {
        @Index(name = "idx_venue_name", columnList = "name"),
        @Index(name = "idx_venue_city", columnList = "city"),
        @Index(name = "idx_venue_capacity", columnList = "capacity")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del venue no puede estar vacío")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Column(nullable = false, length = 200)
    private String address;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Column(nullable = false, length = 100)
    private String city;

    @Positive(message = "La capacidad debe ser un número positivo")
    @Column(nullable = false)
    private Integer capacity;

    /**
     * Relación OneToMany con Event.
     * - mappedBy: EventEntity.venue es el dueño de la relación
     * - cascade: ALL propaga todas las operaciones
     * - orphanRemoval: true elimina eventos huérfanos automáticamente
     * - fetch: LAZY evita N+1 queries
     * - BatchSize: Optimiza la carga de eventos cuando se accede a múltiples venues
     */
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JsonIgnore
    @Builder.Default
    private List<EventEntity> events = new ArrayList<>();

    // ========== Métodos helper bidireccionales ==========

    public void addEvent(EventEntity event) {
        events.add(event);
        event.setVenue(this);
    }

    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setVenue(null);
    }
}
