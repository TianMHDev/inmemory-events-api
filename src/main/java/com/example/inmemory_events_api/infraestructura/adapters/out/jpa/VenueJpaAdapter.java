package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.dominio.model.VenueDTO;
import com.example.inmemory_events_api.dominio.ports.out.VenueRepositoryPort;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VenueJpaAdapter implements VenueRepositoryPort {

    private final VenueRepository venueRepository;

    public VenueJpaAdapter(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public List<VenueDTO> findAll() {
        return venueRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VenueDTO> findById(Long id) {
        return venueRepository.findById(id).map(this::toDTO);
    }

    @Override
    public VenueDTO save(VenueDTO venue) {
        VenueEntity entity = toEntity(venue);
        VenueEntity saved = venueRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    public boolean deleteById(Long id) {
        if (venueRepository.existsById(id)) {
            venueRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private VenueDTO toDTO(VenueEntity entity) {
        return new VenueDTO(entity.getId(), entity.getName(), entity.getAddress());
    }

    private VenueEntity toEntity(VenueDTO dto) {
        VenueEntity entity = new VenueEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAddress(dto.getLocation());
        entity.setCapacity(100); // Default capacity as it is missing in DTO
        return entity;
    }
}
