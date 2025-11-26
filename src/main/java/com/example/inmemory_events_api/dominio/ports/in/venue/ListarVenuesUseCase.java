package com.example.inmemory_events_api.dominio.ports.in.venue;

import com.example.inmemory_events_api.dominio.model.VenueDTO;
import java.util.List;

public interface ListarVenuesUseCase {
    List<VenueDTO> ejecutar();
}
