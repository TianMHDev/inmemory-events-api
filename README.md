# 📚 Sistema de Gestión de Eventos - API REST

API REST para la gestión de eventos y venues (lugares) construida con Spring Boot siguiendo arquitectura hexagonal y buenas prácticas de desarrollo.

## ✨ Características Principales

### 🎯 Tarea Semana 5: Bean Validation Avanzada y Manejo Global de Errores

#### 1. **Bean Validation Avanzada**
- ✅ **Validaciones Cruzadas**: Anotación personalizada `@ValidDateRange` que valida que `startDate < endDate`
- ✅ **Grupos de Validación**: Diferenciación entre operaciones de creación (`OnCreate`) y actualización (`OnUpdate`)
- ✅ **Mensajes Personalizados**: Mensajes de error definidos en `messages.properties`

#### 2. **Manejador Global de Excepciones**
- ✅ `@RestControllerAdvice` centraliza el manejo de errores
- ✅ Captura excepciones de validación, entidades no encontradas, violaciones de datos
- ✅ Respuestas estandarizadas en formato RFC 7807 (ProblemDetail)

#### 3. **Formato de Respuesta Estandarizado**
Todas las respuestas de error incluyen:
- `type`: URI que identifica el tipo de error
- `title`: Título del error
- `status`: Código HTTP
- `detail`: Descripción detallada
- `instance`: Ruta de la solicitud
- `timestamp`: Marca temporal (custom)
- `traceId`: ID único para rastreo (custom)

## 🏗️ Arquitectura

El proyecto sigue **Arquitectura Hexagonal** (Ports and Adapters):

```
src/main/java/com/example/inmemory_events_api/
├── dominio/                    # 🟢 CAPA DE DOMINIO (núcleo)
│   ├── model/                  # Modelos de dominio puros (DTOs)
│   └── ports/                  # Interfaces de puertos
│
├── aplicacion/                 # 🔵 CAPA DE APLICACIÓN
│   └── usecase/                # Casos de uso / Servicios de aplicación
│
└── infraestructura/            # 🟡 CAPA DE INFRAESTRUCTURA
    └── adapters/
        ├── in/                 # Adaptadores de entrada
        │   └── web/            # Controllers REST, DTOs de entrada
        │       ├── dto/        # Data Transfer Objects
        │       ├── validation/ # Validaciones personalizadas
        │       └── exception/  # Manejo de excepciones
        │
        └── out/                # Adaptadores de salida
            └── jpa/            # Persistencia con JPA
                ├── entity/     # Entidades JPA
                └── repository/ # Repositorios
```

### 📦 Beneficios de esta Arquitectura

- **Independencia del Framework**: El dominio no depende de Spring, JPA, etc.
- **Facilidad de Testing**: Cada capa se puede testear independientemente
- ** Flexibilidad**: Fácil cambiar implementaciones (ej: de MySQL a PostgreSQL)
- **Mantenibilidad**: Responsabilidades claramente separadas

## 🚀 Guía de Uso

### Prerrequisitos

- Java 17+
- Maven 3.6+
- MySQL 8.0 (o modificar configuración para otra BD)

### Configuración

1. **Base de Datos**: Modificar `src/main/resources/application.properties`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/inmemory_events
   spring.datasource.username=root
   spring.datasource.password=tu_password
   ```

2. **Iniciar la aplicación**:
   ```bash
   mvn spring-boot:run
   ```

La aplicación estará disponible en: `http://localhost:8081`

## 📋 Validaciones Implementadas

### Validaciones de Creación (`OnCreate`)

| Campo | Validación | Mensaje |
|-------|------------|---------|
| `name` | `@NotBlank` | "El nombre del evento es obligatorio" |
| `venueId` | `@NotNull` | "El ID del lugar es obligatorio" |
| `startDate` | `@NotNull` | "La fecha de inicio es obligatoria" |
| `endDate` | `@NotNull` | "La fecha de fin es obligatoria" |
| `class-level` | `@ValidDateRange` | "La fecha de inicio debe ser anterior a la fecha de fin" |

### Validaciones de Actualización (`OnUpdate`)

| Campo | Validación | Mensaje |
|-------|------------|---------|
| `name` | `@NotBlank` | "El nombre del evento es obligatorio" |
| `venueId` | - | **Opcional en actualización** |

## 🔧 Código Limpio y Comentado

Todos los archivos clave incluyen comentarios Javadoc detallados:

### ✅ Clases Comentadas

- **GlobalExceptionHandler**: Explica cada tipo de excepción y cómo se maneja
- **EventRequestDTO**: Documenta cada validación y su propósito
- **EventController**: Describe cada endpoint y su comportamiento
- **ValidDateRange**: Explica la validación cruzada de fechas
- **DateRangeValidator**: Detalla la lógica de validación
- **OnCreate/OnUpdate**: Documentan los grupos de validación

## 📚 Recursos Adicionales

- [Documentación completa de la tarea](./TAREA_SEMANA5.md)
- [Guía de reseteo de BD](./RESET_DATABASE.md)
- [RFC 7807 - Problem Details](https://datatracker.ietf.org/doc/html/rfc7807)
-[Bean Validation 3.0](https://beanvalidation.org/3.0/)

## 👨‍💻 Autor

Sebastian - Semana 5: Bean Validation Avanzada y Manejo Global de Errores

---

**¿Preguntas?** Revisa los comentarios en el código - cada clase tiene documentación Javadoc detallada explicando su propósito y funcionamiento.