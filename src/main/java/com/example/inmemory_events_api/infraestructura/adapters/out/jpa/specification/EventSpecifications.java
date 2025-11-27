package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.specification;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventStatus;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Specifications para consultas dinámicas de EventEntity.
 * Permite construir queries complejas de forma type-safe y reutilizable.
 * 
 * Ventajas sobre SQL nativo:
 * - Type-safe: errores en tiempo de compilación
 * - Reutilizable: se pueden combinar múltiples specifications
 * - Mantenible: cambios en la entidad se reflejan automáticamente
 */
public class EventSpecifications {

    /**
     * Filtra eventos por venue ID
     */
    public static Specification<EventEntity> hasVenueId(Long venueId) {
        return (root, query, cb) -> {
            if (venueId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("venue").get("id"), venueId);
        };
    }

    /**
     * Filtra eventos por nombre de venue
     */
    public static Specification<EventEntity> hasVenueName(String venueName) {
        return (root, query, cb) -> {
            if (venueName == null || venueName.isBlank()) {
                return cb.conjunction();
            }
            // Join con venue para evitar N+1
            return cb.like(
                    cb.lower(root.get("venue").get("name")),
                    "%" + venueName.toLowerCase() + "%");
        };
    }

    /**
     * Filtra eventos por fecha exacta
     */
    public static Specification<EventEntity> hasDate(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("date"), date);
        };
    }

    /**
     * Filtra eventos después de una fecha
     */
    public static Specification<EventEntity> dateAfter(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("date"), date);
        };
    }

    /**
     * Filtra eventos antes de una fecha
     */
    public static Specification<EventEntity> dateBefore(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("date"), date);
        };
    }

    /**
     * Filtra eventos en un rango de fechas
     */
    public static Specification<EventEntity> dateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return cb.conjunction();
            }
            if (startDate == null) {
                return cb.lessThanOrEqualTo(root.get("date"), endDate);
            }
            if (endDate == null) {
                return cb.greaterThanOrEqualTo(root.get("date"), startDate);
            }
            return cb.between(root.get("date"), startDate, endDate);
        };
    }

    /**
     * Filtra eventos por estado
     */
    public static Specification<EventEntity> hasStatus(EventStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    /**
     * Filtra solo eventos activos
     */
    public static Specification<EventEntity> isActive() {
        return hasStatus(EventStatus.ACTIVO);
    }

    /**
     * Filtra eventos por categoría
     */
    public static Specification<EventEntity> hasCategory(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null || categoryName.isBlank()) {
                return cb.conjunction();
            }
            // Join con categories para filtrar
            return cb.equal(
                    cb.lower(root.join("categories", JoinType.LEFT).get("name")),
                    categoryName.toLowerCase());
        };
    }

    /**
     * Filtra eventos por título (búsqueda parcial)
     */
    public static Specification<EventEntity> titleContains(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%");
        };
    }

    /**
     * Filtra eventos por ciudad del venue
     */
    public static Specification<EventEntity> inCity(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("venue").get("city")),
                    "%" + city.toLowerCase() + "%");
        };
    }

    /**
     * Filtra eventos futuros (fecha mayor a hoy)
     */
    public static Specification<EventEntity> isFuture() {
        return (root, query, cb) -> cb.greaterThan(root.get("date"), LocalDate.now());
    }

    /**
     * Filtra eventos pasados (fecha menor a hoy)
     */
    public static Specification<EventEntity> isPast() {
        return (root, query, cb) -> cb.lessThan(root.get("date"), LocalDate.now());
    }

    /**
     * Combina múltiples filtros con AND
     * Ejemplo: findAll(hasVenueId(1).and(isActive()).and(isFuture()))
     */
}
