package co.edu.sena.cvbuilder.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión con la base de datos cv_builder por medio de JDBC.
 * Los datos de conexión se leen del archivo db.properties.
 *
 * @author Iván Gerardo Rodríguez Bustamante
 */
public final class ConexionBD {

    private static final String ARCHIVO_CONFIGURACION = "/db.properties";
    private static final Properties CONFIGURACION = new Properties();

    static {
        try (InputStream entrada = ConexionBD.class.getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró el archivo db.properties");
            }
            CONFIGURACION.load(entrada);
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo leer db.properties", error);
        }
    }

    private ConexionBD() {
        // Clase utilitaria: no se instancia.
    }

    /**
     * Registra el driver JDBC. En aplicaciones web el registro automático
     * puede no funcionar, por eso se carga de forma explícita.
     */
    private static void cargarDriver() throws SQLException {
        String driver = CONFIGURACION.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        try {
            Class.forName(System.getProperty("db.driver", driver));
        } catch (ClassNotFoundException error) {
            throw new SQLException("No se encontró el driver JDBC: " + driver, error);
        }
    }

    /**
     * Abre una nueva conexión con la base de datos.
     *
     * @return conexión abierta
     * @throws SQLException si no es posible conectarse
     */
    public static Connection obtenerConexion() throws SQLException {
        String url = System.getProperty("db.url", CONFIGURACION.getProperty("db.url"));
        String usuario = System.getProperty("db.usuario", CONFIGURACION.getProperty("db.usuario"));
        String clave = System.getProperty("db.clave", CONFIGURACION.getProperty("db.clave"));
        cargarDriver();
        return DriverManager.getConnection(url, usuario, clave);
    }
}
