package fr.greta.golf.dao;

import fr.greta.golf.entities.Golf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GolfRepository extends JpaRepository<Golf, Long> {

    /*Page<Golf> findByNameContains(String mc, Pageable pageable);*/
    @Query("select g from Golf as g where g.name like :x or g.address like :x or g.city like :x or g.country like :x")
    Page<Golf> chercherParMotCle(@Param("x") String mc, Pageable pageable);
}
