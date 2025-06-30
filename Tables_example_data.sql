-- ========================
-- Datos de ejemplo
-- ========================

-- 1. Empresas
INSERT INTO Empresa (ruc, razon_social, direccion, telefono, estado_empresa) VALUES
('20123456789', 'Soluciones Informáticas S.A.', 'Av. Siempre Viva 123, Lima', '01-2345678', 'A'),
('20567891234', 'Servicios Globales EIRL', 'Jr. Los Olivos 456, Lima', '01-8765432', 'A'),
('20234567891', 'Comercial Tech SAC', 'Calle Falsa 789, Lima', '01-1122334', 'I');

-- 2. Aplicaciones
INSERT INTO Aplicacion (id_empresa, tipo_aplicacion) VALUES
(1, 'Portal Cliente'),
(1, 'App Móvil iOS'),
(2, 'Portal Administrativo'),
(3, 'API Rest');

-- 3. Clientes
INSERT INTO Cliente (nombre_cliente, apellido_cliente, email_cliente, password_cliente, tipo_cliente, estado_cliente, id_empresa) VALUES
('Ana', 'Gómez', 'ana.gomez@example.com', 'hashed_pw_1', 'N', 'A', 1),
('Empresa XYZ', '', 'contacto@xyz.com.pe', 'hashed_pw_2', 'J', 'A', 2),
('Luis', 'Pérez', 'luis.perez@example.com', 'hashed_pw_3', 'N', 'I', NULL),
('Beta SAC', '', 'ventas@beta.com.pe', 'hashed_pw_4', 'J', 'A', 3);

-- 4. Tipos de Solicitud
INSERT INTO tipoSolicitud (tipoSolicitud, descripcion) VALUES
('E', 'Reporte de Error de Sistema'),
('C', 'Solicitud de Capacitación'),
('R', 'Requerimiento de Nueva Funcionalidad');

-- 5. Roles
INSERT INTO Rol (nombre_rol) VALUES
('Soporte Nivel 1'),
('Soporte Nivel 2'),
('Coordinador'),
('Desarrollador');

-- 6. Colaboradores
INSERT INTO Colaborador (nombre_colab, apellido_colab, email_colab, password_colab, id_rol, estado_colab) VALUES
('María', 'Fernández', 'maria.fernandez@empresa.com', 'hashed_col_pw1', 1, TRUE),
('José', 'Ramírez', 'jose.ramirez@empresa.com', 'hashed_col_pw2', 2, TRUE),
('Carla', 'Sánchez', 'carla.sanchez@empresa.com', 'hashed_col_pw3', 3, TRUE),
('Diego', 'Vargas', 'diego.vargas@empresa.com', 'hashed_col_pw4', 4, FALSE);

-- 7. Solicitudes
-- (Cliente 1 reporta un error en Portal Cliente, alta prioridad; Cliente 2 pide capacitación; Cliente 4 solicita funcionalidad)
INSERT INTO Solicitud (id_cliente, id_tipoSolicitud, id_aplicacion, asunto, motivo, fecha_creacion, estado, prioridad) VALUES
(1, 1, 1,
 'Error al iniciar sesión',
 'Al intentar iniciar sesión, recibe mensaje de "Error 500".',
 '2025-05-20 09:15:00', 'A', 'A'),
(2, 2, 3,
 'Capacitación módulo reportes',
 'Solicito sesión de capacitación para el módulo de reportes avanzados.',
 '2025-05-18 14:30:00', 'B', 'M'),
(4, 3, 4,
 'Integración con sistema contable',
 'Se requiere generar API para exportar datos al ERP contable.',
 '2025-05-22 11:45:00', 'C', 'B'),
(1, 1, 2,
 'Falla en notificaciones push',
 'No llegan notificaciones en la app iOS.',
 '2025-05-23 16:00:00', 'P', 'M');

-- 8. Asignaciones
-- (María atiende solicitud 1; José coordina la 2; Carla atiende 3; María revisa 4)
INSERT INTO Asignacion (id_solicitud, id_colaborador, fecha_asignacion, es_coordinador) VALUES
(1, 1, '2025-05-20 09:30:00', FALSE),
(2, 2, '2025-05-18 15:00:00', TRUE),
(3, 3, '2025-05-22 12:00:00', FALSE),
(4, 1, '2025-05-23 16:15:00', FALSE);

-- 9. Actividades
-- (Actividades y tiempos de trabajo en cada asignación)
INSERT INTO Actividad (id_asignacion, descripcion, inicio, fin, es_final) VALUES
(1, 'Revisar logs del servidor', '2025-05-20 09:35:00', '2025-05-20 10:15:00', FALSE),
(1, 'Reiniciar servicio de autenticación', '2025-05-20 10:20:00', '2025-05-20 10:45:00', FALSE),
(1, 'Confirmar solución con cliente', '2025-05-20 10:50:00', '2025-05-20 11:00:00', TRUE),
(2, 'Programar agenda de capacitación', '2025-05-18 15:10:00', '2025-05-18 16:00:00', TRUE),
(3, 'Definir endpoint REST', '2025-05-22 12:10:00', '2025-05-22 14:00:00', FALSE),
(3, 'Desarrollar pruebas unitarias', '2025-05-22 14:10:00', '2025-05-22 15:30:00', TRUE),
(4, 'Verificar configuración de notificaciones', '2025-05-23 16:20:00', '2025-05-23 17:00:00', TRUE);
