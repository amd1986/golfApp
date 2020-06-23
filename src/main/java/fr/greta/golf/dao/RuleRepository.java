package fr.greta.golf.dao;

import fr.greta.golf.entities.Rule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    Page<Rule> findByLangAndTitleContains(String lang, String mc, Pageable pageable);
    Optional<Rule> findByIdAndLang(Long id, String lang);
}
