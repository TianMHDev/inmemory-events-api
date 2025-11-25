package com.example.inmemory_events_api.aplicacion.usecase;

import com.example.inmemory_events_api.dominio.model.VenueDTO;
import com.example.inmemory_events_api.dominio.ports.out.VenueRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    private final VenueRepositoryPort venueRepositoryPort;

    public VenueService(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    // Implementación de casos de uso
    public List<VenueDTO> getAllVenues() {
        return venueRepositoryPort.findAll();
    }

    public Optional<VenueDTO> getVenueById(Long id) {
        return venueRepositoryPort.findById(id);
    }

    public VenueDTO createVenue(VenueDTO venue) {
        return venueRepositoryPort.save(venue);
    }

    public Optional<VenueDTO> updateVenue(Long id, VenueDTO newVenue) {
        return venueRepositoryPort.findById(id).map(existing -> {
            newVenue.setId(id);
            return venueRepositoryPort.save(newVenue);
        });
    }

    public boolean deleteVenue(Long id) {
        return venueRepositoryPort.deleteById(id);
    }
}
