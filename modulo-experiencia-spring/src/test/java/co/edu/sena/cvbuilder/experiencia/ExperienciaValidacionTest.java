package co.edu.sena.cvbuilder.experiencia;

import co.edu.sena.cvbuilder.experiencia.modelo.Experiencia;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de las validaciones de la entidad Experiencia.
 */
class ExperienciaValidacionTest {

    private final Validator validador = Validation.buildDefaultValidatorFactory().getValidator();

    private Experiencia valida() {
        Experiencia e = new Experiencia();
        e.setEmpresa("Soluciones Digitales S.A.S.");
        e.setCargo("Auxiliar de soporte");
        e.setFechaInicio(LocalDate.of(2023, 3, 1));
        e.setTrabajoActual(true);
        return e;
    }

    private Set<ConstraintViolation<Experiencia>> validar(Experiencia e) {
        return validador.validate(e);
    }

    @Test
    void experienciaCompletaEsValida() {
        assertTrue(validar(valida()).isEmpty());
    }

    @Test
    void empresaVaciaNoEsValida() {
        Experiencia e = valida();
        e.setEmpresa("");
        assertTrue(validar(e).stream().anyMatch(v -> v.getMessage().contains("empresa es obligatoria")));
    }

    @Test
    void caracteresEspecialesNoPermitidos() {
        Experiencia e = valida();
        e.setCargo("Auxiliar <script>");
        assertTrue(validar(e).stream().anyMatch(v -> v.getMessage().contains("caracteres no permitidos")));
    }

    @Test
    void longitudMaximaDelCargo() {
        Experiencia e = valida();
        e.setCargo("A".repeat(81));
        assertTrue(validar(e).stream().anyMatch(v -> v.getMessage().contains("entre 2 y 80")));
    }

    @Test
    void fechaInicioFuturaNoEsValida() {
        Experiencia e = valida();
        e.setFechaInicio(LocalDate.now().plusDays(10));
        assertTrue(validar(e).stream().anyMatch(v -> v.getMessage().contains("no puede ser futura")));
    }

    @Test
    void fechaFinAnteriorAlInicioNoEsValida() {
        Experiencia e = valida();
        e.setTrabajoActual(false);
        e.setFechaFin(LocalDate.of(2022, 1, 1));
        assertEquals(1, validar(e).size());
    }
}
