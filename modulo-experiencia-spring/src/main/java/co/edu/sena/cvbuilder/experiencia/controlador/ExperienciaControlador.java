package co.edu.sena.cvbuilder.experiencia.controlador;

import co.edu.sena.cvbuilder.experiencia.modelo.Experiencia;
import co.edu.sena.cvbuilder.experiencia.servicio.ExperienciaServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador Spring MVC del módulo de experiencia laboral (patrón MVC con Thymeleaf).
 *
 * @author Iván Gerardo Rodríguez Bustamante
 */
@Controller
@RequestMapping("/experiencia")
public class ExperienciaControlador {

    private static final Integer ID_PERFIL_DEMO = 1;
    private static final String VISTA_FORMULARIO = "experiencia/formulario";

    private final ExperienciaServicio servicio;

    public ExperienciaControlador(ExperienciaServicio servicio) {
        this.servicio = servicio;
    }

    /** HU-01: consultar la lista de experiencias. */
    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("experiencias", servicio.listarPorPerfil(ID_PERFIL_DEMO));
        return "experiencia/lista";
    }

    /** HU-02: mostrar el formulario para registrar una experiencia. */
    @GetMapping("/nueva")
    public String nueva(Model modelo) {
        modelo.addAttribute("experiencia", new Experiencia());
        return VISTA_FORMULARIO;
    }

    /** HU-03: mostrar el formulario con los datos de una experiencia para editarla. */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model modelo, RedirectAttributes redireccion) {
        return servicio.buscarPorId(id)
                .map(experiencia -> {
                    modelo.addAttribute("experiencia", experiencia);
                    return VISTA_FORMULARIO;
                })
                .orElseGet(() -> {
                    redireccion.addFlashAttribute("error", "No se encontró la experiencia.");
                    return "redirect:/experiencia";
                });
    }

    /** HU-02 y HU-03: validar y guardar (insertar o actualizar). */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("experiencia") Experiencia experiencia,
                          BindingResult validacion, RedirectAttributes redireccion) {
        if (validacion.hasErrors()) {
            return VISTA_FORMULARIO;
        }
        boolean esNueva = experiencia.getIdExperiencia() == null;
        experiencia.setIdPerfil(ID_PERFIL_DEMO);
        servicio.guardar(experiencia);
        redireccion.addFlashAttribute("mensaje",
                esNueva ? "Experiencia registrada correctamente." : "Experiencia actualizada correctamente.");
        return "redirect:/experiencia";
    }

    /** HU-04: eliminar una experiencia. */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redireccion) {
        boolean eliminada = servicio.eliminar(id);
        redireccion.addFlashAttribute(eliminada ? "mensaje" : "error",
                eliminada ? "Experiencia eliminada." : "No se encontró la experiencia.");
        return "redirect:/experiencia";
    }

    /** La raíz de la aplicación lleva a la lista. */
    @GetMapping("/inicio")
    public String inicio() {
        return "redirect:/experiencia";
    }
}
