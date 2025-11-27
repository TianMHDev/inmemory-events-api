package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para CategoryEntity.
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Busca una categoría por su nombre (útil para evitar duplicados).
     */
    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    /**
     * Verifica si existe una categoría con el nombre dado.
     */
    boolean existsByNameIgnoreCase(String name);
}
