# 🎯 InMemory Events API - Semana 4: Relaciones y Ciclo de Vida de Entidades

API REST para gestión de eventos y venues (recintos) implementada con **Spring Boot 3.3.x**, **JPA/Hibernate**, y **Arquitectura Hexagonal**.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Relaciones JPA Implementadas](#-relaciones-jpa-implementadas)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Endpoints API](#-endpoints-api)
- [Documentación Adicional](#-documentación-adicional)

---

## ✨ Características

### Semana 4: Relaciones y Ciclo de Vida de Entidades

✅ **Relación OneToMany / ManyToOne**: Venue ↔ Event
- Un Venue puede tener múltiples eventos
- Un Evento pertenece a un solo Venue
- Configuración completa de `cascade`, `orphanRemoval`, y `fetch LAZY`

✅ **Relación ManyToMany**: Event ↔ Category
- Un Evento puede tener múltiples categorías
- Una Categoría puede estar en múltiples eventos
- Tabla intermedia `event_categories`

✅ **Estrategias de Carga**
- **LAZY loading** por defecto para mejor rendimiento
- Evita el problema N+1 queries
- Documentación de cuándo usar EAGER vs LAZY

✅ **Ciclo de Vida de Entidades**
- Estados: Transient, Managed, Detached, Removed
- Operaciones: persist, merge, remove, detach, refresh
- Ejemplos prácticos en `EntityLifecycleExampleService`

✅ **Métodos Helper Bidireccionales**
- `venue.addEvent(event)` / `venue.removeEvent(event)`
- `event.addCategory(category)` / `event.removeCategory(category)`
- Mantienen consistencia automática en ambos lados de la relación

### Características Previas

✅ CRUD completo para Eventos y Venues  
✅ Validaciones con Jakarta Validation  
✅ Manejo global de excepciones  
✅ Arquitectura Hexagonal (Puertos y Adaptadores)  
✅ MapStruct para mapeo entre DTOs y Entidades  
✅ Base de datos H2 en memoria  
✅ Documentación OpenAPI/Swagger  

---

## 🔗 Relaciones JPA Implementadas

### 1. OneToMany / ManyToOne: Venue ↔ Event

```java
// VenueEntity
@OneToMany(
    mappedBy = "venue",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
)
private List<EventEntity> events = new ArrayList<>();

// EventEntity
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "venue_id", nullable = false)
private VenueEntity venue;
```

**Características:**
- **cascade = ALL**: Crear/eliminar venue propaga a eventos
- **orphanRemoval = true**: Remover evento de la lista lo elimina de BD
- **fetch = LAZY**: Eventos solo se cargan cuando se accede a ellos
- **optional = false**: Un evento DEBE tener un venue

### 2. ManyToMany: Event ↔ Category

```java
// EventEntity (lado dueño)
@ManyToMany(
    cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    fetch = FetchType.LAZY
)
@JoinTable(
    name = "event_categories",
    joinColumns = @JoinColumn(name = "event_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id")
)
private List<CategoryEntity> categories = new ArrayList<>();

// CategoryEntity (lado inverso)
@ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
private List<EventEntity> events = new ArrayList<>();
```

**Características:**
- **cascade = PERSIST, MERGE**: Solo crear/actualizar, NO eliminar
- **fetch = LAZY**: Categorías se cargan solo cuando se necesitan
- **Tabla intermedia**: `event_categories` con FKs a ambas tablas

### Diagrama de Relaciones

```
┌─────────────┐         ┌─────────────┐         ┌──────────────┐
│   Venue     │1      *│    Event    │*      *│   Category   │
│─────────────│◄────────│─────────────│◄────────│──────────────│
│ id          │         │ id          │         │ id           │
│ name        │         │ title       │         │ name         │
│ address     │         │ description │         │ description  │
│ capacity    │         │ date        │         └──────────────┘
└─────────────┘         │ venue_id FK │
                        └─────────────┘
                               │
                               │ event_categories
                               │ (event_id, category_id)
```

---

## 🏗️ Arquitectura

El proyecto sigue **Arquitectura Hexagonal** (Puertos y Adaptadores):

```
src/main/java/com/example/inmemory_events_api/
│
├── dominio/                          # Capa de Dominio (núcleo)
│   ├── model/                        # Modelos de dominio (DTOs)
│   │   ├── EventDTO.java
│   │   └── VenueDTO.java
│   └── ports/                        # Interfaces (contratos)
│       ├── in/                       # Puertos de entrada (use cases)
│       │   ├── event/
│       │   │   ├── CrearEventoUseCase.java
│       │   │   ├── ActualizarEventoUseCase.java
│       │   │   └── ...
│       │   └── venue/
│       │       ├── CrearVenueUseCase.java
│       │       └── ...
│       └── out/                      # Puertos de salida (repositorios)
│           ├── EventRepositoryPort.java
│           └── VenueRepositoryPort.java
│
├── aplicacion/                       # Capa de Aplicación (lógica de negocio)
│   └── usecase/
│       ├── EventService.java         # Implementa use cases de Event
│       ├── VenueService.java         # Implementa use cases de Venue
│       └── EntityLifecycleExampleService.java  # Ejemplos de ciclo de vida
│
└── infraestructura/                  # Capa de Infraestructura (detalles técnicos)
    ├── adapters/
    │   ├── in/                       # Adaptadores de entrada
    │   │   └── web/
    │   │       ├── EventController.java
    │   │       ├── VenueController.java
    │   │       ├── dto/              # DTOs de la API REST
    │   │       └── exception/        # Manejo de excepciones
    │   └── out/                      # Adaptadores de salida
    │       └── jpa/
    │           ├── entity/           # Entidades JPA
    │           │   ├── EventEntity.java
    │           │   ├── VenueEntity.java
    │           │   └── CategoryEntity.java
    │           ├── mapper/           # MapStruct mappers
    │           │   ├── EventMapper.java
    │           │   └── VenueMapper.java
    │           ├── EventRepository.java
    │           ├── VenueRepository.java
    │           ├── CategoryRepository.java
    │           ├── EventJpaAdapter.java
    │           └── VenueJpaAdapter.java
    └── config/
        └── OpenApiConfig.java        # Configuración Swagger
```

---

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.3.x**
- **Spring Data JPA** - Persistencia
- **Hibernate** - ORM
- **H2 Database** - Base de datos en memoria
- **MapStruct 1.5.5** - Mapeo de objetos
- **Lombok** - Reducción de boilerplate
- **Jakarta Validation** - Validaciones
- **SpringDoc OpenAPI 2.6.0** - Documentación API

---

## 🚀 Instalación y Ejecución

### Prerrequisitos

- Java 17 o superior
- Maven 3.8+

### Pasos

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd inmemory-events-api
   ```

2. **Compilar el proyecto**
   ```bash
   ./mvnw clean compile
   ```

3. **Ejecutar la aplicación**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acceder a la documentación Swagger**
   ```
   http://localhost:8080/swagger-ui.html
   ```

5. **Acceder a la consola H2** (opcional)
   ```
   http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:testdb
   Username: sa
   Password: (dejar vacío)
   ```

---

## 📡 Endpoints API

### Venues

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/venues` | Listar todos los venues |
| GET | `/venues/{id}` | Obtener venue por ID |
| POST | `/venues` | Crear nuevo venue |
| PUT | `/venues/{id}` | Actualizar venue |
| DELETE | `/venues/{id}` | Eliminar venue (y sus eventos) |

### Events

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/events` | Listar todos los eventos |
| GET | `/api/events/{id}` | Obtener evento por ID |
| POST | `/api/events` | Crear nuevo evento |
| PUT | `/api/events/{id}` | Actualizar evento |
| DELETE | `/api/events/{id}` | Eliminar evento |

### Ejemplos de Request Body

**Crear Venue:**
```json
{
  "name": "Auditorio Central",
  "location": "Medellín, Colombia"
}
```

**Crear Event:**
```json
{
  "name": "Spring Boot Workshop",
  "venueId": 1,
  "date": "2025-12-15"
}
```

---

## 📚 Documentación Adicional

### Archivos de Documentación

- **[RELACIONES_JPA.md](./RELACIONES_JPA.md)** - Guía completa sobre:
  - Configuración de relaciones OneToMany, ManyToOne, ManyToMany
  - Ciclo de vida de entidades (Transient, Managed, Detached, Removed)
  - Estrategias de carga LAZY vs EAGER
  - Uso de cascade y orphanRemoval
  - Ejemplos prácticos y mejores prácticas

### Código de Ejemplo

- **`EntityLifecycleExampleService.java`** - 10 ejemplos prácticos de:
  - Crear venue con eventos (cascade persist)
  - Eliminar venue (cascade remove)
  - orphanRemoval en acción
  - Relaciones ManyToMany
  - Fetch LAZY
  - Actualización de entidades (merge)

---

## 🧪 Datos de Prueba

La aplicación se inicializa con datos de ejemplo en `data.sql`:

**Venues:**
- Auditorio Riwi (Medellín)
- Centro de Convenciones (Bogotá)
- Teatro Municipal (Cali)

**Categories:**
- Tecnología
- Educación
- Networking
- Conferencias

**Events:**
- Spring Boot Workshop (Tecnología + Educación)
- Tech Fest 2025 (Tecnología + Networking + Conferencias)
- Java Conference (Tecnología + Conferencias)

---

## 🎓 Conceptos Clave Aprendidos

### 1. Relaciones JPA
- ✅ OneToMany / ManyToOne bidireccional
- ✅ ManyToMany con tabla intermedia
- ✅ Configuración de `mappedBy`, `cascade`, `orphanRemoval`

### 2. Ciclo de Vida de Entidades
- ✅ Estados: Transient → Managed → Detached → Removed
- ✅ Operaciones: persist, merge, remove, detach, refresh
- ✅ Impacto en transacciones

### 3. Estrategias de Carga
- ✅ LAZY: Carga diferida (mejor rendimiento)
- ✅ EAGER: Carga inmediata (usar con cuidado)
- ✅ Solución a LazyInitializationException

### 4. Mejores Prácticas
- ✅ Métodos helper bidireccionales
- ✅ Inicialización de colecciones
- ✅ @Transactional en servicios
- ✅ @JsonIgnore para evitar ciclos
- ✅ Cascade selectivo según necesidad

---

## 👨‍💻 Autor

**DevSebastian**  
Email: dev.sebastian@example.com

---

## 📄 Licencia

Apache 2.0