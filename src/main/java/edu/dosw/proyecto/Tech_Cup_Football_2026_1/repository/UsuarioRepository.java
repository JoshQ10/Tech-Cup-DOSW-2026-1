package edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TipoParticipante;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByTipoParticipante(TipoParticipante tipoParticipante);
    List<Usuario> findByActivoTrue();
}
