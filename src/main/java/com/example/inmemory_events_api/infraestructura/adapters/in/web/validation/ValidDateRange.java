package com.example.inmemory_events_api.infraestructura.adapters.in.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación de validación personalizada para validar rangos de fechas.
 * 
 * Esta validación cruzada verifica que la fecha de inicio (startDate)
 * sea anterior a la fecha de fin (endDate) en un objeto.
 * 
 * Se aplica a nivel de CLASE (TYPE), no a nivel de campo individual,
 * porque necesita acceder a DOS campos simultáneamente para compararlos.
 * 
 * Ejemplo de uso:
 * 
 * <pre>
 * {@code
 * @ValidDateRange
 * public class EventRequestDTO {
 *     private LocalDate startDate;
 *     private LocalDate endDate;
 * }
 * }
 * </pre>
 * 
 * El mensaje de error se puede personalizar en messages.properties
 * con la clave: event.dateRange.invalid
 * 
 * @see DateRangeValidator La clase que implementa la lógica de validación
 */
@Constraint(validatedBy = DateRangeValidator.class) // Vincula con el validador
@Target({ ElementType.TYPE }) // Se aplica a clases/interfaces
@Retention(RetentionPolicy.RUNTIME) // Disponible en tiempo de ejecución
public @interface ValidDateRange {

    /**
     * Mensaje de error por defecto si la validación falla.
     * El {event.dateRange.invalid} busca el mensaje en messages.properties
     */
    String message() default "{event.dateRange.invalid}";

    /**
     * Grupos de validación a los que pertenece esta constraint.
     * Permite aplicar la validación solo en ciertos contextos (OnCreate, OnUpdate,
     * etc.)
     */
    Class<?>[] groups() default {};

    /**
     * Payload para metadata adicional (raramente usado).
     * Permite adjuntar información extra a la violación de constraint.
     */
    Class<? extends Payload>[] payload() default {};
}
