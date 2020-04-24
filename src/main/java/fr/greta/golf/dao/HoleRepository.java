package fr.greta.golf.dao;

import fr.greta.golf.entities.Hole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoleRepository extends JpaRepository<Hole, Long> {
    Page<Hole> findByNameContains(String mc, Pageable pageable);
}
