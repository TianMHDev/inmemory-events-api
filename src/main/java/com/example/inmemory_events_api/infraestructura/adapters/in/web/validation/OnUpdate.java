package com.example.inmemory_events_api.infraestructura.adapters.in.web.validation;

/**
 * Grupo de validación para operaciones de ACTUALIZACIÓN.
 * 
 * Este marcador de interfaz se usa con @Validated para aplicar validaciones
 * específicas solo cuando se está actualizando una entidad existente.
 * 
 * Ejemplo de uso:
 * 
 * <pre>
 * {@code
 * &#64;PutMapping("/{id}")
 * public ResponseEntity<?> update(@PathVariable Long id,
 *                                 @Validated(OnUpdate.class) @RequestBody MyDTO dto) {
 *     // Método que actualiza una entidad existente
 * }
 * }
 * </pre>
 * 
 * Casos de uso comunes:
 * - Campos opcionales en actualización pero obligatorios en creación
 * - Validaciones más flexibles en actualización
 * - Permitir actualización parcial de entidades
 * 
 * @see OnCreate
 * @see org.springframework.validation.annotation.Validated
 */
public interface OnUpdate {
    // Interfaz marcadora - no requiere métodos
}
