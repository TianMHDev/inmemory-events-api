package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import com.example.inmemory_events_api.dominio.ports.out.EventRepositoryPort;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventJpaAdapter implements EventRepositoryPort {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public EventJpaAdapter(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public List<EventDTO> findAll() {
        return eventRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventDTO> findById(Long id) {
        return eventRepository.findById(id).map(this::toDTO);
    }

    @Override
    public EventDTO save(EventDTO event) {
        EventEntity entity = toEntity(event);
        EventEntity saved = eventRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    public boolean deleteById(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private EventDTO toDTO(EventEntity entity) {
        String dateStr = entity.getDate() != null ? entity.getDate().toString() : "";
        Long venueId = entity.getVenue() != null ? entity.getVenue().getId() : null;
        return new EventDTO(entity.getId(), entity.getTitle(), venueId, dateStr);
    }

    private EventEntity toEntity(EventDTO dto) {
        EventEntity entity = new EventEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getName());
        entity.setDescription("No description"); // Default description
        if (dto.getDate() != null && !dto.getDate().isEmpty()) {
            try {
                entity.setDate(LocalDate.parse(dto.getDate()));
            } catch (Exception e) {
                entity.setDate(LocalDate.now());
            }
        } else {
            entity.setDate(LocalDate.now());
        }

        if (dto.getVenueId() != null) {
            Optional<VenueEntity> venue = venueRepository.findById(dto.getVenueId());
            venue.ifPresent(entity::setVenue);
        }

        return entity;
    }
}
