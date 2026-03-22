package edu.dosw.proyecto.Tech_Cup_Football_2026_1;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Rol;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TechCupFootball20261Application {

    public static void main(String[] args) {
        SpringApplication.run(TechCupFootball20261Application.class, args);
    }

    @Bean
    CommandLineRunner seedRoles(RolRepository rolRepository) {
        return args -> {
            for (Rol.RolNombre nombre : Rol.RolNombre.values()) {
                if (!rolRepository.existsByNombre(nombre)) {
                    rolRepository.save(new Rol(nombre));
                }
            }
        };
    }
}
