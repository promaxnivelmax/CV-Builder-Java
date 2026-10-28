package co.edu.sena.cvbuilder.experiencia.servicio;

import co.edu.sena.cvbuilder.experiencia.modelo.Experiencia;
import co.edu.sena.cvbuilder.experiencia.repositorio.ExperienciaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio del módulo de experiencia laboral.
 *
 * @author Iván Gerardo Rodríguez Bustamante
 */
@Service
public class ExperienciaServicio {

    private final ExperienciaRepositorio repositorio;

    public ExperienciaServicio(ExperienciaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    /** Lista las experiencias del perfil indicado. */
    @Transactional(readOnly = true)
    public List<Experiencia> listarPorPerfil(Integer idPerfil) {
        return repositorio.findByIdPerfilOrderByFechaInicioDesc(idPerfil);
    }

    /** Busca una experiencia por su identificador. */
    @Transactional(readOnly = true)
    public Optional<Experiencia> buscarPorId(Integer id) {
        return repositorio.findById(id);
    }

    /** Guarda una experiencia nueva o actualiza una existente. */
    @Transactional
    public Experiencia guardar(Experiencia experiencia) {
        if (experiencia.isTrabajoActual()) {
            experiencia.setFechaFin(null);
        }
        experiencia.setEmpresa(experiencia.getEmpresa().trim());
        experiencia.setCargo(experiencia.getCargo().trim());
        return repositorio.save(experiencia);
    }

    /** Elimina una experiencia; devuelve false si no existe. */
    @Transactional
    public boolean eliminar(Integer id) {
        if (!repositorio.existsById(id)) {
            return false;
        }
        repositorio.deleteById(id);
        return true;
    }
}
