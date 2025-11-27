package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA para Event (Evento).
 * 
 * Relaciones:
 * 1. ManyToOne con Venue: Muchos eventos pertenecen a un venue
 * 2. ManyToMany con Category: Un evento puede tener múltiples categorías
 * 
 * Configuraciones importantes:
 * - fetch: LAZY en ManyToOne para evitar cargar el venue innecesariamente
 * - optional: false indica que un evento DEBE tener un venue (FK NOT NULL)
 * - JoinTable: Define la tabla intermedia para la relación ManyToMany
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Column(nullable = false, length = 1000)
    private String description;

    @NotNull(message = "La fecha no puede estar vacía")
    @Future(message = "La fecha debe ser futura")
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Relación ManyToOne con Venue.
     * - fetch: LAZY evita cargar el venue automáticamente (mejor performance)
     * - optional: false indica que venue_id es obligatorio (NOT NULL)
     * - JoinColumn: Define el nombre de la columna FK en la tabla events
     * 
     * Ciclo de vida:
     * - NO usamos cascade aquí porque Venue es la entidad padre
     * - Si eliminamos un Event, el Venue NO se elimina
     * - Si eliminamos un Venue, sus Events SÍ se eliminan (cascade en VenueEntity)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    @NotNull(message = "El evento debe estar asociado a un venue")
    private VenueEntity venue;

    /**
     * Relación ManyToMany con Category.
     * - Un evento puede tener múltiples categorías
     * - Una categoría puede estar en múltiples eventos
     * - cascade: PERSIST y MERGE para crear/actualizar categorías automáticamente
     * - fetch: LAZY para cargar categorías solo cuando se necesiten
     * - JoinTable: Define la tabla intermedia event_categories
     */
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "event_categories", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryEntity> categories = new ArrayList<>();

    // ========== Métodos de conveniencia para manejar relaciones ==========

    /**
     * Agrega una categoría al evento manteniendo la consistencia bidireccional.
     */
    public void addCategory(CategoryEntity category) {
        categories.add(category);
        category.getEvents().add(this);
    }

    /**
     * Remueve una categoría del evento manteniendo la consistencia bidireccional.
     */
    public void removeCategory(CategoryEntity category) {
        categories.remove(category);
        category.getEvents().remove(this);
    }
}
