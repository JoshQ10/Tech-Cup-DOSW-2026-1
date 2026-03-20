package edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TokenVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenVerificacionRepository extends JpaRepository<TokenVerificacion, Long> {
    Optional<TokenVerificacion> findByToken(String token);
    Optional<TokenVerificacion> findByUsuarioId(Long usuarioId);
}

