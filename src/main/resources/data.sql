-- ======= Datos iniciales para Venue =======
INSERT INTO venues (id, name, address, capacity)
VALUES (1, 'Auditorio Riwi', 'Medellín', 150);
INSERT INTO venues (id, name, address, capacity)
VALUES (2, 'Centro de Convenciones', 'Bogotá', 500);

-- ======= Datos iniciales para Event =======
INSERT INTO events (id, title, description, date, venue_id)
VALUES (1, 'Spring Boot Workshop', 'Taller intensivo de Spring Boot', '2025-11-15', 1);
INSERT INTO events (id, title, description, date, venue_id)
VALUES (2, 'Tech Fest 2025', 'Evento de tecnología e innovación', '2025-12-01', 2);
