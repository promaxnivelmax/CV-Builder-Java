package co.edu.sena.cvbuilder.modelo;

import java.time.LocalDate;

/**
 * Representa un registro de formación académica de la hoja de vida.
 * Corresponde a la tabla formacion de la base de datos cv_builder.
 *
 * @author Iván Gerardo Rodríguez Bustamante
 */
public class Formacion {

    private int idFormacion;
    private int idPerfil;
    private String institucion;
    private String titulo;
    private String nivel;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean enCurso;

    public Formacion() {
    }

    public Formacion(int idPerfil, String institucion, String titulo, String nivel,
                     LocalDate fechaInicio, LocalDate fechaFin, boolean enCurso) {
        this.idPerfil = idPerfil;
        this.institucion = institucion;
        this.titulo = titulo;
        this.nivel = nivel;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.enCurso = enCurso;
    }

    public int getIdFormacion() { return idFormacion; }
    public void setIdFormacion(int idFormacion) { this.idFormacion = idFormacion; }
    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }
    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public boolean isEnCurso() { return enCurso; }
    public void setEnCurso(boolean enCurso) { this.enCurso = enCurso; }

    @Override
    public String toString() {
        String periodo = fechaInicio + " a " + (enCurso ? "en curso" : String.valueOf(fechaFin));
        return String.format("%-3d | %-48s | %-10s | %-38s | %s",
                idFormacion, titulo, nivel, institucion, periodo);
    }
}
