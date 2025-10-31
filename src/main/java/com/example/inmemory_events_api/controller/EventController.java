package com.example.inmemory_events_api.controller;

import com.example.inmemory_events_api.model.EventDTO;
import com.example.inmemory_events_api.service.EventService;
import com.example.inmemory_events_api.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Eventos", description = "Operaciones CRUD para eventos")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los eventos", description = "Retorna una lista completa de eventos almacenados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public List<EventDTO> getAll() {
        return service.getAllEvents();
    }

    @Operation(
            summary = "Crear un nuevo evento",
            description = "Registra un evento con los campos obligatorios",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EventDTO.class),
                            examples = @ExampleObject(value = """
                        {
                          "id": 1,
                          "name": "Festival de Música",
                          "location": "Medellín",
                          "date": "2025-12-10"
                        }
                        """)
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Evento creado correctamente")
    @PostMapping
    public ResponseEntity<EventDTO> create(@RequestBody EventDTO dto) {
        return ResponseEntity.status(201).body(service.createEvent(dto));
    }

    @Operation(summary = "Buscar evento por ID", description = "Devuelve un evento específico si existe")
    @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable Long id) {
        return service.getEventById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Evento con ID " + id + " no encontrado"));
    }

    @Operation(summary = "Eliminar evento", description = "Elimina un evento existente por ID")
    @ApiResponse(responseCode = "204", description = "Evento eliminado correctamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
