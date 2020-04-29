package fr.greta.golf.dao;

import fr.greta.golf.entities.Hole;
import fr.greta.golf.entities.WalkTimeBtHoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalkRepository extends JpaRepository<WalkTimeBtHoles, Long> {
    Page<WalkTimeBtHoles> findByDescriptionContains(String mc, Pageable pageable);
    @Query("select w from WalkTimeBtHoles as w where w.hole1 = :x and w.hole2 = :y")
    Optional<WalkTimeBtHoles> chercherParTrous(@Param("x") Hole hole1, @Param("y") Hole hole2);
}
