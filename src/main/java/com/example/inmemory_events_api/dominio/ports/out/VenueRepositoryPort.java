package com.example.inmemory_events_api.dominio.ports.out;

import com.example.inmemory_events_api.dominio.model.VenueDTO;
import java.util.List;
import java.util.Optional;

public interface VenueRepositoryPort {
    List<VenueDTO> findAll();

    Optional<VenueDTO> findById(Long id);

    VenueDTO save(VenueDTO venue);

    boolean deleteById(Long id);
}
