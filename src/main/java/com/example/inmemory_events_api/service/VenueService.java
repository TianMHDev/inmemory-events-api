package com.example.inmemory_events_api.service;

import com.example.inmemory_events_api.exception.ResourceNotFoundException;
import com.example.inmemory_events_api.model.VenueEntity;
import com.example.inmemory_events_api.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<VenueEntity> getAllVenues() {
        return venueRepository.findAll();
    }

    public VenueEntity getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id " + id));
    }

    public VenueEntity createVenue(VenueEntity venue) {
        return venueRepository.save(venue);
    }

    public VenueEntity updateVenue(Long id, VenueEntity updatedVenue) {
        VenueEntity existing = getVenueById(id);
        existing.setName(updatedVenue.getName());
        existing.setAddress(updatedVenue.getAddress());
        existing.setCapacity(updatedVenue.getCapacity());
        return venueRepository.save(existing);
    }

    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }
}
