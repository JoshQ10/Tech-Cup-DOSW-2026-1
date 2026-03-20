package edu.dosw.proyecto.Tech_Cup_Football_2026_1;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Rol;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class TechCupFootball20261Application {

    public static void main(String[] args) {
        SpringApplication.run(TechCupFootball20261Application.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner seedRoles(EntityManager entityManager) {
        return args -> {
            for (Rol.RolNombre rolNombre : Rol.RolNombre.values()) {
                Long total = entityManager
                        .createQuery("SELECT COUNT(r) FROM Rol r WHERE r.nombre = :nombre", Long.class)
                        .setParameter("nombre", rolNombre)
                        .getSingleResult();

                if (total == 0) {
                    entityManager.persist(new Rol(rolNombre));
                }
            }
        };
    }
}
