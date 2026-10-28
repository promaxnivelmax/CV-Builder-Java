package co.edu.sena.cvbuilder.controlador;

import co.edu.sena.cvbuilder.dao.FormacionDAO;
import co.edu.sena.cvbuilder.modelo.Formacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador del módulo web de formación académica de CV Builder.
 * <ul>
 *   <li>GET: muestra la lista, el formulario para un estudio nuevo o el formulario de edición.</li>
 *   <li>POST: guarda (inserta o actualiza) o elimina un estudio.</li>
 * </ul>
 *
 * @author Iván Gerardo Rodríguez Bustamante
 */
@WebServlet(name = "FormacionServlet", urlPatterns = {"/formacion"})
public class FormacionServlet extends HttpServlet {

    private static final int ID_PERFIL_DEMO = 1;
    private static final String VISTA_LISTA = "/WEB-INF/vistas/formacion-lista.jsp";
    private static final String VISTA_FORMULARIO = "/WEB-INF/vistas/formacion-formulario.jsp";

    private final FormacionDAO formacionDao = new FormacionDAO();

    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String accion = valorOVacio(solicitud.getParameter("accion"));
        try {
            switch (accion) {
                case "nuevo" -> mostrarFormulario(solicitud, respuesta, new Formacion());
                case "editar" -> editar(solicitud, respuesta);
                default -> listar(solicitud, respuesta);
            }
        } catch (SQLException error) {
            throw new ServletException("Error al consultar la base de datos", error);
        }
    }

    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        solicitud.setCharacterEncoding("UTF-8");
        String accion = valorOVacio(solicitud.getParameter("accion"));
        try {
            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(solicitud.getParameter("id"));
                boolean eliminado = formacionDao.eliminar(id);
                redirigir(solicitud, respuesta, eliminado ? "Estudio eliminado." : "No se encontró el estudio.");
                return;
            }
            Formacion formacion = leerFormulario(solicitud);
            List<String> errores = validar(formacion);
            if (!errores.isEmpty()) {
                solicitud.setAttribute("errores", errores);
                mostrarFormulario(solicitud, respuesta, formacion);
                return;
            }
            if (formacion.getIdFormacion() == 0) {
                formacionDao.insertar(formacion);
                redirigir(solicitud, respuesta, "Estudio registrado correctamente.");
            } else {
                formacionDao.actualizar(formacion);
                redirigir(solicitud, respuesta, "Estudio actualizado correctamente.");
            }
        } catch (SQLException error) {
            throw new ServletException("Error al guardar en la base de datos", error);
        }
    }

    private void listar(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws SQLException, ServletException, IOException {
        solicitud.setAttribute("estudios", formacionDao.consultarPorPerfil(ID_PERFIL_DEMO));
        solicitud.setAttribute("mensaje", solicitud.getParameter("mensaje"));
        solicitud.getRequestDispatcher(VISTA_LISTA).forward(solicitud, respuesta);
    }

    private void editar(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(solicitud.getParameter("id"));
        Optional<Formacion> encontrado = formacionDao.consultarPorId(id);
        if (encontrado.isEmpty()) {
            redirigir(solicitud, respuesta, "No se encontró el estudio.");
            return;
        }
        mostrarFormulario(solicitud, respuesta, encontrado.get());
    }

    private void mostrarFormulario(HttpServletRequest solicitud, HttpServletResponse respuesta, Formacion formacion)
            throws ServletException, IOException {
        solicitud.setAttribute("formacion", formacion);
        solicitud.getRequestDispatcher(VISTA_FORMULARIO).forward(solicitud, respuesta);
    }

    private Formacion leerFormulario(HttpServletRequest solicitud) {
        Formacion formacion = new Formacion();
        String id = valorOVacio(solicitud.getParameter("id"));
        formacion.setIdFormacion(id.isEmpty() ? 0 : Integer.parseInt(id));
        formacion.setIdPerfil(ID_PERFIL_DEMO);
        formacion.setInstitucion(valorOVacio(solicitud.getParameter("institucion")).trim());
        formacion.setTitulo(valorOVacio(solicitud.getParameter("titulo")).trim());
        formacion.setNivel(valorOVacio(solicitud.getParameter("nivel")));
        formacion.setEnCurso(solicitud.getParameter("enCurso") != null);
        formacion.setFechaInicio(convertirFecha(solicitud.getParameter("fechaInicio")));
        formacion.setFechaFin(formacion.isEnCurso() ? null : convertirFecha(solicitud.getParameter("fechaFin")));
        return formacion;
    }

    private List<String> validar(Formacion formacion) {
        List<String> errores = new ArrayList<>();
        if (formacion.getInstitucion().isEmpty()) {
            errores.add("La institución es obligatoria.");
        }
        if (formacion.getTitulo().isEmpty()) {
            errores.add("El título es obligatorio.");
        }
        if (formacion.getNivel().isEmpty()) {
            errores.add("Seleccione el nivel.");
        }
        if (formacion.getFechaInicio() == null) {
            errores.add("La fecha de inicio es obligatoria.");
        }
        if (!formacion.isEnCurso() && formacion.getFechaFin() == null) {
            errores.add("Indique la fecha de finalización o marque «En curso».");
        }
        if (formacion.getFechaInicio() != null && formacion.getFechaFin() != null
                && formacion.getFechaFin().isBefore(formacion.getFechaInicio())) {
            errores.add("La fecha de finalización no puede ser anterior a la de inicio.");
        }
        return errores;
    }

    private void redirigir(HttpServletRequest solicitud, HttpServletResponse respuesta, String mensaje)
            throws IOException {
        String texto = java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);
        respuesta.sendRedirect(solicitud.getContextPath() + "/formacion?mensaje=" + texto);
    }

    private static LocalDate convertirFecha(String texto) {
        try {
            return (texto == null || texto.isBlank()) ? null : LocalDate.parse(texto);
        } catch (DateTimeParseException error) {
            return null;
        }
    }

    private static String valorOVacio(String texto) {
        return texto == null ? "" : texto;
    }
}
