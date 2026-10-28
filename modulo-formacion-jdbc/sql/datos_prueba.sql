-- Datos mínimos para probar el módulo de formación académica
SET NAMES utf8mb4;
USE cv_builder;
INSERT INTO usuario (nombre_usuario, correo, contrasena_hash)
VALUES ('ivanr', 'ivan.rodriguezbu@gmail.com', SHA2('CvBuilder2026*', 256));
INSERT INTO perfil (id_usuario, nombres, apellidos, numero_documento, telefono, ciudad)
VALUES (1, 'Iván Gerardo', 'Rodríguez Bustamante', '1000000001', '3100000000', 'Barrancabermeja');
