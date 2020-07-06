package fr.greta.golf.dao;
import fr.greta.golf.entities.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface SectionRepository extends JpaRepository<Section, Long> {
    Page<Section> findByLangAndTitleContains(String lang, String mc, Pageable pageable);
    Optional<Section> findByIdAndLang(Long id, String lang);
    List<Section> findAllByLang(String lang);
}
