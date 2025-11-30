package com.example.inmemory_events_api.aplicacion.usecase;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.EventRepository;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Implementación usando repositorio JPA optimizado

    /**
     * Transacción de solo lectura.
     * Mejora el rendimiento al evitar el "dirty checking" de Hibernate.
     * Propagation.SUPPORTS podría usarse si no se requiere transacción forzosa,
     * pero REQUIRED es más seguro para consistencia.
     */
    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        // Usamos la query optimizada con join fetch para evitar N+1
        return eventRepository.findAllWithVenue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EventDTO> getEventById(Long id) {
        // Usamos la query optimizada
        return eventRepository.findWithVenueById(id)
                .map(this::toDTO);
    }

    /**
     * Transacción de escritura (por defecto readOnly=false).
     * Usa el aislamiento por defecto (usualmente READ_COMMITTED).
     * Si falla, se hace rollback automático de toda la operación.
     */
    public EventDTO createEvent(EventDTO eventDTO) {
        EventEntity entity = toEntity(eventDTO);
        EventEntity saved = eventRepository.save(entity);
        return toDTO(saved);
    }

    public Optional<EventDTO> updateEvent(Long id, EventDTO newEvent) {
        return eventRepository.findById(id).map(existing -> {
            existing.setTitle(newEvent.getName());
            existing.setDate(java.time.LocalDate.parse(newEvent.getDate()));
            // Nota: Actualizar venue requeriría buscar el venue entity
            return toDTO(eventRepository.save(existing));
        });
    }

    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Mappers manuales simples para romper dependencia de MapStruct
    private EventDTO toDTO(EventEntity entity) {
        return new EventDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getVenue() != null ? entity.getVenue().getId() : null,
                entity.getDate().toString());
    }

    private EventEntity toEntity(EventDTO dto) {
        EventEntity entity = new EventEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getName());
        entity.setDescription("Descripción por defecto"); // DTO no tiene descripción
        if (dto.getDate() != null) {
            entity.setDate(java.time.LocalDate.parse(dto.getDate()));
        }

        // Manejo básico de Venue para creación
        if (dto.getVenueId() != null) {
            VenueEntity venue = new VenueEntity();
            venue.setId(dto.getVenueId());
            entity.setVenue(venue);
        }
        return entity;
    }
}