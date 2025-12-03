package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA optimizada para Event (Evento).
 * 
 * Optimizaciones implementadas:
 * - @BatchSize para reducir N+1 queries
 * - FetchType.LAZY en ManyToOne y ManyToMany
 * - Índices en columnas frecuentemente consultadas
 * - Enum para estado del evento
 */
@Entity
@Table(name = "events", indexes = {
        @Index(name = "idx_event_date", columnList = "date"),
        @Index(name = "idx_event_status", columnList = "status"),
        @Index(name = "idx_event_venue", columnList = "venue_id"),
        @Index(name = "idx_event_category", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Event.withVenue", attributeNodes = @NamedAttributeNode("venue")),
        @NamedEntityGraph(name = "Event.withCategories", attributeNodes = @NamedAttributeNode("categories")),
        @NamedEntityGraph(name = "Event.full", attributeNodes = {
                @NamedAttributeNode("venue"),
                @NamedAttributeNode("categories")
        })
})
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Column(nullable = false, length = 1000)
    private String description;

    @NotNull(message = "La fecha no puede estar vacía")
    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 100)
    private String category;

    /**
     * Estado del evento: ACTIVO, CANCELADO, FINALIZADO, POSPUESTO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EventStatus status = EventStatus.ACTIVO;

    /**
     * Relación ManyToOne con Venue.
     * - fetch: LAZY evita cargar el venue automáticamente
     * - optional: false indica que venue_id es obligatorio
     * - BatchSize: Optimiza la carga cuando se accede a múltiples eventos
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    @NotNull(message = "El evento debe estar asociado a un venue")
    private VenueEntity venue;

    /**
     * Relación ManyToMany con Category.
     * - cascade: PERSIST y MERGE para crear/actualizar categorías
     * - fetch: LAZY para cargar solo cuando se necesiten
     * - BatchSize: Optimiza la carga de categorías
     */
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "event_categories", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @BatchSize(size = 10)
    @Builder.Default
    private List<CategoryEntity> categories = new ArrayList<>();

    // ========== Métodos helper ==========

    public void addCategory(CategoryEntity category) {
        categories.add(category);
        category.getEvents().add(this);
    }

    public void removeCategory(CategoryEntity category) {
        categories.remove(category);
        category.getEvents().remove(this);
    }

    /**
     * Verifica si el evento está activo
     */
    public boolean isActive() {
        return status == EventStatus.ACTIVO;
    }

    /**
     * Cancela el evento
     */
    public void cancel() {
        this.status = EventStatus.CANCELADO;
    }

    /**
     * Marca el evento como finalizado
     */
    public void finish() {
        this.status = EventStatus.FINALIZADO;
    }
}
