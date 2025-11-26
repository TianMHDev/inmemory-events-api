package com.example.inmemory_events_api.dominio.ports.in.event;

import com.example.inmemory_events_api.dominio.model.EventDTO;

public interface CrearEventoUseCase {
    EventDTO ejecutar(EventDTO event);
}
