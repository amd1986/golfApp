package fr.greta.golf.dao;

import fr.greta.golf.entities.security.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LangRepository extends JpaRepository<Language, String> {
}
