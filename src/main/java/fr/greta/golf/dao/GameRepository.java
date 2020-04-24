package fr.greta.golf.dao;

import fr.greta.golf.entities.Course;
import fr.greta.golf.entities.Game;
import fr.greta.golf.entities.Hole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    public Page<Game> findByNameContains(String mc, Pageable pageable);
}
