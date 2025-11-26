package com.example.inmemory_events_api.infraestructura.adapters.in.web;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import com.example.inmemory_events_api.aplicacion.usecase.EventService;
import com.example.inmemory_events_api.infraestructura.adapters.in.web.dto.EventRequestDTO;
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
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventRequestDTO request) {
        EventDTO eventDTO = mapToDTO(request);
        EventDTO savedEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, 
                                                @Valid @RequestBody EventRequestDTO request) {
        EventDTO eventDTO = mapToDTO(request);
        return eventService.updateEvent(id, eventDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventService.deleteEvent(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mapea EventRequestDTO (capa web) a EventDTO (dominio)
     */
    private EventDTO mapToDTO(EventRequestDTO request) {
        return new EventDTO(null, request.getName(), request.getVenueId(), request.getDate());
    }
}
