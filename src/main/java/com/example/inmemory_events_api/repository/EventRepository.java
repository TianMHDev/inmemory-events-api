package com.example.inmemory_events_api.repository;

import com.example.inmemory_events_api.model.EventDTO;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class EventRepository {

    private final Map<Long, EventDTO> events = new HashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<EventDTO> findAll() {
        return new ArrayList<>(events.values());
    }

    public Optional<EventDTO> findById(Long id) {
        return Optional.ofNullable(events.get(id));
    }

    public boolean existsByTitleIgnoreCase(String title) {
        return events.values().stream()
                .anyMatch(e -> e.getTitle().equalsIgnoreCase(title));
    }

    public EventDTO save(EventDTO event) {
        if (event.getId() == null) {
            event.setId(counter.getAndIncrement());
        }
        events.put(event.getId(), event);
        return event;
    }

    // 🔹 Filtro + paginación
    public Page<EventDTO> findFiltered(String city, String category, LocalDate fechaInicio, Pageable pageable) {
        List<EventDTO> filtered = events.values().stream()
                .filter(e -> e.getCity().toLowerCase().contains(city.toLowerCase()))
                .filter(e -> e.getCategory().toLowerCase().contains(category.toLowerCase()))
                .filter(e -> e.getDate().isAfter(fechaInicio))
                .sorted(Comparator.comparing(EventDTO::getDate))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<EventDTO> paged = filtered.subList(start, end);

        return new PageImpl<>(paged, pageable, filtered.size());
    }
}
