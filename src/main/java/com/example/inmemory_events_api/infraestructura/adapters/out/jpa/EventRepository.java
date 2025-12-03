package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio optimizado para EventEntity.
 * 
 * Optimizaciones implementadas:
 * 1. JpaSpecificationExecutor: Permite usar Specifications para filtros
 * dinámicos
 * 2. @EntityGraph: Evita N+1 queries cargando relaciones en una sola query
 * 3. JPQL: Queries type-safe y optimizadas
 * 4. join fetch: Carga eager selectiva cuando es necesario
 * 
 * Comparación de estrategias:
 * - @EntityGraph: Mejor para queries simples con relaciones conocidas
 * - join fetch: Mejor para queries complejas con múltiples joins
 * - Specifications: Mejor para filtros dinámicos combinables
 */
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

        // ========== Queries con @EntityGraph (evita N+1) ==========

        /**
         * Busca evento por ID cargando el venue en la misma query.
         * Evita N+1: 1 query en lugar de 2 (evento + venue)
         */
        @EntityGraph(value = "Event.withVenue", type = EntityGraph.EntityGraphType.LOAD)
        Optional<EventEntity> findWithVenueById(Long id);

        /**
         * Busca evento por ID cargando venue y categorías.
         * Evita N+1: 1 query en lugar de 3 (evento + venue + categorías)
         */
        @EntityGraph(value = "Event.full", type = EntityGraph.EntityGraphType.LOAD)
        Optional<EventEntity> findFullById(Long id);

        /**
         * Lista todos los eventos con sus venues.
         * Evita N+1: 1 query en lugar de N+1 (1 para eventos + N para venues)
         */
        @Query("SELECT DISTINCT e FROM EventEntity e LEFT JOIN FETCH e.venue")
        List<EventEntity> findAllWithVenue();

        // ========== Queries JPQL optimizadas ==========

        /**
         * Busca eventos por venue usando JPQL.
         * join fetch carga el venue en la misma query.
         */
        @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue v WHERE v.id = :venueId")
        List<EventEntity> findByVenueIdWithVenue(@Param("venueId") Long venueId);

        /**
         * Busca eventos por nombre de venue.
         * join fetch evita N+1 al cargar el venue.
         */
        @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :venueName, '%'))")
        List<EventEntity> findByVenueNameContaining(@Param("venueName") String venueName);

        /**
         * Busca eventos en un rango de fechas.
         * Optimizado con índice en la columna date.
         */
        @Query("SELECT e FROM EventEntity e WHERE e.date BETWEEN :startDate AND :endDate ORDER BY e.date")
        List<EventEntity> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        /**
         * Busca eventos en un rango de fechas con venue.
         * join fetch evita N+1.
         */
        @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.date BETWEEN :startDate AND :endDate ORDER BY e.date")
        List<EventEntity> findByDateRangeWithVenue(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        /**
         * Busca eventos por estado.
         * Optimizado con índice en la columna status.
         */
        @Query("SELECT e FROM EventEntity e WHERE e.status = :status")
        List<EventEntity> findByStatus(@Param("status") EventStatus status);

        /**
         * Busca eventos activos con venue.
         * Combina filtro por estado y join fetch.
         */
        @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.status = 'ACTIVO' ORDER BY e.date")
        List<EventEntity> findActiveEventsWithVenue();

        /**
         * Busca eventos futuros activos.
         * Optimizado con índices en date y status.
         */
        @Query("SELECT e FROM EventEntity e WHERE e.status = 'ACTIVO' AND e.date > :today ORDER BY e.date")
        List<EventEntity> findFutureActiveEvents(@Param("today") LocalDate today);

        /**
         * Busca eventos por categoría.
         * join con categories para filtrar.
         */
        @Query("SELECT DISTINCT e FROM EventEntity e JOIN e.categories c WHERE LOWER(c.name) = LOWER(:categoryName)")
        List<EventEntity> findByCategoryName(@Param("categoryName") String categoryName);

        /**
         * Busca eventos por categoría con venue y categorías cargadas.
         * Evita N+1 con múltiples join fetch.
         */
        @Query("SELECT DISTINCT e FROM EventEntity e " +
                        "JOIN FETCH e.venue " +
                        "LEFT JOIN FETCH e.categories c " +
                        "WHERE LOWER(c.name) = LOWER(:categoryName)")
        List<EventEntity> findByCategoryNameWithDetails(@Param("categoryName") String categoryName);

        /**
         * Busca eventos por ciudad del venue.
         * join con venue para filtrar por ciudad.
         */
        @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue v WHERE LOWER(v.city) LIKE LOWER(CONCAT('%', :city, '%'))")
        List<EventEntity> findByVenueCity(@Param("city") String city);

        /**
         * Cuenta eventos por venue.
         * Query de agregación optimizada.
         */
        @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.venue.id = :venueId")
        Long countByVenueId(@Param("venueId") Long venueId);

        /**
         * Busca eventos por título (búsqueda parcial).
         * Optimizado con índice en title.
         */
        @Query("SELECT e FROM EventEntity e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))")
        List<EventEntity> findByTitleContaining(@Param("title") String title);

        /**
         * Query compleja: Busca eventos activos futuros en una ciudad específica.
         * Combina múltiples filtros y join fetch.
         */
        @Query("SELECT e FROM EventEntity e " +
                        "JOIN FETCH e.venue v " +
                        "WHERE e.status = 'ACTIVO' " +
                        "AND e.date > :today " +
                        "AND LOWER(v.city) = LOWER(:city) " +
                        "ORDER BY e.date")
        List<EventEntity> findFutureActiveEventsByCity(
                        @Param("today") LocalDate today,
                        @Param("city") String city);

        // ========== Queries derivadas (Spring Data JPA) ==========
        // Estas son menos eficientes que JPQL pero más simples

        /**
         * Busca eventos por fecha exacta.
         * Spring Data genera la query automáticamente.
         */
        List<EventEntity> findByDate(LocalDate date);

        /**
         * Busca eventos después de una fecha.
         */
        List<EventEntity> findByDateAfter(LocalDate date);

        /**
         * Busca eventos antes de una fecha.
         */
        List<EventEntity> findByDateBefore(LocalDate date);

        /**
         * Verifica si existe un evento con el título dado.
         */
        boolean existsByTitleIgnoreCase(String title);
}
