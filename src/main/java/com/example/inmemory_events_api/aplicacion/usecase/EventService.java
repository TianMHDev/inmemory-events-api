package com.example.inmemory_events_api.aplicacion.usecase;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import com.example.inmemory_events_api.dominio.ports.out.EventRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepositoryPort eventRepositoryPort;

    public EventService(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    // Implementación de casos de uso
    public List<EventDTO> getAllEvents() {
        return eventRepositoryPort.findAll();
    }

    public Optional<EventDTO> getEventById(Long id) {
        return eventRepositoryPort.findById(id);
    }

    public EventDTO createEvent(EventDTO event) {
        return eventRepositoryPort.save(event);
    }

    public Optional<EventDTO> updateEvent(Long id, EventDTO newEvent) {
        return eventRepositoryPort.findById(id).map(existing -> {
            newEvent.setId(id);
            return eventRepositoryPort.save(newEvent);
        });
    }

    public boolean deleteEvent(Long id) {
        return eventRepositoryPort.deleteById(id);
    }
}