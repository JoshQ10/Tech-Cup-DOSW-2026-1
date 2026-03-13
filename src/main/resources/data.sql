-- Datos iniciales para pruebas en modo desarrollo

-- Insertar usuarios de prueba
INSERT INTO usuario (nombre, email, password, activo) VALUES 
('Juan Pérez', 'juan@example.com', '$2a$10$BqPHlH.BmKj7f.VZs5kpSe7XwXlLnXj8LjGYXqXjxXZ3VjL5V9Mii', TRUE),
('María García', 'maria@example.com', '$2a$10$BqPHlH.BmKj7f.VZs5kpSe7XwXlLnXj8LjGYXqXjxXZ3VjL5V9Mii', TRUE),
('Carlos López', 'carlos@example.com', '$2a$10$BqPHlH.BmKj7f.VZs5kpSe7XwXlLnXj8LjGYXqXjxXZ3VjL5V9Mii', TRUE)
ON CONFLICT (email) DO NOTHING;

-- Insertar correos de ejemplo
INSERT INTO email (destinatario, asunto, cuerpo, remitente, enviado) VALUES 
('juan@example.com', 'Bienvenido', 'Bienvenido a Tech Cup Football', 'admin@tech-cup.com', TRUE),
('maria@example.com', 'Confirmación de Registro', 'Tu cuenta ha sido creada exitosamente', 'admin@tech-cup.com', TRUE)
ON CONFLICT DO NOTHING;
