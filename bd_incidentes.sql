DROP DATABASE IF EXISTS bd_incidentes;
CREATE DATABASE bd_incidentes;
USE bd_incidentes;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

-- Tabla: rol
CREATE TABLE rol (
  id_rol INT(11) NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabla: usuario
CREATE TABLE usuario (
  id_usuario INT(11) NOT NULL AUTO_INCREMENT,
  nombres VARCHAR(100) DEFAULT NULL,
  apellidos VARCHAR(100) DEFAULT NULL,
  telefono CHAR(15) DEFAULT NULL,
  correo VARCHAR(100) DEFAULT NULL,
  contrasena VARCHAR(255) DEFAULT NULL,
  estado INT ,
  id_rol INT(11) DEFAULT NULL,
  PRIMARY KEY (id_usuario),
  UNIQUE KEY correo (correo),
  KEY fk_usuario_rol (id_rol),
  CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol (id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabla: estado_incidente
CREATE TABLE estado_incidente (
  id_estado INT(11) NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (id_estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabla: incidente
CREATE TABLE incidente (
  id_incidente INT(11) NOT NULL AUTO_INCREMENT,
  titulo VARCHAR(100) DEFAULT NULL,
  descripcion TEXT DEFAULT NULL,
  fecha_creacion DATETIME DEFAULT current_timestamp(),
  solucion TEXT NULL,
  fecha_solucion DATETIME DEFAULT NULL,
  prioridad ENUM('Alta','Media','Baja') DEFAULT NULL,
  id_estado INT(11) DEFAULT NULL,
  id_cliente INT(11) DEFAULT NULL,
  id_tecnico INT(11) DEFAULT NULL,
  PRIMARY KEY (id_incidente),
  KEY fk_incidente_estado (id_estado),
  KEY fk_incidente_cliente (id_cliente),
  KEY fk_incidente_tecnico (id_tecnico),
  CONSTRAINT fk_incidente_estado FOREIGN KEY (id_estado) REFERENCES estado_incidente (id_estado),
  CONSTRAINT fk_incidente_cliente FOREIGN KEY (id_cliente) REFERENCES usuario (id_usuario),
  CONSTRAINT fk_incidente_tecnico FOREIGN KEY (id_tecnico) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabla: bitacora_estado
CREATE TABLE bitacora_estado (
  id_bitacora INT(11) NOT NULL AUTO_INCREMENT,
  id_incidente INT(11) NOT NULL,
  id_estado INT(11) NOT NULL,
  id_usuario INT(11) NOT NULL,
  fecha DATETIME DEFAULT current_timestamp(),
  PRIMARY KEY (id_bitacora),
  KEY fk_bitacora_incidente (id_incidente),
  KEY fk_bitacora_estado (id_estado),
  KEY fk_bitacora_usuario (id_usuario),
  CONSTRAINT fk_bitacora_incidente FOREIGN KEY (id_incidente) REFERENCES incidente (id_incidente),
  CONSTRAINT fk_bitacora_estado FOREIGN KEY (id_estado) REFERENCES estado_incidente (id_estado),
  CONSTRAINT fk_bitacora_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO rol (id_rol, nombre) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_TECNICO'),
(3, 'ROLE_CLIENTE');

INSERT INTO usuario (nombres, apellidos, telefono, correo, contrasena, id_rol, estado) VALUES
('Erick', 'García Conde', '987654321', 'egarcia@admin.com', '$2a$10$2HRl0yiax2V/4ixERmKtWOHmZRMxl.GbNI9SKDFkLgUp.wzd4cfVW', 1,1),
('Luis', 'Mendoza Torres', '912345678', 'lmendoza@tecnico.com', '$2a$10$sDHNKA1VASmmRaHT02jxBuZ0MxTjrpr.6QsG5al/rOBpbtnkJxk5i', 2,1),
('Ana', 'Ramírez Soto', '916789234', 'aramirez@cliente.com', '$2a$10$fMKYWekshcrlPgt8bz15ae5uxhafYOGlRAvW/IQCnV4djqe8w15Z2', 3,1),
('María', 'Pérez Gómez', '987000111', 'mperez@cliente.com', '$2a$10$x7iXWpVtP4U29SaEZvflBetAH.358mlKx.hoMbQu8LQKzWHDvDX3.', 3,1),
('Carlos', 'Vásquez León', '922333444', 'cvasquez@tecnico.com', '$2a$10$mM8GVLj8JvKRrGs/k5o0muFp7/TKTbVSihXs4BsEp/AoN7cf1cxMq', 2,1),
('Lucía', 'Flores Chávez', '955667788', 'lflores@cliente.com', '$2a$10$6i.Xd2wkcccBFSLlEpXp4OVItLa.6TsyMZxelbUjTCHlJEhzJuM7q', 3,1),
('Jorge', 'Reyes Díaz', '933111222', 'jreyes@tecnico.com', '$2a$10$/LC9QPdxmFWHcmbEhd4YDOiHyMXwT6Gg2fcRWBOItGLI3HlfrY6XO', 2,1),
('Valeria', 'Quispe Rojas', '944999888', 'vquispe@cliente.com', '$2a$10$8TVzv5IR78q0NplhLtiF7emSGKbzZjqSeytOjHHGFzxZ.zRFldYN2', 3,1),
('Miguel', 'Torres Gutiérrez', '911223344', 'mtorres@admin.com', '$2a$10$Chpn2lwJLZHSPQJDYx78wuwCGxxjVybe65Sz6.7aq5jdMGFxM6obW', 1,1),
('Diana', 'Silva Cabrera', '922887766', 'dsilva@cliente.com', '$2a$10$7MjrOAbHeVaBE7mkc8JFHOmoCSiGoukW2i1pVIk4tvvbyI/FlqXau', 3,1);

INSERT INTO estado_incidente (id_estado, nombre) VALUES
(1, 'Pendiente'),
(2, 'En Proceso'),
(3, 'Resuelto');


