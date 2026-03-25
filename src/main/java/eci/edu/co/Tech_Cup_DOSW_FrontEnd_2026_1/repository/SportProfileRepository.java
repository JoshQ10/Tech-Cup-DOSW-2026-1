package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.SportProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportProfileRepository extends JpaRepository<SportProfile, Long> {
    Optional<SportProfile> findByUserId(Long userId);
}
