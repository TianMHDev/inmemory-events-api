package com.example.inmemory_events_api.infraestructura.adapters.in.web;

import com.example.inmemory_events_api.dominio.model.VenueDTO;
import com.example.inmemory_events_api.aplicacion.usecase.VenueService;
import com.example.inmemory_events_api.infraestructura.adapters.in.web.dto.VenueRequestDTO;
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
    public ResponseEntity<VenueDTO> createVenue(@Valid @RequestBody VenueRequestDTO request) {
        VenueDTO venueDTO = mapToDTO(request);
        VenueDTO createdVenue = venueService.createVenue(venueDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVenue);
    }

    @GetMapping
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        List<VenueDTO> venues = venueService.getAllVenues();
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        return venueService.getVenueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueDTO> updateVenue(@PathVariable Long id,
            @Valid @RequestBody VenueRequestDTO request) {
        VenueDTO venueDTO = mapToDTO(request);
        return venueService.updateVenue(id, venueDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        if (venueService.deleteVenue(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mapea VenueRequestDTO (capa web) a VenueDTO (dominio)
     */
    private VenueDTO mapToDTO(VenueRequestDTO request) {
        return new VenueDTO(
                null,
                request.getName(),
                request.getAddress(),
                request.getCity(),
                request.getCapacity());
    }
}
