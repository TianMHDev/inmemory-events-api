package com.example.inmemory_events_api.infraestructura.adapters.in.web.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manejador Global de Excepciones para toda la aplicación.
 * 
 * Esta clase centraliza el manejo de errores utilizando @RestControllerAdvice,
 * lo que permite capturar excepciones en cualquier controlador y devolver
 * respuestas estandarizadas en formato RFC 7807 (ProblemDetail).
 * 
 * Ventajas:
 * - Respuestas de error consistentes en toda la API
 * - Fácil mantenimiento y extensión
 * - Cumplimiento con estándares web (RFC 7807)
 * - Trazabilidad con timestamp y traceId
 * 
 * @author Sebastian
 * @version 1.0
 * @since 2025-12-02
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de Bean Validation (Jakarta Validation).
     * 
     * Se ejecuta cuando las anotaciones @Valid o @Validated fallan en
     * un @RequestBody.
     * Ejemplos: @NotNull, @NotBlank, @Size, @Email, validaciones personalizadas,
     * etc.
     * 
     * @param ex      La excepción que contiene todos los errores de validación
     * @param request El contexto de la petición HTTP
     * @return ResponseEntity con ProblemDetail que incluye todos los errores de
     *         validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        // Crear ProblemDetail base con status 400 (Bad Request)
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Failed");

        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://example.com/errors/validation"));
        problemDetail.setInstance(URI.create(request.getContextPath()));

        // Extraer todos los errores de validación y mapearlos por nombre de campo
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Agregar los errores como una propiedad adicional
        problemDetail.setProperty("errors", errors);

        // Agregar timestamp y traceId para trazabilidad
        addCommonProperties(problemDetail);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Maneja EntityNotFoundException de JPA/Hibernate.
     * 
     * Se lanza cuando una entidad no se encuentra en la base de datos.
     * 
     * @param ex      La excepción lanzada
     * @param request El contexto de la petición HTTP
     * @return ResponseEntity con ProblemDetail indicando que el recurso no fue
     *         encontrado
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFound(
            EntityNotFoundException ex,
            WebRequest request) {
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "Resource Not Found",
                "https://example.com/errors/not-found",
                request);
    }

    /**
     * Maneja ResourceNotFoundException personalizada.
     * 
     * Similar a EntityNotFoundException pero para nuestra excepción personalizada.
     * 
     * @param ex      La excepción lanzada
     * @param request El contexto de la petición HTTP
     * @return ResponseEntity con ProblemDetail indicando que el recurso no fue
     *         encontrado
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request) {
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "Resource Not Found",
                "https://example.com/errors/not-found",
                request);
    }

    /**
     * Maneja violaciones de integridad de datos en la base de datos.
     * 
     * Ocurre cuando:
     * - Se intenta insertar un valor duplicado en una columna UNIQUE
     * - Se viola una constraint de FOREIGN KEY
     * - Se viola una constraint de CHECK
     * - Se intenta insertar NULL en una columna NOT NULL
     * 
     * @param ex      La excepción de violación de integridad
     * @param request El contexto de la petición HTTP
     * @return ResponseEntity con ProblemDetail indicando un conflicto de datos
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                ex.getMostSpecificCause().getMessage(),
                "Data Integrity Violation",
                "https://example.com/errors/conflict",
                request);
    }

    /**
     * Manejador genérico para todas las demás excepciones no capturadas.
     * 
     * Este es el último recurso cuando ningún otro @ExceptionHandler coincide.
     * Devuelve un error 500 (Internal Server Error).
     * 
     * IMPORTANTE: En producción, evita exponer detalles internos del error.
     * 
     * @param ex      Cualquier excepción no manejada específicamente
     * @param request El contexto de la petición HTTP
     * @return ResponseEntity con ProblemDetail indicando un error interno del
     *         servidor
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAllExceptions(
            Exception ex,
            WebRequest request) {
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                "Internal Server Error",
                "https://example.com/errors/internal-server-error",
                request);
    }

    /**
     * Método auxiliar para construir ProblemDetail de forma consistente.
     * 
     * Centraliza la creación de respuestas de error para evitar duplicación de
     * código.
     * 
     * @param status  El código de estado HTTP
     * @param detail  El mensaje detallado del error
     * @param title   El título del error
     * @param type    La URI que identifica el tipo de error
     * @param request El contexto de la petición HTTP
     * @return ResponseEntity con el ProblemDetail configurado
     */
    private ResponseEntity<ProblemDetail> buildProblemDetail(
            HttpStatus status,
            String detail,
            String title,
            String type,
            WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(type));
        problemDetail.setInstance(URI.create(request.getContextPath()));

        addCommonProperties(problemDetail);

        return ResponseEntity.status(status).body(problemDetail);
    }

    /**
     * Agrega propiedades comunes a todas las respuestas de error.
     * 
     * - timestamp: Momento exacto en que ocurrió el error (útil para logs y
     * debugging)
     * - traceId: Identificador único para rastrear el error en logs distribuidos
     * 
     * @param problemDetail El objeto ProblemDetail a enriquecer
     */
    private void addCommonProperties(ProblemDetail problemDetail) {
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("traceId", UUID.randomUUID().toString());
    }
}
