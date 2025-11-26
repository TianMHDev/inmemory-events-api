package com.example.inmemory_events_api.dominio.ports.in.venue;

import com.example.inmemory_events_api.dominio.model.VenueDTO;

public interface CrearVenueUseCase {
    VenueDTO ejecutar(VenueDTO venue);
}
