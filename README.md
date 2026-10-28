# CV Builder – Módulos en Java

Proyecto formativo SENA – Tecnología en Análisis y Desarrollo de Software (ficha 3186596)
Autor: Iván Gerardo Rodríguez Bustamante

## Módulos
| Carpeta | Evidencia | Descripción |
|---|---|---|
| `modulo-formacion-jdbc` | GA7-220501096-AA2-EV01 | CRUD de formación académica con JDBC y MySQL |
| `modulo-formacion-web` | GA7-220501096-AA2-EV02 | Módulo web con Servlets, JSP y formularios HTML (GET y POST) |

## Cómo ejecutar `modulo-formacion-jdbc`
1. Crear la base de datos: `mysql -u root -p < sql/cv_builder_bd.sql` y `mysql -u root -p < sql/datos_prueba.sql`
2. Ajustar usuario y clave en `src/main/resources/db.properties`
3. Compilar y ejecutar: `mvn compile exec:java`

## Cómo ejecutar `modulo-formacion-web`
1. Crear la base de datos con los scripts de `modulo-formacion-jdbc/sql`.
2. Ajustar usuario y clave en `src/main/resources/db.properties`.
3. Generar el WAR con `mvn package` y copiar `target/cvbuilder-formacion.war` a la carpeta `webapps` de Tomcat 10.
4. Abrir http://localhost:8080/cvbuilder-formacion/

## modulo-experiencia-spring (GA7-220501096-AA3)
Módulo de experiencia laboral con Spring Boot 3, Spring Data JPA, Thymeleaf y Bean Validation.
Ejecutar con MySQL: `mvn spring-boot:run` · Demostración sin MySQL: `mvn spring-boot:run -Dspring-boot.run.profiles=demo` y abrir http://localhost:8080/experiencia
Pruebas unitarias: `mvn test` · Evidencias de pruebas en `evidencias/aa3`.
