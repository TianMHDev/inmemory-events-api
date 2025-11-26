package com.example.inmemory_events_api.infraestructura.adapters.out.jpa;

import com.example.inmemory_events_api.dominio.model.VenueDTO;
import com.example.inmemory_events_api.dominio.ports.out.VenueRepositoryPort;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.mapper.VenueMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VenueJpaAdapter implements VenueRepositoryPort {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public VenueJpaAdapter(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    @Override
    public List<VenueDTO> findAll() {
        return venueRepository.findAll().stream()
                .map(venueMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VenueDTO> findById(Long id) {
        return venueRepository.findById(id)
                .map(venueMapper::toDTO);
    }

    @Override
    public VenueDTO save(VenueDTO venue) {
        VenueEntity entity = venueMapper.toEntity(venue);
        VenueEntity saved = venueRepository.save(entity);
        return venueMapper.toDTO(saved);
    }

    @Override
    public boolean deleteById(Long id) {
        if (venueRepository.existsById(id)) {
            venueRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
