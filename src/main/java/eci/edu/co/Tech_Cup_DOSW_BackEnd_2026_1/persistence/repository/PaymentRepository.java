package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.PaymentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByTeamId(Long teamId);
    List<PaymentEntity> findByStatus(PaymentStatus status);
}
