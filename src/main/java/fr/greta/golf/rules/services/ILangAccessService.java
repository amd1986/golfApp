package fr.greta.golf.rules.services;

import fr.greta.golf.rules.entities.Rule;
import fr.greta.golf.rules.entities.Section;
import fr.greta.golf.rules.entities.SubSection;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface ILangAccessService {
    boolean langAccess(String lang, HttpServletRequest request);
    Section sectionLangAccess(String lang, Long id);
    SubSection subsectionLangAccess(String lang, Long id);
    Rule ruleLangAccess(String lang, Long id);
    List<Section> getAllSectionByLang(String lang);
    List<SubSection> getAllSubsectionByLang(String lang);
    boolean deleteSection(Long id);
    boolean deleteSubsection(Long id);
    String cleanHtml(String html);
}
