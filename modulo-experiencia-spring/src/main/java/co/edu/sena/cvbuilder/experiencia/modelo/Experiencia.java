package co.edu.sena.cvbuilder.experiencia.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Entidad JPA que representa una experiencia laboral de la hoja de vida.
 * Se mapea a la tabla experiencia de la base de datos cv_builder.
 *
 * @author Iván Gerardo Rodríguez Bustamante
 */
@Entity
@Table(name = "experiencia")
public class Experiencia {

    /** Letras (con tildes y ñ), números, espacios y signos básicos de puntuación. */
    private static final String TEXTO_VALIDO = "^[\\p{L}0-9 .,&()\\-]+$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_experiencia")
    private Integer idExperiencia;

    @Column(name = "id_perfil", nullable = false)
    private Integer idPerfil = 1;

    @NotBlank(message = "La empresa es obligatoria.")
    @Size(min = 2, max = 100, message = "La empresa debe tener entre 2 y 100 caracteres.")
    @Pattern(regexp = TEXTO_VALIDO, message = "La empresa contiene caracteres no permitidos.")
    @Column(nullable = false, length = 100)
    private String empresa;

    @NotBlank(message = "El cargo es obligatorio.")
    @Size(min = 2, max = 80, message = "El cargo debe tener entre 2 y 80 caracteres.")
    @Pattern(regexp = TEXTO_VALIDO, message = "El cargo contiene caracteres no permitidos.")
    @Column(nullable = false, length = 80)
    private String cargo;

    @Size(max = 500, message = "Las funciones no pueden superar 500 caracteres.")
    @Column(columnDefinition = "TEXT")
    private String funciones;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    @PastOrPresent(message = "La fecha de inicio no puede ser futura.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @PastOrPresent(message = "La fecha de finalización no puede ser futura.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "trabajo_actual", nullable = false)
    private boolean trabajoActual;

    /** Regla de negocio: si no es el trabajo actual, debe tener fecha de finalización. */
    @AssertTrue(message = "Indique la fecha de finalización o marque «Trabajo actual».")
    public boolean isFechaFinCompleta() {
        return trabajoActual || fechaFin != null;
    }

    /** Regla de negocio: la fecha de finalización no puede ser anterior a la de inicio. */
    @AssertTrue(message = "La fecha de finalización no puede ser anterior a la de inicio.")
    public boolean isRangoFechasValido() {
        return fechaInicio == null || fechaFin == null || !fechaFin.isBefore(fechaInicio);
    }

    public Integer getIdExperiencia() { return idExperiencia; }
    public void setIdExperiencia(Integer idExperiencia) { this.idExperiencia = idExperiencia; }
    public Integer getIdPerfil() { return idPerfil; }
    public void setIdPerfil(Integer idPerfil) { this.idPerfil = idPerfil; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getFunciones() { return funciones; }
    public void setFunciones(String funciones) { this.funciones = funciones; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public boolean isTrabajoActual() { return trabajoActual; }
    public void setTrabajoActual(boolean trabajoActual) { this.trabajoActual = trabajoActual; }
}
