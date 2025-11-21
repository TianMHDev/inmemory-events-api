package com.example.inmemory_events_api.infraestructura.adapters.in.web;

import com.example.inmemory_events_api.infraestructura.entity.EventEntity;
import com.example.inmemory_events_api.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventEntity> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    public ResponseEntity<EventEntity> createEvent(@Valid @RequestBody EventEntity event) {
        EventEntity savedEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> updateEvent(@PathVariable Long id, @Valid @RequestBody EventEntity event) {
        EventEntity updatedEvent = eventService.updateEvent(id, event);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
