package com.example.inmemory_events_api.service;
import com.example.inmemory_events_api.model.EventDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final List<EventDTO> events = new ArrayList<>();
    private Long idCounter = 1L;

    public List<EventDTO> getAllEvents() {
        return new ArrayList<>(events);
    }

    public Optional<EventDTO> getEventById(Long id) {
        return events.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public EventDTO createEvent(EventDTO event) {
        event.setId(idCounter++);
        events.add(event);
        return event;
    }

    public Optional<EventDTO> updateEvent(Long id, EventDTO newEvent) {
        return getEventById(id).map(existing -> {
            existing.setName(newEvent.getName());
            existing.setVenueId(newEvent.getVenueId());
            existing.setDate(newEvent.getDate());
            return existing;
        });
    }

    public boolean deleteEvent(Long id) {
        return events.removeIf(e -> e.getId().equals(id));
    }
}