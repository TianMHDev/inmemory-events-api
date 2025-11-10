package com.example.inmemory_events_api.service;

import com.example.inmemory_events_api.exception.ResourceNotFoundException;
import com.example.inmemory_events_api.model.VenueDTO;
import com.example.inmemory_events_api.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<VenueDTO> findAll() {
        return venueRepository.findAll();
    }

    public VenueDTO findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado con ID: " + id));
    }

    public VenueDTO save(VenueDTO venue) {
        // Validar duplicados por nombre
        if (venueRepository.existsByName(venue.getName())) {
            throw new IllegalArgumentException("Ya existe un venue con el nombre: " + venue.getName());
        }
        return venueRepository.save(venue);
    }

    public VenueDTO update(Long id, VenueDTO details) {
        VenueDTO existing = findById(id);
        existing.setName(details.getName());
        existing.setAddress(details.getAddress());
        existing.setCapacity(details.getCapacity());
        return venueRepository.save(existing);
    }

    public void delete(Long id) {
        venueRepository.deleteById(id);
    }
}
