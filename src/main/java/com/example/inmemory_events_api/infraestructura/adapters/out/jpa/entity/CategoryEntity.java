package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA optimizada para Category (Categoría).
 * 
 * Optimizaciones:
 * - @BatchSize para reducir N+1 queries
 * - FetchType.LAZY en ManyToMany
 * - Índice en nombre para búsquedas rápidas
 */
@Entity
@Table(name = "categories", indexes = {
        @Index(name = "idx_category_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 500)
    private String description;

    /**
     * Relación ManyToMany con Event (lado inverso).
     * - mappedBy: Event es el dueño de la relación
     * - fetch: LAZY para evitar N+1
     * - BatchSize: Optimiza la carga
     */
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JsonIgnore
    @Builder.Default
    private List<EventEntity> events = new ArrayList<>();
}
