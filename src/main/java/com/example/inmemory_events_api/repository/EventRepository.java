package com.example.inmemory_events_api.repository;

import com.example.inmemory_events_api.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
