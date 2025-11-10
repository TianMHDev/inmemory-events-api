package com.example.inmemory_events_api.service;

import com.example.inmemory_events_api.exception.ResourceNotFoundException;
import com.example.inmemory_events_api.model.EventDTO;
import com.example.inmemory_events_api.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // 🔹 Guardar evento con validación de duplicados
    public EventDTO create(EventDTO eventDTO) {
        if (eventRepository.existsByTitleIgnoreCase(eventDTO.getTitle())) {
            throw new IllegalArgumentException("Ya existe un evento con el título: " + eventDTO.getTitle());
        }
        return eventRepository.save(eventDTO);
    }

    // 🔹 Filtros + paginación
    public Page<EventDTO> getFiltered(Optional<String> city, Optional<String> category,
                                      Optional<LocalDate> fechaInicio, Pageable pageable) {

        String cityFilter = city.orElse("");
        String categoryFilter = category.orElse("");
        LocalDate dateFilter = fechaInicio.orElse(LocalDate.now());

        return eventRepository.findFiltered(cityFilter, categoryFilter, dateFilter, pageable);
    }
}
