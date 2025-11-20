package com.example.inmemory_events_api.service;

import com.example.inmemory_events_api.exception.ResourceNotFoundException;
import com.example.inmemory_events_api.infraestructura.entity.EventEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public EventEntity getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }

    public EventEntity createEvent(EventEntity event) {
        return eventRepository.save(event);
    }

    public EventEntity updateEvent(Long id, EventEntity updatedEvent) {
        EventEntity existing = getEventById(id);
        existing.setTitle(updatedEvent.getTitle());
        existing.setDescription(updatedEvent.getDescription());
        existing.setDate(updatedEvent.getDate());
        existing.setVenue(updatedEvent.getVenue());
        return eventRepository.save(existing);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
