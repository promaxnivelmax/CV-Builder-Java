# CV Builder – Módulos en Java

Proyecto formativo SENA – Tecnología en Análisis y Desarrollo de Software (ficha 3186596)
Autor: Iván Gerardo Rodríguez Bustamante

## Módulos
| Carpeta | Evidencia | Descripción |
|---|---|---|
| `modulo-formacion-jdbc` | GA7-220501096-AA2-EV01 | CRUD de formación académica con JDBC y MySQL |

## Cómo ejecutar `modulo-formacion-jdbc`
1. Crear la base de datos: `mysql -u root -p < sql/cv_builder_bd.sql` y `mysql -u root -p < sql/datos_prueba.sql`
2. Ajustar usuario y clave en `src/main/resources/db.properties`
3. Compilar y ejecutar: `mvn compile exec:java`
