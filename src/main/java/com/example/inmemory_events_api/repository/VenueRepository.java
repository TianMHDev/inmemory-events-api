package com.example.inmemory_events_api.repository;

import com.example.inmemory_events_api.model.VenueDTO;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VenueRepository {

    private final Map<Long, VenueDTO> venues = new HashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<VenueDTO> findAll() {
        return new ArrayList<>(venues.values());
    }

    public Optional<VenueDTO> findById(Long id) {
        return Optional.ofNullable(venues.get(id));
    }

    public boolean existsByNameIgnoreCase(String name) {
        return venues.values().stream()
                .anyMatch(v -> v.getName().equalsIgnoreCase(name));
    }

    public VenueDTO save(VenueDTO venue) {
        if (venue.getId() == null) {
            venue.setId(counter.getAndIncrement());
        }
        venues.put(venue.getId(), venue);
        return venue;
    }

    public void deleteById(Long id) {
        venues.remove(id);
    }
}
