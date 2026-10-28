-- =============================================================
-- Proyecto formativo: CV Builder - Generador de hojas de vida
-- Script de la base de datos (modelo físico) - MySQL 8 / MariaDB
-- Autor: Iván Gerardo Rodríguez Bustamante - ADSO ficha 3186596
-- =============================================================
SET NAMES utf8mb4;
DROP DATABASE IF EXISTS cv_builder;
CREATE DATABASE cv_builder CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE cv_builder;

-- Usuarios que se registran en la aplicación
CREATE TABLE usuario (
  id_usuario      INT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre_usuario  VARCHAR(60)  NOT NULL,
  correo          VARCHAR(120) NOT NULL,
  contrasena_hash VARCHAR(255) NOT NULL,
  fecha_registro  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado          ENUM('Activo','Inactivo') NOT NULL DEFAULT 'Activo',
  CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
  CONSTRAINT uq_usuario_correo UNIQUE (correo)
) ENGINE=InnoDB;

-- Datos personales de la hoja de vida (uno por usuario)
CREATE TABLE perfil (
  id_perfil          INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_usuario         INT UNSIGNED NOT NULL,
  nombres            VARCHAR(60)  NOT NULL,
  apellidos          VARCHAR(60)  NOT NULL,
  tipo_documento     ENUM('CC','TI','CE','PPT','Pasaporte') NOT NULL DEFAULT 'CC',
  numero_documento   VARCHAR(20)  NOT NULL,
  fecha_nacimiento   DATE         NULL,
  telefono           VARCHAR(15)  NOT NULL,
  ciudad             VARCHAR(60)  NOT NULL,
  direccion          VARCHAR(120) NULL,
  perfil_profesional TEXT         NULL,
  foto_url           VARCHAR(255) NULL,
  CONSTRAINT pk_perfil PRIMARY KEY (id_perfil),
  CONSTRAINT uq_perfil_usuario UNIQUE (id_usuario),
  CONSTRAINT uq_perfil_documento UNIQUE (tipo_documento, numero_documento),
  CONSTRAINT fk_perfil_usuario FOREIGN KEY (id_usuario)
    REFERENCES usuario (id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Formación académica
CREATE TABLE formacion (
  id_formacion INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_perfil    INT UNSIGNED NOT NULL,
  institucion  VARCHAR(100) NOT NULL,
  titulo       VARCHAR(100) NOT NULL,
  nivel        ENUM('Bachiller','Técnico','Tecnólogo','Profesional','Especialización','Maestría','Curso') NOT NULL,
  fecha_inicio DATE NOT NULL,
  fecha_fin    DATE NULL,
  en_curso     BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT pk_formacion PRIMARY KEY (id_formacion),
  CONSTRAINT fk_formacion_perfil FOREIGN KEY (id_perfil)
    REFERENCES perfil (id_perfil) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT ck_formacion_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
) ENGINE=InnoDB;

-- Experiencia laboral
CREATE TABLE experiencia (
  id_experiencia INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_perfil      INT UNSIGNED NOT NULL,
  empresa        VARCHAR(100) NOT NULL,
  cargo          VARCHAR(80)  NOT NULL,
  funciones      TEXT NULL,
  fecha_inicio   DATE NOT NULL,
  fecha_fin      DATE NULL,
  trabajo_actual BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT pk_experiencia PRIMARY KEY (id_experiencia),
  CONSTRAINT fk_experiencia_perfil FOREIGN KEY (id_perfil)
    REFERENCES perfil (id_perfil) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT ck_experiencia_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
) ENGINE=InnoDB;

-- Habilidades
CREATE TABLE habilidad (
  id_habilidad INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_perfil    INT UNSIGNED NOT NULL,
  nombre       VARCHAR(60) NOT NULL,
  nivel        ENUM('Básico','Intermedio','Avanzado') NOT NULL DEFAULT 'Intermedio',
  CONSTRAINT pk_habilidad PRIMARY KEY (id_habilidad),
  CONSTRAINT uq_habilidad UNIQUE (id_perfil, nombre),
  CONSTRAINT fk_habilidad_perfil FOREIGN KEY (id_perfil)
    REFERENCES perfil (id_perfil) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Idiomas
CREATE TABLE idioma (
  id_idioma INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_perfil INT UNSIGNED NOT NULL,
  idioma    VARCHAR(40) NOT NULL,
  nivel     ENUM('A1','A2','B1','B2','C1','C2','Nativo') NOT NULL,
  CONSTRAINT pk_idioma PRIMARY KEY (id_idioma),
  CONSTRAINT uq_idioma UNIQUE (id_perfil, idioma),
  CONSTRAINT fk_idioma_perfil FOREIGN KEY (id_perfil)
    REFERENCES perfil (id_perfil) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Referencias personales y laborales
CREATE TABLE referencia (
  id_referencia INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_perfil     INT UNSIGNED NOT NULL,
  nombre        VARCHAR(100) NOT NULL,
  tipo          ENUM('Personal','Laboral','Familiar') NOT NULL,
  telefono      VARCHAR(15)  NOT NULL,
  ocupacion     VARCHAR(80)  NULL,
  CONSTRAINT pk_referencia PRIMARY KEY (id_referencia),
  CONSTRAINT fk_referencia_perfil FOREIGN KEY (id_perfil)
    REFERENCES perfil (id_perfil) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Plantillas de diseño disponibles
CREATE TABLE plantilla (
  id_plantilla INT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre       VARCHAR(40)  NOT NULL,
  descripcion  VARCHAR(200) NULL,
  archivo_css  VARCHAR(120) NOT NULL,
  activa       BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT pk_plantilla PRIMARY KEY (id_plantilla),
  CONSTRAINT uq_plantilla_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

-- Hojas de vida generadas en PDF
CREATE TABLE hoja_vida (
  id_hoja_vida     INT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_perfil        INT UNSIGNED NOT NULL,
  id_plantilla     INT UNSIGNED NOT NULL,
  fecha_generacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  archivo_pdf      VARCHAR(255) NULL,
  CONSTRAINT pk_hoja_vida PRIMARY KEY (id_hoja_vida),
  CONSTRAINT fk_hoja_vida_perfil FOREIGN KEY (id_perfil)
    REFERENCES perfil (id_perfil) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_hoja_vida_plantilla FOREIGN KEY (id_plantilla)
    REFERENCES plantilla (id_plantilla) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Datos iniciales
INSERT INTO plantilla (nombre, descripcion, archivo_css) VALUES
('Clásica','Diseño sobrio en una columna','clasica.css'),
('Moderna','Dos columnas con barra lateral de color','moderna.css'),
('Creativa','Encabezado con foto y colores','creativa.css');
