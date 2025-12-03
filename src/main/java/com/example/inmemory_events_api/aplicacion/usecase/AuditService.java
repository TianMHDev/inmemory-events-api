package com.example.inmemory_events_api.aplicacion.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de auditoría simple para demostrar transacciones con
 * Propagation.REQUIRES_NEW.
 * En un caso real, se registrarían eventos de auditoría en una tabla separada.
 */
@Service
public class AuditService {

    /**
     * Registra una acción de auditoría en una transacción independiente.
     * La anotación REQUIRES_NEW garantiza que, incluso si la transacción
     * llamante falla y se hace rollback, este registro persista.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String action) {
        // Aquí iría la lógica de persistencia, por ejemplo:
        // auditRepository.save(new AuditEntry(action, LocalDateTime.now()));
        // Para este ejemplo simplemente imprimimos en consola.
        System.out.println("[AUDIT] " + action);
    }
}
