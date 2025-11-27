package com.example.inmemory_events_api.aplicacion.usecase;

import com.example.inmemory_events_api.dominio.model.VenueDTO;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.VenueRepository;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Transactional(readOnly = true)
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<VenueDTO> getVenueById(Long id) {
        return venueRepository.findById(id)
                .map(this::toDTO);
    }

    public VenueDTO createVenue(VenueDTO venueDTO) {
        VenueEntity entity = toEntity(venueDTO);
        VenueEntity saved = venueRepository.save(entity);
        return toDTO(saved);
    }

    public Optional<VenueDTO> updateVenue(Long id, VenueDTO newVenue) {
        return venueRepository.findById(id).map(existing -> {
            existing.setName(newVenue.getName());
            existing.setAddress(newVenue.getAddress());
            existing.setCity(newVenue.getCity());
            existing.setCapacity(newVenue.getCapacity());
            return toDTO(venueRepository.save(existing));
        });
    }

    public boolean deleteVenue(Long id) {
        if (venueRepository.existsById(id)) {
            venueRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Mappers manuales simples
    private VenueDTO toDTO(VenueEntity entity) {
        return new VenueDTO(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCity(),
                entity.getCapacity());
    }

    private VenueEntity toEntity(VenueDTO dto) {
        VenueEntity entity = new VenueEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setCapacity(dto.getCapacity());
        return entity;
    }
}
