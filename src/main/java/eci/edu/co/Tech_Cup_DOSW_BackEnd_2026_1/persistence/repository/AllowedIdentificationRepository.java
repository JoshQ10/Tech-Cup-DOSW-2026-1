package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.AllowedIdentificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllowedIdentificationRepository extends JpaRepository<AllowedIdentificationEntity, Long> {

    Optional<AllowedIdentificationEntity> findByIdentificationAndAllowedRoleAndUsedFalse(
            String identification, Role allowedRole);

    List<AllowedIdentificationEntity> findByAllowedRole(Role allowedRole);

    boolean existsByIdentification(String identification);
}
