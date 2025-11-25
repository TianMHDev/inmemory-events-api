package com.example.inmemory_events_api.infraestructura.adapters.in.web;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import com.example.inmemory_events_api.application.usecase.VenueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<VenueEntity> createVenue(@Valid @RequestBody VenueEntity venue) {
        VenueEntity createdVenue = venueService.createVenue(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVenue);
    }

    @GetMapping
    public ResponseEntity<List<VenueEntity>> getAllVenues() {
        List<VenueEntity> venues = venueService.getAllVenues();
        return ResponseEntity.ok(venues);
    }


    @GetMapping("/{id}")
    public ResponseEntity<VenueEntity> getVenueById(@PathVariable Long id) {
        VenueEntity venue = venueService.getVenueById(id);
        return ResponseEntity.ok(venue);
    }


    @PutMapping("/{id}")
    public ResponseEntity<VenueEntity> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueEntity updatedVenue) {
        VenueEntity venue = venueService.updateVenue(id, updatedVenue);
        return ResponseEntity.ok(venue);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
