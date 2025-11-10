package com.example.inmemory_events_api.service;

import com.example.inmemory_events_api.exception.ResourceNotFoundException;
import com.example.inmemory_events_api.model.VenueDTO;
import com.example.inmemory_events_api.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public VenueDTO create(VenueDTO venueDTO) {
        if (venueRepository.existsByNameIgnoreCase(venueDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un venue con el nombre: " + venueDTO.getName());
        }
        return venueRepository.save(venueDTO);
    }

    public List<VenueDTO> findAll() {
        return venueRepository.findAll();
    }

    public VenueDTO findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue con ID " + id + " no encontrado"));
    }

    public VenueDTO update(Long id, VenueDTO venueDTO) {
        VenueDTO existing = findById(id);
        existing.setName(venueDTO.getName());
        existing.setAddress(venueDTO.getAddress());
        existing.setCity(venueDTO.getCity());
        existing.setCapacity(venueDTO.getCapacity());
        return venueRepository.save(existing);
    }

    public void delete(Long id) {
        if (!venueRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Venue con ID " + id + " no encontrado");
        }
        venueRepository.deleteById(id);
    }
}
