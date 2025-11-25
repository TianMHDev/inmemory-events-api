package com.example.inmemory_events_api.dominio.ports.out;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryPort {
    List<EventDTO> findAll();

    Optional<EventDTO> findById(Long id);

    EventDTO save(EventDTO event);

    boolean deleteById(Long id);
}
