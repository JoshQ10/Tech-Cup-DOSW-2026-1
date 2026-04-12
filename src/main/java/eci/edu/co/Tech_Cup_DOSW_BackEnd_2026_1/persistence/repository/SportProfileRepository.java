package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.SportProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportProfileRepository extends JpaRepository<SportProfileEntity, Long> {
    Optional<SportProfileEntity> findByUserId(Long userId);

    @Query("SELECT sp FROM SportProfileEntity sp JOIN UserEntity u ON sp.userId = u.id " +
           "WHERE sp.available = true " +
           "AND (:position IS NULL OR sp.position = :position) " +
           "AND (:semester IS NULL OR sp.semester = :semester) " +
           "AND (:age IS NULL OR sp.age = :age) " +
           "AND (:gender IS NULL OR sp.gender = :gender) " +
           "AND (:name IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<SportProfileEntity> searchAvailablePlayers(
            @Param("position") Position position,
            @Param("semester") Integer semester,
            @Param("age") Integer age,
            @Param("gender") String gender,
            @Param("name") String name,
            Pageable pageable);

    @Query("SELECT sp FROM SportProfileEntity sp WHERE sp.available = true")
    Page<SportProfileEntity> findAllAvailable(Pageable pageable);
}
