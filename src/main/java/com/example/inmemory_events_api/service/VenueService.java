package com.example.inmemory_events_api.service;

import com.example.inmemory_events_api.model.VenueDTO;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class VenueService {

    private final List<VenueDTO> venues = new ArrayList<>();
    private Long idCounter = 1L;

    public List<VenueDTO> getAllVenues() {
        return venues;
    }

    public Optional<VenueDTO> getVenueById(Long id) {
        return venues.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    public VenueDTO createVenue(VenueDTO venue) {
        venue.setId(idCounter++);
        venues.add(venue);
        return venue;
    }

    public Optional<VenueDTO> updateVenue(Long id, VenueDTO newVenue) {
        return getVenueById(id).map(v -> {
            v.setName(newVenue.getName());
            v.setLocation(newVenue.getLocation());
            return v;
        });
    }

    public boolean deleteVenue(Long id) {
        return venues.removeIf(v -> v.getId().equals(id));
    }
}
