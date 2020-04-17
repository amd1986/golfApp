package fr.greta.golf.dao;

import fr.greta.golf.entities.Hole;
import fr.greta.golf.entities.WalkTimeBtHoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRepository extends JpaRepository<WalkTimeBtHoles, Long> {
}
