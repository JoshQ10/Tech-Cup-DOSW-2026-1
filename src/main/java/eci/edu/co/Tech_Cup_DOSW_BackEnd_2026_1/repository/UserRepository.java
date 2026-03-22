package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
