package fr.greta.golf.rules.dao;

import fr.greta.golf.rules.entities.SubSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubsectionRepository extends JpaRepository<SubSection, Long> {
    Page<SubSection> findByLangAndTitleContains(String lang, String mc, Pageable pageable);
    Optional<SubSection> findByIdAndLang(Long id, String lang);
    List<SubSection> findAllByLang(String lang);
}
