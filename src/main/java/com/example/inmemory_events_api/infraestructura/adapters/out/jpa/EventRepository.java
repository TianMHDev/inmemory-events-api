package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
