package com.example.inmemory_events_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.inmemory_events_api.model.EventDTO;

public interface EventRepository extends JpaRepository<EventDTO, Long> {
    boolean existsByTitle(String title);
}
