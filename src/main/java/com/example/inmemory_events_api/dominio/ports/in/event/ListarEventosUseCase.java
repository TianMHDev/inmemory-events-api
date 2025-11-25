package com.example.inmemory_events_api.dominio.ports.in.event;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import java.util.List;

public interface ListarEventosUseCase {
    List<EventDTO> ejecutar();
}
