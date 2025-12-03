package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio optimizado para VenueEntity.
 * 
 * Optimizaciones:
 * 1. JpaSpecificationExecutor para filtros dinámicos
 * 2. @EntityGraph para evitar N+1 al cargar eventos
 * 3. JPQL con join fetch
 * 4. Índices en columnas consultadas frecuentemente
 */
public interface VenueRepository extends JpaRepository<VenueEntity, Long>, JpaSpecificationExecutor<VenueEntity> {

        // ========== Queries con join fetch (evita N+1) ==========

        /**
         * Busca venue por ID con sus eventos cargados.
         * Evita N+1: 1 query en lugar de 2 (venue + eventos)
         */
        @Query("SELECT v FROM VenueEntity v LEFT JOIN FETCH v.events WHERE v.id = :id")
        Optional<VenueEntity> findByIdWithEvents(@Param("id") Long id);

        /**
         * Lista todos los venues con sus eventos.
         * Evita N+1: 1 query en lugar de N+1
         */
        @Query("SELECT DISTINCT v FROM VenueEntity v LEFT JOIN FETCH v.events")
        List<VenueEntity> findAllWithEvents();

        // ========== Queries JPQL optimizadas ==========

        /**
         * Busca venues por ciudad.
         * Optimizado con índice en city.
         */
        @Query("SELECT v FROM VenueEntity v WHERE LOWER(v.city) LIKE LOWER(CONCAT('%', :city, '%'))")
        List<VenueEntity> findByCity(@Param("city") String city);

        /**
         * Busca venues por ciudad con eventos.
         */
        @Query("SELECT DISTINCT v FROM VenueEntity v LEFT JOIN FETCH v.events WHERE LOWER(v.city) LIKE LOWER(CONCAT('%', :city, '%'))")
        List<VenueEntity> findByCityWithEvents(@Param("city") String city);

        /**
         * Busca venues con capacidad mínima.
         * Optimizado con índice en capacity.
         */
        @Query("SELECT v FROM VenueEntity v WHERE v.capacity >= :minCapacity ORDER BY v.capacity")
        List<VenueEntity> findByMinCapacity(@Param("minCapacity") Integer minCapacity);

        /**
         * Busca venues en un rango de capacidad.
         */
        @Query("SELECT v FROM VenueEntity v WHERE v.capacity BETWEEN :minCapacity AND :maxCapacity ORDER BY v.capacity")
        List<VenueEntity> findByCapacityRange(
                        @Param("minCapacity") Integer minCapacity,
                        @Param("maxCapacity") Integer maxCapacity);

        /**
         * Busca venues por nombre (búsqueda parcial).
         * Optimizado con índice en name.
         */
        @Query("SELECT v FROM VenueEntity v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%'))")
        List<VenueEntity> findByNameContaining(@Param("name") String name);

        /**
         * Busca venues por dirección.
         */
        @Query("SELECT v FROM VenueEntity v WHERE LOWER(v.address) LIKE LOWER(CONCAT('%', :address, '%'))")
        List<VenueEntity> findByAddressContaining(@Param("address") String address);

        /**
         * Cuenta eventos de un venue.
         */
        @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.venue.id = :venueId")
        Long countEventsByVenueId(@Param("venueId") Long venueId);

        /**
         * Busca venues que tienen eventos activos futuros.
         * Query compleja con subquery.
         */
        @Query("SELECT DISTINCT v FROM VenueEntity v " +
                        "WHERE EXISTS (SELECT e FROM EventEntity e " +
                        "WHERE e.venue = v " +
                        "AND e.status = 'ACTIVO' " +
                        "AND e.date > CURRENT_DATE)")
        List<VenueEntity> findVenuesWithFutureActiveEvents();

        // ========== Queries derivadas ==========

        /**
         * Verifica si existe un venue con el nombre dado.
         */
        boolean existsByNameIgnoreCase(String name);

        /**
         * Busca venue por nombre exacto.
         */
        Optional<VenueEntity> findByNameIgnoreCase(String name);
}
