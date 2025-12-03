package com.example.inmemory_events_api.infraestructura.adapters.in.web.validation;

/**
 * Grupo de validación para operaciones de CREACIÓN.
 * 
 * Este marcador de interfaz se usa con @Validated para aplicar validaciones
 * específicas solo cuando se está creando una nueva entidad.
 * 
 * Ejemplo de uso:
 * 
 * <pre>
 * {@code
 * @PostMapping
 * public ResponseEntity<?> create(@Validated(OnCreate.class) @RequestBody MyDTO dto) {
 *     // Método que crea una nueva entidad
 * }
 * }
 * </pre>
 * 
 * Casos de uso comunes:
 * - Campos obligatorios solo en creación (ej: venueId)
 * - Validaciones más estrictas en creación
 * - Campos que no deben modificarse después de la creación
 * 
 * @see OnUpdate
 * @see org.springframework.validation.annotation.Validated
 */
public interface OnCreate {
    // Interfaz marcadora - no requiere métodos
}
