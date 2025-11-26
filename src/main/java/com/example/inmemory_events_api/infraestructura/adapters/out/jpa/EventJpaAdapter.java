package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import com.example.inmemory_events_api.dominio.ports.out.EventRepositoryPort;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.mapper.EventMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventJpaAdapter implements EventRepositoryPort {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final EventMapper eventMapper;

    public EventJpaAdapter(EventRepository eventRepository, 
                          VenueRepository venueRepository,
                          EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventDTO> findAll() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventDTO> findById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::toDTO);
    }

    @Override
    public EventDTO save(EventDTO event) {
        EventEntity entity = eventMapper.toEntity(event);
        
        // Manejar la relación con Venue manualmente
        if (event.getVenueId() != null) {
            Optional<VenueEntity> venue = venueRepository.findById(event.getVenueId());
            venue.ifPresent(entity::setVenue);
        }
        
        EventEntity saved = eventRepository.save(entity);
        return eventMapper.toDTO(saved);
    }

    @Override
    public boolean deleteById(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
