package com.example.inmemory_events_api.controller;

import com.example.inmemory_events_api.model.VenueDTO;
import com.example.inmemory_events_api.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public List<VenueDTO> getAll() {
        return venueService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VenueDTO> create(@Valid @RequestBody VenueDTO venue) {
        return ResponseEntity.status(201).body(venueService.save(venue));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueDTO> update(@PathVariable Long id, @Valid @RequestBody VenueDTO venue) {
        return ResponseEntity.ok(venueService.update(id, venue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
