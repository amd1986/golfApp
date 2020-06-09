package fr.greta.golf.rules.services;

import fr.greta.golf.rules.entities.Rule;
import fr.greta.golf.rules.entities.Section;
import fr.greta.golf.rules.entities.SubSection;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.util.Set;

@Service
public interface IGenerateRuleDocService {
    WordprocessingMLPackage generateDocxFromHtml(Set<Section> sections, Set<SubSection> subSections, Set<Rule> rules) throws Docx4JException, JAXBException;
}
