package com.example.inmemory_events_api.controller;

import com.example.inmemory_events_api.model.EventDTO;
import com.example.inmemory_events_api.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @PostMapping
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO event) {
        return ResponseEntity.status(201).body(eventService.create(event));
    }


    @GetMapping
    public ResponseEntity<Page<EventDTO>> getFiltered(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @PageableDefault(size = 5, sort = "date") Pageable pageable) {

        Page<EventDTO> result = eventService.getFiltered(
                Optional.ofNullable(city),
                Optional.ofNullable(category),
                Optional.ofNullable(fechaInicio),
                pageable
        );

        return ResponseEntity.ok(result);
    }
}
