package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.SportProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportProfileRepository extends MongoRepository<SportProfile, String> {
    Optional<SportProfile> findByUserId(String userId);
}
