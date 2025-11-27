-- ======= Datos iniciales para Venue =======
INSERT INTO venues (id, name, address, capacity)
VALUES (1, 'Auditorio Riwi', 'Medellín', 150);
INSERT INTO venues (id, name, address, capacity)
VALUES (2, 'Centro de Convenciones', 'Bogotá', 500);
INSERT INTO venues (id, name, address, capacity)
VALUES (3, 'Teatro Municipal', 'Cali', 300);

-- ======= Datos iniciales para Category =======
INSERT INTO categories (id, name, description)
VALUES (1, 'Tecnología', 'Eventos relacionados con tecnología e innovación');
INSERT INTO categories (id, name, description)
VALUES (2, 'Educación', 'Talleres y cursos educativos');
INSERT INTO categories (id, name, description)
VALUES (3, 'Networking', 'Eventos de networking profesional');
INSERT INTO categories (id, name, description)
VALUES (4, 'Conferencias', 'Conferencias y charlas magistrales');

-- ======= Datos iniciales para Event =======
INSERT INTO events (id, title, description, date, venue_id)
VALUES (1, 'Spring Boot Workshop', 'Taller intensivo de Spring Boot y arquitectura hexagonal', '2025-11-15', 1);
INSERT INTO events (id, title, description, date, venue_id)
VALUES (2, 'Tech Fest 2025', 'Evento de tecnología e innovación con múltiples charlas', '2025-12-01', 2);
INSERT INTO events (id, title, description, date, venue_id)
VALUES (3, 'Java Conference', 'Conferencia anual de Java y JVM', '2025-12-15', 3);

-- ======= Relaciones ManyToMany: Event <-> Category =======
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

