-- ========================
-- TIPOS ENUM
-- ========================
-- Estado de Empresa y Cliente
-- A: Activo, I: Inactivo
CREATE TYPE estado_enum AS ENUM ('A', 'I');

-- Tipo de cliente (N: Natural, J: Jurídica)
CREATE TYPE tipo_cliente_enum AS ENUM ('N', 'J');

-- Estado de la solicitud
-- A: Abierto, B: Asignado, C: Programado, P: Pendiente, S: Solucionado, N: Anulado
CREATE TYPE estado_solicitud_enum AS ENUM ('A', 'B', 'C', 'P', 'N', 'S');

-- Prioridad de la solicitud
-- B: Baja, M: Media, A: Alta
CREATE TYPE prioridad_enum AS ENUM ('B', 'M', 'A');

-- Tipo de solicitud
-- E: Error, C: Capacitación, R: Requerimiento
CREATE TYPE tipo_solicitud_enum AS ENUM ('E', 'C', 'R');

-- ========================
-- TABLAS
-- ========================

-- Tabla: Empresa
CREATE TABLE Empresa (
    id_empresa SERIAL PRIMARY KEY,
    ruc VARCHAR(11) NOT NULL,
    razon_social VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    estado_empresa estado_enum DEFAULT 'A'
);

-- Tabla: Aplicacion
CREATE TABLE Aplicacion (
    id_aplicacion SERIAL PRIMARY KEY,
    id_empresa INT NOT NULL,
    tipo_aplicacion VARCHAR(50),
    FOREIGN KEY (id_empresa) REFERENCES Empresa(id_empresa) ON DELETE RESTRICT
);

-- Tabla: Cliente
CREATE TABLE Cliente (
    id_cliente SERIAL PRIMARY KEY,
    nombre_cliente VARCHAR(50) NOT NULL,
    apellido_cliente VARCHAR(50) NOT NULL,
    email_cliente VARCHAR(100) UNIQUE NOT NULL,
    password_cliente VARCHAR(255) NOT NULL,
    tipo_cliente tipo_cliente_enum NOT NULL,
    estado_cliente estado_enum DEFAULT 'A',
    id_empresa INT,
    FOREIGN KEY (id_empresa) REFERENCES Empresa(id_empresa) ON DELETE RESTRICT
);

-- Tabla: tipoSolicitud
CREATE TABLE tipoSolicitud (
    id_tipoSolicitud SERIAL PRIMARY KEY,
    tipoSolicitud tipo_solicitud_enum NOT NULL,
    descripcion VARCHAR(100)
);

-- Tabla: Solicitud
CREATE TABLE Solicitud (
    id_solicitud SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_tipoSolicitud INT NOT NULL,
    id_aplicacion INT,
    asunto VARCHAR(100),
    motivo VARCHAR(500),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado estado_solicitud_enum DEFAULT 'A',
    prioridad prioridad_enum DEFAULT 'B',
    fecha_cierre TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE RESTRICT,
    FOREIGN KEY (id_tipoSolicitud) REFERENCES tipoSolicitud(id_tipoSolicitud),
    FOREIGN KEY (id_aplicacion) REFERENCES Aplicacion(id_aplicacion) ON DELETE SET NULL
);

-- Tabla: Rol
CREATE TABLE Rol (
    id_rol SERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50)
);

-- Tabla: Colaborador
CREATE TABLE Colaborador (
    id_colaborador SERIAL PRIMARY KEY,
    nombre_colab VARCHAR(50),
    apellido_colab VARCHAR(50),
    email_colab VARCHAR(100) UNIQUE NOT NULL,
    password_colab VARCHAR(255) NOT NULL,
    id_rol INT,
    estado_colab BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES Rol(id_rol)
);

-- Tabla: Asignacion
CREATE TABLE Asignacion (
    id_asignacion SERIAL PRIMARY KEY,
    id_solicitud INT NOT NULL,
    id_colaborador INT,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    es_coordinador BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_solicitud) REFERENCES Solicitud(id_solicitud) ON DELETE RESTRICT,
    FOREIGN KEY (id_colaborador) REFERENCES Colaborador(id_colaborador) ON DELETE SET NULL
);

-- Tabla: Actividad
CREATE TABLE Actividad (
    id_actividad SERIAL PRIMARY KEY,
    id_asignacion INT NOT NULL,
    descripcion TEXT,
    inicio TIMESTAMP,
    fin TIMESTAMP,
    es_final BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_asignacion) REFERENCES Asignacion(id_asignacion) ON DELETE RESTRICT
);
