package fr.greta.golf.dao;

import fr.greta.golf.entities.WalkTimeBtHoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRepository extends JpaRepository<WalkTimeBtHoles, Long> {
    public Page<WalkTimeBtHoles> findByDescriptionContains(String mc, Pageable pageable);
}
