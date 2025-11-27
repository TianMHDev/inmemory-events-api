package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.specification;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications para consultas dinámicas de VenueEntity.
 */
public class VenueSpecifications {

    /**
     * Filtra venues por nombre (búsqueda parcial)
     */
    public static Specification<VenueEntity> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

    /**
     * Filtra venues por ciudad
     */
    public static Specification<VenueEntity> inCity(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("city")),
                    "%" + city.toLowerCase() + "%");
        };
    }

    /**
     * Filtra venues con capacidad mínima
     */
    public static Specification<VenueEntity> hasMinCapacity(Integer minCapacity) {
        return (root, query, cb) -> {
            if (minCapacity == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("capacity"), minCapacity);
        };
    }

    /**
     * Filtra venues con capacidad máxima
     */
    public static Specification<VenueEntity> hasMaxCapacity(Integer maxCapacity) {
        return (root, query, cb) -> {
            if (maxCapacity == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("capacity"), maxCapacity);
        };
    }

    /**
     * Filtra venues con capacidad en un rango
     */
    public static Specification<VenueEntity> capacityBetween(Integer minCapacity, Integer maxCapacity) {
        return (root, query, cb) -> {
            if (minCapacity == null && maxCapacity == null) {
                return cb.conjunction();
            }
            if (minCapacity == null) {
                return cb.lessThanOrEqualTo(root.get("capacity"), maxCapacity);
            }
            if (maxCapacity == null) {
                return cb.greaterThanOrEqualTo(root.get("capacity"), minCapacity);
            }
            return cb.between(root.get("capacity"), minCapacity, maxCapacity);
        };
    }

    /**
     * Filtra venues por dirección (búsqueda parcial)
     */
    public static Specification<VenueEntity> addressContains(String address) {
        return (root, query, cb) -> {
            if (address == null || address.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("address")),
                    "%" + address.toLowerCase() + "%");
        };
    }
}
