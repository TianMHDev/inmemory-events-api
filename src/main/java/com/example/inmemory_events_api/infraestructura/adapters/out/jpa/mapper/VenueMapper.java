package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.mapper;

import com.example.inmemory_events_api.dominio.model.VenueDTO;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VenueMapper {

    @Mapping(source = "address", target = "location")
    VenueDTO toDTO(VenueEntity entity);

    @Mapping(source = "location", target = "address")
    @Mapping(target = "capacity", constant = "100")
    @Mapping(target = "events", ignore = true)
    VenueEntity toEntity(VenueDTO dto);
}
