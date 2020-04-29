package fr.greta.golf.dao;

import fr.greta.golf.entities.TimePerHPerG;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimesRepository extends JpaRepository<TimePerHPerG, Long> {

    @Query("select t from TimePerHPerG as t where t.game.name like :x or t.hole.name like :x")
    Page<TimePerHPerG> chercherParMC(@Param("x") String mc, Pageable pageable);
}
