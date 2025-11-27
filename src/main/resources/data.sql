-- ========== Datos iniciales para Venues ==========
INSERT INTO venues (id, name, address, city, capacity)
VALUES (1, 'Auditorio Riwi', 'Calle 50 #45-23', 'Medellín', 150);

INSERT INTO venues (id, name, address, city, capacity)
VALUES (2, 'Centro de Convenciones', 'Carrera 7 #24-89', 'Bogotá', 500);

INSERT INTO venues (id, name, address, city, capacity)
VALUES (3, 'Teatro Municipal', 'Avenida Colombia #8-12', 'Cali', 300);

INSERT INTO venues (id, name, address, city, capacity)
VALUES (4, 'Salón de Eventos Plaza', 'Calle 10 #15-30', 'Medellín', 80);

INSERT INTO venues (id, name, address, city, capacity)
VALUES (5, 'Auditorio Universidad', 'Campus Norte', 'Bogotá', 200);

-- ========== Datos iniciales para Categories ==========
INSERT INTO categories (id, name, description)
VALUES (1, 'Tecnología', 'Eventos relacionados con tecnología e innovación');

INSERT INTO categories (id, name, description)
VALUES (2, 'Educación', 'Talleres y cursos educativos');

INSERT INTO categories (id, name, description)
VALUES (3, 'Networking', 'Eventos de networking profesional');

INSERT INTO categories (id, name, description)
VALUES (4, 'Conferencias', 'Conferencias y charlas magistrales');

INSERT INTO categories (id, name, description)
VALUES (5, 'Deportes', 'Eventos deportivos y competencias');

-- ========== Datos iniciales para Events ==========
-- Eventos futuros activos
INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (1, 'Spring Boot Workshop', 'Taller intensivo de Spring Boot y arquitectura hexagonal', '2025-12-15', 'Tecnología', 'ACTIVO', 1);

INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (2, 'Tech Fest 2025', 'Evento de tecnología e innovación con múltiples charlas', '2025-12-20', 'Tecnología', 'ACTIVO', 2);

INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (3, 'Java Conference', 'Conferencia anual de Java y JVM', '2026-01-10', 'Tecnología', 'ACTIVO', 3);

INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (4, 'Networking Night', 'Noche de networking para profesionales de TI', '2025-12-05', 'Networking', 'ACTIVO', 4);

INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (5, 'Curso de React', 'Curso completo de React y Next.js', '2026-01-15', 'Educación', 'ACTIVO', 1);

-- Eventos cancelados
INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (6, 'Angular Workshop', 'Taller de Angular cancelado por baja asistencia', '2025-11-30', 'Tecnología', 'CANCELADO', 2);

-- Eventos pasados finalizados
INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (7, 'DevOps Summit 2024', 'Cumbre de DevOps ya finalizada', '2024-10-15', 'Tecnología', 'FINALIZADO', 3);

INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (8, 'Hackathon 2024', 'Hackathon de 48 horas finalizado', '2024-09-20', 'Tecnología', 'FINALIZADO', 2);

-- Más eventos futuros
INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (9, 'Maratón Programación', 'Maratón de programación competitiva', '2026-02-01', 'Deportes', 'ACTIVO', 5);

INSERT INTO events (id, title, description, date, category, status, venue_id)
VALUES (10, 'Charla Inteligencia Artificial', 'Charla sobre IA y Machine Learning', '2025-12-28', 'Conferencias', 'ACTIVO', 1);

-- ========== Relaciones ManyToMany: Event <-> Category ==========
-- Spring Boot Workshop: Tecnología + Educación
INSERT INTO event_categories (event_id, category_id) VALUES (1, 1);
INSERT INTO event_categories (event_id, category_id) VALUES (1, 2);

-- Tech Fest 2025: Tecnología + Networking + Conferencias
INSERT INTO event_categories (event_id, category_id) VALUES (2, 1);
INSERT INTO event_categories (event_id, category_id) VALUES (2, 3);
INSERT INTO event_categories (event_id, category_id) VALUES (2, 4);

-- Java Conference: Tecnología + Conferencias
INSERT INTO event_categories (event_id, category_id) VALUES (3, 1);
INSERT INTO event_categories (event_id, category_id) VALUES (3, 4);

-- Networking Night: Networking
INSERT INTO event_categories (event_id, category_id) VALUES (4, 3);

-- Curso de React: Tecnología + Educación
INSERT INTO event_categories (event_id, category_id) VALUES (5, 1);
INSERT INTO event_categories (event_id, category_id) VALUES (5, 2);

-- Angular Workshop: Tecnología + Educación
INSERT INTO event_categories (event_id, category_id) VALUES (6, 1);
INSERT INTO event_categories (event_id, category_id) VALUES (6, 2);

-- DevOps Summit: Tecnología + Conferencias
INSERT INTO event_categories (event_id, category_id) VALUES (7, 1);
INSERT INTO event_categories (event_id, category_id) VALUES (7, 4);

-- Hackathon: Tecnología + Deportes
INSERT INTO event_categories (event_id, category_id) VALUES (8, 1);
INSERT INTO event_categories (event_id, category_id) VALUES (8, 5);

-- Maratón: Deportes
INSERT INTO event_categories (event_id, category_id) VALUES (9, 5);

-- Charla IA: Tecnología + Conferencias
INSERT INTO event_categories (event_id, category_id) VALUES (10, 1);
INSERT INTO event_categories (event_id, category_id) VALUES (10, 4);
