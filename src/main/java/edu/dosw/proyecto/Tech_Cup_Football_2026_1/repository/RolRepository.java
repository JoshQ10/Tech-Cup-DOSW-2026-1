package edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(Rol.RolNombre nombre);
    boolean existsByNombre(Rol.RolNombre nombre);
}

