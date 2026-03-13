-- Script de creación de tablas para Tech Cup Football 2026

-- Tabla Usuario
CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Email
CREATE TABLE IF NOT EXISTS email (
    id SERIAL PRIMARY KEY,
    destinatario VARCHAR(100) NOT NULL,
    asunto VARCHAR(200) NOT NULL,
    cuerpo TEXT NOT NULL,
    remitente VARCHAR(100) NOT NULL,
    enviado BOOLEAN DEFAULT FALSE,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Pago
CREATE TABLE IF NOT EXISTS pago (
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    descripcion VARCHAR(255),
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metodo_pago VARCHAR(100),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Tabla Notificacion
CREATE TABLE IF NOT EXISTS notificacion (
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    contenido TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    leido BOOLEAN DEFAULT FALSE,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_pago_usuario_id ON pago(usuario_id);
CREATE INDEX idx_pago_estado ON pago(estado);
CREATE INDEX idx_notificacion_usuario_id ON notificacion(usuario_id);
CREATE INDEX idx_notificacion_leido ON notificacion(leido);
CREATE INDEX idx_email_destinatario ON email(destinatario);
