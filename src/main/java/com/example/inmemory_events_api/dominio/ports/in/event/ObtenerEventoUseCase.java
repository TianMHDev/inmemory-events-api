package com.example.inmemory_events_api.dominio.ports.in.event;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import java.util.Optional;

public interface ObtenerEventoUseCase {
    Optional<EventDTO> ejecutar(Long id);
}
