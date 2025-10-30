package com.example.inmemory_events_api.service;

import com.example.inmemory_events_api.model.EventDTO;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EventService {

    private final List<EventDTO> events = new ArrayList<>();
    private Long idCounter = 1L;

    public List<EventDTO> getAllEvents() {
        return events;
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
        return getEventById(id).map(event -> {
            event.setName(newEvent.getName());
            event.setVenueId(newEvent.getVenueId());
            event.setDate(newEvent.getDate());
            return event;
        });
    }

    public boolean deleteEvent(Long id) {
        return events.removeIf(e -> e.getId().equals(id));
    }
}
