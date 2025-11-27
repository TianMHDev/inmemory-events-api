package com.example.inmemory_events_api.infraestructura.adapters.out.jpa.mapper;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper para convertir entre EventEntity (JPA) y EventDTO (Dominio).
 * MapStruct genera automáticamente la implementación en tiempo de compilación.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(source = "title", target = "name")
    @Mapping(source = "venue.id", target = "venueId")
    @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd")
    EventDTO toDTO(EventEntity entity);

    @Mapping(source = "name", target = "title")
    @Mapping(target = "venue", ignore = true) // Se maneja manualmente en el adaptador
    @Mapping(target = "categories", ignore = true) // Se maneja manualmente si es necesario
    @Mapping(target = "description", constant = "No description")
    @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd")
    EventEntity toEntity(EventDTO dto);
}
