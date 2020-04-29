package fr.greta.golf.dao;

import fr.greta.golf.entities.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Page<Game> findByNameContains(String mc, Pageable pageable);
}
