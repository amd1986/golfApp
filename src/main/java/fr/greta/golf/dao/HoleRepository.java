package fr.greta.golf.dao;

import fr.greta.golf.entities.Hole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoleRepository extends JpaRepository<Hole, Long> {
}
