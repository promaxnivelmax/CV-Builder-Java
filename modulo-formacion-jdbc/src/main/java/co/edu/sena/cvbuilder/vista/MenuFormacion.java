package co.edu.sena.cvbuilder.vista;

import co.edu.sena.cvbuilder.dao.FormacionDAO;
import co.edu.sena.cvbuilder.modelo.Formacion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menú de consola del módulo de formación académica de CV Builder.
 * Permite insertar, consultar, actualizar y eliminar estudios de un perfil.
 *
 * @author Iván Gerardo Rodríguez Bustamante
 */
public class MenuFormacion {

    private static final int ID_PERFIL_DEMO = 1;
    private static final String[] NIVELES = {"Bachiller", "Técnico", "Tecnólogo", "Profesional",
        "Especialización", "Maestría", "Curso"};

    private final FormacionDAO formacionDao = new FormacionDAO();
    private final Scanner lector = new Scanner(System.in, "UTF-8");

    public static void main(String[] args) {
        new MenuFormacion().iniciar();
    }

    /** Muestra el menú hasta que el usuario elige salir. */
    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1 -> registrarFormacion();
                    case 2 -> listarFormacion();
                    case 3 -> actualizarFormacion();
                    case 4 -> eliminarFormacion();
                    case 0 -> System.out.println("Hasta pronto.");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException error) {
                System.out.println("Error de base de datos: " + error.getMessage());
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== CV BUILDER - FORMACIÓN ACADÉMICA =====");
        System.out.println("1. Registrar estudio");
        System.out.println("2. Consultar estudios");
        System.out.println("3. Actualizar estudio");
        System.out.println("4. Eliminar estudio");
        System.out.println("0. Salir");
    }

    private void registrarFormacion() throws SQLException {
        String institucion = leerTexto("Institución: ");
        String titulo = leerTexto("Título: ");
        String nivel = leerNivel();
        LocalDate fechaInicio = leerFecha("Fecha de inicio (aaaa-mm-dd): ");
        boolean enCurso = leerTexto("¿En curso? (s/n): ").equalsIgnoreCase("s");
        LocalDate fechaFin = enCurso ? null : leerFecha("Fecha de finalización (aaaa-mm-dd): ");
        Formacion formacion = new Formacion(ID_PERFIL_DEMO, institucion, titulo, nivel, fechaInicio, fechaFin, enCurso);
        int id = formacionDao.insertar(formacion);
        System.out.println("Estudio registrado con el id " + id + ".");
    }

    private void listarFormacion() throws SQLException {
        List<Formacion> lista = formacionDao.consultarPorPerfil(ID_PERFIL_DEMO);
        if (lista.isEmpty()) {
            System.out.println("No hay estudios registrados.");
            return;
        }
        System.out.println("ID  | Título                                           | Nivel      | Institución                            | Periodo");
        lista.forEach(System.out::println);
    }

    private void actualizarFormacion() throws SQLException {
        int id = leerEntero("Id del estudio a actualizar: ");
        Optional<Formacion> encontrado = formacionDao.consultarPorId(id);
        if (encontrado.isEmpty()) {
            System.out.println("No existe un estudio con ese id.");
            return;
        }
        Formacion formacion = encontrado.get();
        String titulo = leerTexto("Nuevo título (Enter para dejar '" + formacion.getTitulo() + "'): ");
        if (!titulo.isBlank()) {
            formacion.setTitulo(titulo);
        }
        String institucion = leerTexto("Nueva institución (Enter para dejar '" + formacion.getInstitucion() + "'): ");
        if (!institucion.isBlank()) {
            formacion.setInstitucion(institucion);
        }
        System.out.println(formacionDao.actualizar(formacion) ? "Estudio actualizado." : "No se pudo actualizar.");
    }

    private void eliminarFormacion() throws SQLException {
        int id = leerEntero("Id del estudio a eliminar: ");
        System.out.println(formacionDao.eliminar(id) ? "Estudio eliminado." : "No existe un estudio con ese id.");
    }

    private String leerNivel() {
        for (int i = 0; i < NIVELES.length; i++) {
            System.out.println("  " + (i + 1) + ". " + NIVELES[i]);
        }
        int opcion = leerEntero("Nivel: ");
        return (opcion >= 1 && opcion <= NIVELES.length) ? NIVELES[opcion - 1] : "Curso";
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return lector.hasNextLine() ? lector.nextLine().trim() : "";
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException error) {
                if (!lector.hasNextLine() && texto.isEmpty()) {
                    return 0;
                }
                System.out.println("Escriba un número válido.");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje));
            } catch (DateTimeParseException error) {
                System.out.println("Formato de fecha no válido. Use aaaa-mm-dd.");
            }
        }
    }
}
