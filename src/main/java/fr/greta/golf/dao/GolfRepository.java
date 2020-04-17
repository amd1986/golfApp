package fr.greta.golf.dao;

import fr.greta.golf.entities.Golf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GolfRepository extends JpaRepository<Golf, Long> {
}
