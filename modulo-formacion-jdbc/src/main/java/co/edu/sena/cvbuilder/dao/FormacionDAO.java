package co.edu.sena.cvbuilder.dao;

import co.edu.sena.cvbuilder.conexion.ConexionBD;
import co.edu.sena.cvbuilder.modelo.Formacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Objeto de acceso a datos (DAO) de la tabla formacion.
 * Implementa las operaciones de inserción, consulta, actualización y eliminación (CRUD)
 * usando JDBC y sentencias preparadas para evitar inyección SQL.
 *
 * @author Iván Gerardo Rodríguez Bustamante
 */
public class FormacionDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO formacion (id_perfil, institucion, titulo, nivel, fecha_inicio, fecha_fin, en_curso) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_CONSULTAR_POR_PERFIL =
            "SELECT * FROM formacion WHERE id_perfil = ? ORDER BY fecha_inicio DESC";
    private static final String SQL_CONSULTAR_POR_ID =
            "SELECT * FROM formacion WHERE id_formacion = ?";
    private static final String SQL_ACTUALIZAR =
            "UPDATE formacion SET institucion = ?, titulo = ?, nivel = ?, fecha_inicio = ?, "
            + "fecha_fin = ?, en_curso = ? WHERE id_formacion = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM formacion WHERE id_formacion = ?";

    /**
     * Inserta un nuevo registro de formación.
     *
     * @param formacion datos a guardar
     * @return identificador generado
     * @throws SQLException si ocurre un error en la base de datos
     */
    public int insertar(Formacion formacion) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            asignarParametros(sentencia, formacion);
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    formacion.setIdFormacion(llaves.getInt(1));
                }
            }
            return formacion.getIdFormacion();
        }
    }

    /**
     * Consulta todos los registros de formación de un perfil.
     *
     * @param idPerfil identificador del perfil
     * @return lista de registros, del más reciente al más antiguo
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Formacion> consultarPorPerfil(int idPerfil) throws SQLException {
        List<Formacion> lista = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_POR_PERFIL)) {
            sentencia.setInt(1, idPerfil);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    lista.add(mapearFila(resultado));
                }
            }
        }
        return lista;
    }

    /**
     * Consulta un registro por su identificador.
     *
     * @param idFormacion identificador del registro
     * @return el registro, o vacío si no existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    public Optional<Formacion> consultarPorId(int idFormacion) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_POR_ID)) {
            sentencia.setInt(1, idFormacion);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapearFila(resultado)) : Optional.empty();
            }
        }
    }

    /**
     * Actualiza un registro existente.
     *
     * @param formacion datos actualizados (debe tener idFormacion)
     * @return true si se actualizó el registro
     * @throws SQLException si ocurre un error en la base de datos
     */
    public boolean actualizar(Formacion formacion) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            sentencia.setString(1, formacion.getInstitucion());
            sentencia.setString(2, formacion.getTitulo());
            sentencia.setString(3, formacion.getNivel());
            sentencia.setDate(4, Date.valueOf(formacion.getFechaInicio()));
            asignarFechaFin(sentencia, 5, formacion);
            sentencia.setBoolean(6, formacion.isEnCurso());
            sentencia.setInt(7, formacion.getIdFormacion());
            return sentencia.executeUpdate() == 1;
        }
    }

    /**
     * Elimina un registro por su identificador.
     *
     * @param idFormacion identificador del registro
     * @return true si se eliminó el registro
     * @throws SQLException si ocurre un error en la base de datos
     */
    public boolean eliminar(int idFormacion) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setInt(1, idFormacion);
            return sentencia.executeUpdate() == 1;
        }
    }

    private void asignarParametros(PreparedStatement sentencia, Formacion formacion) throws SQLException {
        sentencia.setInt(1, formacion.getIdPerfil());
        sentencia.setString(2, formacion.getInstitucion());
        sentencia.setString(3, formacion.getTitulo());
        sentencia.setString(4, formacion.getNivel());
        sentencia.setDate(5, Date.valueOf(formacion.getFechaInicio()));
        asignarFechaFin(sentencia, 6, formacion);
        sentencia.setBoolean(7, formacion.isEnCurso());
    }

    private void asignarFechaFin(PreparedStatement sentencia, int indice, Formacion formacion) throws SQLException {
        if (formacion.getFechaFin() == null) {
            sentencia.setNull(indice, Types.DATE);
        } else {
            sentencia.setDate(indice, Date.valueOf(formacion.getFechaFin()));
        }
    }

    private Formacion mapearFila(ResultSet resultado) throws SQLException {
        Formacion formacion = new Formacion();
        formacion.setIdFormacion(resultado.getInt("id_formacion"));
        formacion.setIdPerfil(resultado.getInt("id_perfil"));
        formacion.setInstitucion(resultado.getString("institucion"));
        formacion.setTitulo(resultado.getString("titulo"));
        formacion.setNivel(resultado.getString("nivel"));
        formacion.setFechaInicio(resultado.getDate("fecha_inicio").toLocalDate());
        Date fechaFin = resultado.getDate("fecha_fin");
        formacion.setFechaFin(fechaFin == null ? null : fechaFin.toLocalDate());
        formacion.setEnCurso(resultado.getBoolean("en_curso"));
        return formacion;
    }
}
