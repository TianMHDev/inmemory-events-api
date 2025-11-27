package com.example.inmemory_events_api.aplicacion.usecase;

import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.CategoryRepository;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.EventRepository;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.VenueRepository;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.CategoryEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.EventEntity;
import com.example.inmemory_events_api.infraestructura.adapters.out.jpa.entity.VenueEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de ejemplo que demuestra el ciclo de vida de entidades JPA
 * y el correcto manejo de relaciones con cascade y orphanRemoval.
 * 
 * IMPORTANTE: Todos los métodos son @Transactional para manejar correctamente
 * las relaciones LAZY y asegurar que los cambios se persistan.
 */
@Service
public class EntityLifecycleExampleService {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EntityLifecycleExampleService(
            VenueRepository venueRepository,
            EventRepository eventRepository,
            CategoryRepository categoryRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Ejemplo 1: Crear un Venue con Eventos (cascade PERSIST)
     * 
     * Ciclo de vida:
     * 1. VenueEntity y EventEntity están en estado TRANSIENT
     * 2. Al hacer save(), pasan a MANAGED
     * 3. Gracias a cascade=ALL, los eventos también se persisten
     * 4. Al commit de la transacción, se ejecutan los INSERT
     */
    @Transactional
    public VenueEntity createVenueWithEvents(String venueName, List<String> eventTitles) {
        // Estado: TRANSIENT
        VenueEntity venue = new VenueEntity();
        venue.setName(venueName);
        venue.setAddress("Dirección de ejemplo");
        venue.setCapacity(100);

        // Crear eventos y asociarlos al venue
        for (String title : eventTitles) {
            EventEntity event = new EventEntity();
            event.setTitle(title);
            event.setDescription("Descripción de " + title);
            event.setDate(LocalDate.now().plusDays(30));

            // Usar método helper para mantener consistencia bidireccional
            venue.addEvent(event);
        }

        // Estado: TRANSIENT → MANAGED
        // Gracias a cascade=ALL, los eventos también pasan a MANAGED
        VenueEntity savedVenue = venueRepository.save(venue);

        // Al finalizar el método (commit), se ejecutan:
        // INSERT INTO venues ...
        // INSERT INTO events ... (por cada evento)

        return savedVenue;
    }

    /**
     * Ejemplo 2: Eliminar un Venue (cascade REMOVE)
     * 
     * Ciclo de vida:
     * 1. VenueEntity está en estado MANAGED
     * 2. Al hacer delete(), pasa a REMOVED
     * 3. Gracias a cascade=ALL, los eventos también pasan a REMOVED
     * 4. Al commit, se ejecutan los DELETE
     */
    @Transactional
    public void deleteVenueAndItsEvents(Long venueId) {
        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado"));

        // Estado: MANAGED → REMOVED
        // Gracias a cascade=ALL, todos los eventos también pasan a REMOVED
        venueRepository.delete(venue);

        // Al commit:
        // DELETE FROM events WHERE venue_id = ?
        // DELETE FROM venues WHERE id = ?
    }

    /**
     * Ejemplo 3: orphanRemoval en acción
     * 
     * Demuestra cómo orphanRemoval=true elimina automáticamente
     * eventos que se remueven de la colección del venue.
     */
    @Transactional
    public void removeEventFromVenue(Long venueId, String eventTitle) {
        // Cargar venue con sus eventos (LAZY, se cargan al acceder)
        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado"));

        // Acceder a eventos dispara la carga LAZY
        EventEntity eventToRemove = venue.getEvents().stream()
                .filter(e -> e.getTitle().equals(eventTitle))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Remover evento de la colección usando método helper
        venue.removeEvent(eventToRemove);

        // NO necesitamos llamar eventRepository.delete()
        // Gracias a orphanRemoval=true, el evento se elimina automáticamente
        venueRepository.save(venue);

        // Al commit:
        // DELETE FROM events WHERE id = ?
    }

    /**
     * Ejemplo 4: Agregar un evento a un venue existente (cascade PERSIST)
     */
    @Transactional
    public EventEntity addEventToExistingVenue(Long venueId, String eventTitle) {
        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado"));

        // Crear nuevo evento (TRANSIENT)
        EventEntity newEvent = new EventEntity();
        newEvent.setTitle(eventTitle);
        newEvent.setDescription("Nuevo evento agregado");
        newEvent.setDate(LocalDate.now().plusDays(60));

        // Agregar al venue usando método helper
        venue.addEvent(newEvent);

        // Gracias a cascade=ALL, el nuevo evento se persiste automáticamente
        venueRepository.save(venue);

        // Al commit:
        // INSERT INTO events ...

        return newEvent;
    }

    /**
     * Ejemplo 5: ManyToMany con cascade PERSIST y MERGE
     * 
     * Demuestra cómo crear un evento con categorías nuevas y existentes.
     */
    @Transactional
    public EventEntity createEventWithCategories(
            Long venueId,
            String eventTitle,
            List<String> categoryNames) {

        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado"));

        // Crear evento
        EventEntity event = new EventEntity();
        event.setTitle(eventTitle);
        event.setDescription("Evento con múltiples categorías");
        event.setDate(LocalDate.now().plusDays(45));
        event.setVenue(venue);

        // Agregar categorías (nuevas o existentes)
        for (String categoryName : categoryNames) {
            CategoryEntity category = categoryRepository
                    .findByNameIgnoreCase(categoryName)
                    .orElseGet(() -> {
                        // Si no existe, crear nueva (TRANSIENT)
                        CategoryEntity newCat = new CategoryEntity();
                        newCat.setName(categoryName);
                        newCat.setDescription("Categoría creada automáticamente");
                        return newCat;
                    });

            // Agregar categoría usando método helper
            event.addCategory(category);
        }

        // Guardar evento
        // Gracias a cascade={PERSIST, MERGE}:
        // - Categorías nuevas se persisten automáticamente
        // - Categorías existentes se actualizan si es necesario
        EventEntity savedEvent = eventRepository.save(event);

        // Al commit:
        // INSERT INTO events ...
        // INSERT INTO categories ... (solo las nuevas)
        // INSERT INTO event_categories ... (todas las relaciones)

        return savedEvent;
    }

    /**
     * Ejemplo 6: Fetch LAZY - Cargar venue sin eventos
     * 
     * Demuestra que con LAZY, los eventos NO se cargan automáticamente.
     */
    @Transactional(readOnly = true)
    public VenueEntity getVenueWithoutEvents(Long venueId) {
        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado"));

        // En este punto, venue.getEvents() NO ha ejecutado ningún SELECT
        // Los eventos solo se cargarán si accedemos a la colección

        return venue;
        // Si retornamos el venue y la transacción se cierra,
        // intentar acceder a venue.getEvents() fuera de este método
        // causará LazyInitializationException
    }

    /**
     * Ejemplo 7: Fetch LAZY - Cargar venue CON eventos
     * 
     * Demuestra cómo forzar la carga de eventos dentro de la transacción.
     */
    @Transactional(readOnly = true)
    public VenueEntity getVenueWithEvents(Long venueId) {
        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado"));

        // Forzar la carga de eventos accediendo a la colección
        venue.getEvents().size(); // Dispara SELECT de eventos

        return venue;
        // Ahora los eventos están cargados y disponibles
        // incluso fuera de la transacción
    }

    /**
     * Ejemplo 8: Eliminar categoría NO elimina eventos
     * 
     * Demuestra que al NO usar cascade en el lado inverso,
     * eliminar una categoría no afecta a los eventos.
     */
    @Transactional
    public void deleteCategoryKeepingEvents(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Eliminar categoría
        categoryRepository.delete(category);

        // Al commit:
        // DELETE FROM event_categories WHERE category_id = ?
        // DELETE FROM categories WHERE id = ?
        // Los eventos NO se eliminan porque NO hay cascade en CategoryEntity
    }

    /**
     * Ejemplo 9: Actualizar evento (merge)
     * 
     * Demuestra el uso de merge para actualizar una entidad detached.
     */
    @Transactional
    public EventEntity updateEvent(Long eventId, String newTitle, String newDescription) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Estado: MANAGED
        // Los cambios se detectan automáticamente
        event.setTitle(newTitle);
        event.setDescription(newDescription);

        // NO es necesario llamar save() porque está MANAGED
        // Al commit, se ejecuta UPDATE automáticamente

        return event;
        // Al commit:
        // UPDATE events SET title = ?, description = ? WHERE id = ?
    }

    /**
     * Ejemplo 10: Remover todas las categorías de un evento
     * 
     * Demuestra cómo limpiar una relación ManyToMany.
     */
    @Transactional
    public void removeAllCategoriesFromEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Limpiar categorías manteniendo consistencia bidireccional
        event.getCategories().forEach(category -> category.getEvents().remove(event));
        event.getCategories().clear();

        eventRepository.save(event);

        // Al commit:
        // DELETE FROM event_categories WHERE event_id = ?
        // Las categorías NO se eliminan (solo la relación)
    }
}
