package fr.greta.golf.services;

import fr.greta.golf.dao.RuleRepository;
import fr.greta.golf.dao.SectionRepository;
import fr.greta.golf.dao.SubsectionRepository;
import fr.greta.golf.dao.UserRepository;
import fr.greta.golf.entities.*;
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

/**
 * <b>LangAccessServiceImpl est la classe implémentant ILangAccessService pour la gestion des règles locales</b>
 *
 * @see ILangAccessService
 * @see UserRepository
 * @see SectionRepository
 * @see SubsectionRepository
 * @see RuleRepository
 *
 * @author ahmed
 * @version 1.1.0
 */
public class LangAccessServiceImpl implements ILangAccessService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private SubsectionRepository subsectionRepository;
    @Autowired
    private RuleRepository ruleRepository;

    public LangAccessServiceImpl() {
    }

    /**
     * Méthode langAccess.
     * <p>
     *     Méthode qui va vérifier l'accès selon la langue attribuée à l'utilisateur.
     * </p>
     *
     * @param lang Langue choisit par l'utilisateur
     * @param request Pour récupérer l'utlisateur, ainsi que les langages qui lui sont attribués
     * @return boolean : true si l'utilisateur a droit d'accéder à du contenu ds cette langue
     */
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

    /**
     * Méthode sectionLangAccess.
     * <p>
     *     Méthode qui va vérifier que l'utilisateur a un droit de gestion sur la catégorie.
     * </p>
     *
     * @param lang Langue choisit par l'utilisateur
     * @param id Identitfiant de la catégorie sur laquelle l'utlisateur veut faire une opération
     * @return Section
     */
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

    /**
     * Méthode subsectionLangAccess.
     * <p>
     *     Méthode qui va vérifier que l'utilisateur a un droit de gestion sur la sous-catégorie.
     * </p>
     *
     * @param lang Langue choisit par l'utilisateur
     * @param id Identitfiant de la sous-catégorie sur laquelle l'utlisateur veut faire une opération
     * @return Subsection
     */
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

    /**
     * Méthode subsectionLangAccess.
     * <p>
     *     Méthode qui va vérifier que l'utilisateur a un droit de gestion sur la règle.
     * </p>
     *
     * @param lang Langue choisit par l'utilisateur
     * @param id Identitfiant de la règle sur laquelle l'utlisateur veut faire une opération
     * @return Rule
     */
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

    /**
     * Méthode getAllSectionByLang.
     * <p>
     *     Méthode qui va la liste des catégories par langue.
     * </p>
     *
     * @param lang Langue choisit par l'utilisateur
     * @return Liste de catégories
     */
    @Override
    public List<Section> getAllSectionByLang(String lang) {
        return sectionRepository.findAllByLang(lang);
    }

    /**
     * Méthode getAllSubsectionByLang.
     * <p>
     *     Méthode qui va la liste des sous-catégories par langue.
     * </p>
     *
     * @param lang Langue choisit par l'utilisateur
     * @return Liste de sous-catégories
     */
    @Override
    public List<SubSection> getAllSubsectionByLang(String lang) {
        return subsectionRepository.findAllByLang(lang);
    }

    /**
     * Méthode deleteSection.
     * <p>
     *     Méthode qui permet de supprimer la catégorie.
     * </p>
     *
     * @param id Identifiant de la catégorie à supprimer.
     * @return boolean
     */
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

    /**
     * Méthode deleteSubsection.
     * <p>
     *     Méthode qui permet de supprimer la sous-catégorie.
     * </p>
     *
     * @param id Identifiant de la sous-catégorie à supprimer.
     * @return boolean
     */
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

    /**
     * Méthode deleteSubsection.
     * <p>
     *     Méthode qui permet de nettoyer le code html.
     * </p>
     *
     * @param html Le contenu html à nettoyer.
     * @return boolean
     */
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
    /*TODO Utliser BBcode à la place du HTML*/
}
