package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA para Category (Categoría).
 * Relación ManyToMany con Event: Una categoría puede estar en múltiples
 * eventos.
 * 
 * Configuraciones importantes:
 * - mappedBy: Indica que Event es el dueño de la relación (define la JoinTable)
 * - fetch: LAZY para evitar cargar todos los eventos cuando solo necesitamos la
 * categoría
 * - NO usamos cascade aquí para evitar eliminar eventos al borrar una categoría
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    /**
     * Relación ManyToMany con Event (lado inverso).
     * - mappedBy: "categories" indica que EventEntity.categories es el dueño
     * - fetch: LAZY para cargar eventos solo cuando se necesiten
     * - NO usamos cascade para evitar eliminar eventos al borrar categorías
     */
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @JsonIgnore // Evita serialización circular en JSON
    private List<EventEntity> events = new ArrayList<>();

    // Constructor de conveniencia
    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
