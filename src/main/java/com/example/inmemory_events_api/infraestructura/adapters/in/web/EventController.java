package com.example.inmemory_events_api.infraestructura.adapters.in.web;

import com.example.inmemory_events_api.dominio.model.EventDTO;
import com.example.inmemory_events_api.aplicacion.usecase.EventService;
import com.example.inmemory_events_api.infraestructura.adapters.in.web.dto.EventRequestDTO;
import com.example.inmemory_events_api.infraestructura.adapters.in.web.validation.OnCreate;
import com.example.inmemory_events_api.infraestructura.adapters.in.web.validation.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Eventos.
 * 
 * Este controlador expone endpoints HTTP para operaciones CRUD sobre eventos.
 * Todas las respuestas y peticiones usan formato JSON.
 * 
 * Endpoints disponibles:
 * - GET /api/events → Lista todos los eventos
 * - GET /api/events/{id} → Obtiene un evento por ID
 * - POST /api/events → Crea un nuevo evento
 * - PUT /api/events/{id} → Actualiza un evento existente
 * - DELETE /api/events/{id} → Elimina un evento
 * 
 * Características:
 * - Validación automática con Bean Validation
 * - Grupos de validación para diferenciar creación vs actualización
 * - Manejo de errores centralizado en GlobalExceptionHandler
 * - Arquitectura hexagonal: este controlador es un ADAPTADOR DE ENTRADA
 * 
 * @see EventService La lógica de negocio (capa de aplicación)
 * @see EventRequestDTO DTO de entrada con validaciones
 * @see EventDTO Modelo de dominio
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    /**
     * Constructor con inyección de dependencias.
     * Spring inyecta automáticamente EventService.
     */
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Lista todos los eventos.
     * 
     * @return Lista de EventDTO (puede estar vacía)
     * 
     *         Ejemplo de respuesta exitosa (200 OK):
     * 
     *         <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Concierto Rock",
     *     "venueId": 1,
     *     "date": "2025-12-01"
     *   }
     * ]
     *         </pre>
     */
    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Obtiene un evento específico por su ID.
     * 
     * @param id El identificador único del evento
     * @return ResponseEntity con el evento si existe (200 OK)
     *         o 404 NOT FOUND si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok) // Si existe → 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no existe → 404 NOT FOUND
    }

    /**
     * Crea un nuevo evento.
     * 
     * Este endpoint aplica el grupo de validación OnCreate, lo que significa:
     * - name debe estar presente y no vacío
     * - venueId es OBLIGATORIO (solo en creación)
     * - startDate y endDate son OBLIGATORIAS
     * - startDate debe ser anterior a endDate
     * 
     * @param request DTO con los datos del evento a crear
     * @return ResponseEntity con el evento creado (201 CREATED)
     * 
     *         Errores posibles:
     *         - 400 BAD REQUEST: Si las validaciones fallan
     *         - 500 INTERNAL SERVER ERROR: Si hay error en BD
     * 
     *         Ejemplo de petición:
     * 
     *         <pre>
     * POST /api/events
     * {
     *   "name": "Festival de Jazz",
     *   "venueId": 1,
     *   "startDate": "2025-12-01",
     *   "endDate": "2025-12-03"
     * }
     *         </pre>
     */
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(
            @Validated(OnCreate.class) @RequestBody EventRequestDTO request) {

        // Mapear DTO de entrada (capa web) a modelo de dominio
        EventDTO eventDTO = mapToDTO(request);

        // Delegar la lógica de negocio al servicio
        EventDTO savedEvent = eventService.createEvent(eventDTO);

        // Retornar 201 CREATED con el evento creado en el body
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    /**
     * Actualiza un evento existente.
     * 
     * Este endpoint aplica el grupo de validación OnUpdate, lo que significa:
     * - name debe estar presente si se envía
     * - venueId es OPCIONAL (permite actualización parcial)
     * - Las fechas son opcionales en actualización
     * 
     * @param id      El ID del evento a actualizar
     * @param request DTO con los nuevos datos
     * @return ResponseEntity con el evento actualizado (200 OK)
     *         o 404 NOT FOUND si el evento no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable Long id,
            @Validated(OnUpdate.class) @RequestBody EventRequestDTO request) {

        EventDTO eventDTO = mapToDTO(request);

        return eventService.updateEvent(id, eventDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un evento.
     * 
     * @param id El ID del evento a eliminar
     * @return 204 NO CONTENT si se eliminó correctamente
     *         404 NOT FOUND si el evento no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventService.deleteEvent(id)) {
            return ResponseEntity.noContent().build(); // 204 NO CONTENT
        } else {
            return ResponseEntity.notFound().build(); // 404 NOT FOUND
        }
    }

    /**
     * Mapea EventRequestDTO (capa web) a EventDTO (dominio).
     * 
     * Esta conversión mantiene la separación de responsabilidades:
     * - EventRequestDTO: Conoce validaciones y anotaciones web
     * - EventDTO: Modelo puro de dominio sin dependencias de frameworks
     * 
     * NOTA: Aquí usamos startDate como la fecha principal del evento.
     * En un escenario real, podrías guardar ambas fechas (start y end).
     * 
     * @param request El DTO de la petición HTTP
     * @return EventDTO para la capa de dominio
     */
    private EventDTO mapToDTO(EventRequestDTO request) {
        return new EventDTO(
                null, // ID será asignado por la BD
                request.getName(), // Nombre del evento
                request.getVenueId(), // ID del venue
                request.getStartDate() // Fecha de inicio como fecha principal
        );
    }
}
