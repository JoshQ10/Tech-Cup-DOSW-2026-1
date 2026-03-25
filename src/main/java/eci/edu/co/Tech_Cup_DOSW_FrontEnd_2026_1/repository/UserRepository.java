package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
