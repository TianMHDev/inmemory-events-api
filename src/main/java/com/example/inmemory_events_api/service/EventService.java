package com.example.inmemory_events_api.service;

import com.example.inmemory_events_api.exception.ResourceNotFoundException;
import com.example.inmemory_events_api.model.EventDTO;
import com.example.inmemory_events_api.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventDTO> findAll() {
        return eventRepository.findAll();
    }

    public EventDTO findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + id));
    }

    public EventDTO save(EventDTO event) {
        if (eventRepository.existsByTitle(event.getTitle())) {
            throw new IllegalArgumentException("Ya existe un evento con el título: " + event.getTitle());
        }
        return eventRepository.save(event);
    }

    public EventDTO update(Long id, EventDTO details) {
        EventDTO existing = findById(id);
        existing.setTitle(details.getTitle());
        existing.setDescription(details.getDescription());
        existing.setDate(details.getDate());
        existing.setVenue(details.getVenue());
        return eventRepository.save(existing);
    }

    public void delete(Long id) {
        eventRepository.deleteById(id);
    }
}
