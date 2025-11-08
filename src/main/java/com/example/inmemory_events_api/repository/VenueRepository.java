package com.example.inmemory_events_api.repository;

import com.example.inmemory_events_api.model.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<VenueEntity, Long> {
}
