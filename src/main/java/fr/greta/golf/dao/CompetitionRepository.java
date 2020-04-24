package fr.greta.golf.dao;

import fr.greta.golf.entities.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    public Page<Competition> findByNameContains(String mc, Pageable pageable);
}
