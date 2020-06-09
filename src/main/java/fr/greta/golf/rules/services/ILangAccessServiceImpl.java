package fr.greta.golf.rules.services;

import fr.greta.golf.dao.UserRepository;
import fr.greta.golf.entities.security.Language;
import fr.greta.golf.entities.security.User;
import fr.greta.golf.rules.dao.RuleRepository;
import fr.greta.golf.rules.dao.SectionRepository;
import fr.greta.golf.rules.dao.SubsectionRepository;
import fr.greta.golf.rules.entities.Rule;
import fr.greta.golf.rules.entities.Section;
import fr.greta.golf.rules.entities.SubSection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class ILangAccessServiceImpl implements ILangAccessService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private SubsectionRepository subsectionRepository;
    @Autowired
    private RuleRepository ruleRepository;

    public ILangAccessServiceImpl() {
    }

    @Override
    public boolean langAccess(String lang, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            for (Language language: user.get().getLanguages()){
                if (language.getLanguage().equals(lang)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Section sectionLangAccess(String lang, Long id) {
        Section s = new Section();
        Optional<Section> section = sectionRepository.findByIdAndLang(id, lang);
        if (section.isPresent()){
            if (section.get().getLang().equals(lang))
                s = section.get();
        }
        return s;
    }

    @Override
    public SubSection subsectionLangAccess(String lang, Long id) {
        SubSection sub = new SubSection();
        Optional<SubSection> subSection = subsectionRepository.findByIdAndLang(id, lang);
        if (subSection.isPresent()){
            if (subSection.get().getLang().equals(lang))
                sub = subSection.get();
        }
        return sub;
    }

    @Override
    public Rule ruleLangAccess(String lang, Long id) {
        Rule r = new Rule();
        Optional<Rule> rule = ruleRepository.findByIdAndLang(id, lang);
        if (rule.isPresent()){
            if (rule.get().getLang().equals(lang))
                r = rule.get();
        }
        return r;
    }

    @Override
    public List<Section> getAllSectionByLang(String lang) {
        return sectionRepository.findAllByLang(lang);
    }

    @Override
    public List<SubSection> getAllSubsectionByLang(String lang) {
        return subsectionRepository.findAllByLang(lang);
    }

    @Override @Transactional
    public boolean deleteSection(Long id) {
        boolean removed = false;
        Optional<Section> section = sectionRepository.findById(id);
        if (section.isPresent()){
            if (section.get().getSubSections() != null){
                for (SubSection subSection: section.get().getSubSections()){
                    if (subSection.getRules() != null){
                        for (Rule rule: subSection.getRules()){
                            ruleRepository.delete(rule);
                        }
                    }
                    subsectionRepository.delete(subSection);
                }
            }
            sectionRepository.delete(section.get());
            removed = true;
        }
        return removed;
    }

    @Override @Transactional
    public boolean deleteSubsection(Long id) {
        boolean removed = false;
        Optional<SubSection> subSection = subsectionRepository.findById(id);
        if (subSection.isPresent()){
            if (subSection.get().getRules() != null){
                for (Rule rule: subSection.get().getRules()){
                    ruleRepository.delete(rule);
                }
            }
            subsectionRepository.delete(subSection.get());
            removed = true;
        }
        return removed;
    }

    @Override
    public String cleanHtml(String html) {
        Document doc = Jsoup.parse(html);

        Whitelist whiteList = new Whitelist();
        whiteList.addTags("p", "ul", "ol", "li", "strong", "em", "u");

        Cleaner cleaner = new Cleaner(whiteList);
        Document cleanDoc = cleaner.clean(doc);
        Elements elements = cleanDoc.select("body");
        System.out.println(elements);
        return elements.toString();
    }

}
