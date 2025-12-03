package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para CategoryEntity.
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Busca categoría por nombre (case insensitive).
     */
    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    /**
     * Verifica si existe una categoría con el nombre dado.
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Busca categorías por nombre parcial.
     */
    @Query("SELECT c FROM CategoryEntity c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<CategoryEntity> findByNameContaining(@Param("name") String name);
}
