package co.edu.sena.cvbuilder.experiencia.repositorio;

import co.edu.sena.cvbuilder.experiencia.modelo.Experiencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data JPA de la entidad Experiencia.
 * Spring genera automáticamente las operaciones CRUD.
 */
public interface ExperienciaRepositorio extends JpaRepository<Experiencia, Integer> {

    /** Consulta las experiencias de un perfil, de la más reciente a la más antigua. */
    List<Experiencia> findByIdPerfilOrderByFechaInicioDesc(Integer idPerfil);
}
