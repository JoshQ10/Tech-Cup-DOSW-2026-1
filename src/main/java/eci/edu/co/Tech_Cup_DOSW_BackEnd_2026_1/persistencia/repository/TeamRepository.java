package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistencia.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
