package com.angel.escuela.repositories;

import com.angel.escuela.entities.Maestro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaestroRepository extends JpaRepository<Maestro, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByTelefono(String telefono);

    boolean existsByEmailIgnoreCaseAndIdNot(String email , Long id);

    boolean existsByTelefonoAndIdNot(String nombre, Long id);

    Long id(Long id);
}
