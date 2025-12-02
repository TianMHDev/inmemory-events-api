package com.example.inmemory_events_api.infraestructura.adapters.in.web.validation;

import com.example.inmemory_events_api.infraestructura.adapters.in.web.dto.EventRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementación del validador para @ValidDateRange.
 * 
 * Esta clase contiene la lógica real de validación que verifica
 * que startDate sea anterior a endDate en un EventRequestDTO.
 * 
 * ConstraintValidator<A, T>:
 * - A: El tipo de anotación (@ValidDateRange)
 * - T: El tipo del objeto a validar (EventRequestDTO)
 * 
 * Flujo de validación:
 * 1. Spring encuentra @ValidDateRange en EventRequestDTO
 * 2. Busca el validador correspondiente (esta clase)
 * 3. Llama al método isValid() con el objeto completo
 * 4. Si retorna false, se genera una MethodArgumentNotValidException
 * 
 * @see ValidDateRange La anotación que usa este validador
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, EventRequestDTO> {

    /**
     * Valida que startDate sea anterior a endDate.
     * 
     * Lógica de validación:
     * - Si alguna fecha es null, se considera VÁLIDO
     * (esto permite que @NotNull maneje los nulos por separado)
     * - Si ambas fechas existen, startDate DEBE ser antes de endDate
     * 
     * @param dto     El objeto EventRequestDTO completo a validar
     * @param context Contexto de validación (permite personalizar el mensaje de
     *                error)
     * @return true si la validación pasa, false si falla
     */
    @Override
    public boolean isValid(EventRequestDTO dto, ConstraintValidatorContext context) {
        // Si alguna fecha es null, las validaciones @NotNull lo manejarán
        // Retornamos true aquí para no duplicar mensajes de error
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            return true;
        }

        // Comparar fechas: startDate debe ser ANTES de endDate
        // isBefore() retorna true si la fecha receptora es anterior al parámetro
        return dto.getStartDate().isBefore(dto.getEndDate());
    }
}
