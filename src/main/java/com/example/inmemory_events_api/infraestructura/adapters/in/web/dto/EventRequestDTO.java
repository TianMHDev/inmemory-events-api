package com.example.inmemory_events_api.infraestructura.adapters.in.web.dto;

import com.example.inmemory_events_api.infraestructura.adapters.in.web.validation.OnCreate;
import com.example.inmemory_events_api.infraestructura.adapters.in.web.validation.OnUpdate;
import com.example.inmemory_events_api.infraestructura.adapters.in.web.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para las peticiones de creación/actualización de
 * eventos.
 * 
 * Este DTO representa los datos que llegan desde el cliente en formato JSON
 * en el cuerpo de las peticiones HTTP POST y PUT.
 * 
 * Características:
 * - Anotado con @Data de Lombok para generar getters, setters, equals, hashCode
 * y toString
 * - Validaciones con grupos (OnCreate, OnUpdate) para diferentes escenarios
 * - Validación cruzada con @ValidDateRange para verificar lógica de negocio
 * - Mensajes de error personalizados usando messages.properties
 * 
 * Ejemplo de JSON esperado:
 * 
 * <pre>
 * {
 *   "name": "Concierto de Rock",
 *   "venueId": 1,
 *   "startDate": "2025-12-01",
 *   "endDate": "2025-12-02"
 * }
 * </pre>
 * 
 * Separación de responsabilidades:
 * - EventRequestDTO: Validación de entrada de la capa web
 * - EventDTO: Modelo de dominio puro (sin dependencias de frameworks)
 * - EventEntity: Persistencia en base de datos con JPA
 * 
 * @see OnCreate Grupo de validación para creación
 * @see OnUpdate Grupo de validación para actualización
 * @see ValidDateRange Validación cruzada de fechas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidDateRange(groups = { OnCreate.class, OnUpdate.class })
public class EventRequestDTO {

    /**
     * Nombre del evento.
     * 
     * Validaciones:
     * - @NotBlank: No puede ser null, vacío o solo espacios
     * - Aplica en: Creación (OnCreate) y Actualización (OnUpdate)
     * - Mensaje: Definido en messages.properties con clave "event.name.notBlank"
     */
    @NotBlank(message = "{event.name.notBlank}", groups = { OnCreate.class, OnUpdate.class })
    private String name;

    /**
     * ID del lugar (venue) donde se realizará el evento.
     * 
     * Validaciones:
     * - @NotNull: No puede ser null
     * - Aplica solo en: Creación (OnCreate)
     * - En actualización no es obligatorio (permite actualización parcial)
     * - Mensaje: Definido en messages.properties con clave "event.venueId.notNull"
     * 
     * Relación: Este ID debe corresponder a un VenueEntity existente en la BD
     */
    @NotNull(message = "{event.venueId.notNull}", groups = { OnCreate.class })
    private Long venueId;

    /**
     * Fecha de inicio del evento.
     * 
     * Validaciones:
     * - @NotNull: No puede ser null
     * - Aplica solo en: Creación (OnCreate)
     * - Validación adicional: Debe ser anterior a endDate (@ValidDateRange)
     * - Mensaje: Definido en messages.properties con clave
     * "event.startDate.notNull"
     * 
     * Formato esperado en JSON: "yyyy-MM-dd" (ej: "2025-12-01")
     * Spring Boot convierte automáticamente el String JSON a LocalDate
     */
    @NotNull(message = "{event.startDate.notNull}", groups = { OnCreate.class })
    private LocalDate startDate;

    /**
     * Fecha de fin del evento.
     * 
     * Validaciones:
     * - @NotNull: No puede ser null
     * - Aplica solo en: Creación (OnCreate)
     * - Validación adicional: Debe ser posterior a startDate (@ValidDateRange)
     * - Mensaje: Definido en messages.properties con clave "event.endDate.notNull"
     * 
     * Formato esperado en JSON: "yyyy-MM-dd" (ej: "2025-12-02")
     * Spring Boot convierte automáticamente el String JSON a LocalDate
     */
    @NotNull(message = "{event.endDate.notNull}", groups = { OnCreate.class })
    private LocalDate endDate;
}
