package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.repository;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.Tournament;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends MongoRepository<Tournament, String> {
}
