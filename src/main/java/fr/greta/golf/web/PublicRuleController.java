package fr.greta.golf.web;

import fr.greta.golf.entities.Rule;
import fr.greta.golf.entities.Section;
import fr.greta.golf.entities.SubSection;
import fr.greta.golf.services.GenerateRuleDocServiceImpl;
import fr.greta.golf.services.IGenerateRuleDocService;
import fr.greta.golf.services.ILangAccessService;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <b>PublicRuleController est la classe controller pour la génération de règles de golf</b>
 * <p>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher toutes les règles de golf.</li>
 * <li>Un méthode Get pour afficher les règles sélectionnées.</li>
 * <li>Un méthode Post pour générer le document Word de règles locales.</li>
 * </ul>
 * </p>
 *
 * @see ILangAccessService
 * @see IGenerateRuleDocService
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class PublicRuleController {
    private IGenerateRuleDocService IGenerateRuleDocService;
    private ILangAccessService iLangAccessService;

    /**
     * Méthode getIGenerateRuleDocService.
     * <p>
     *     Méthode permettant d'injecter le service dans le controller.
     * </p>
     *
     * @see IGenerateRuleDocService
     * @see GenerateRuleDocServiceImpl
     *
     * @return IGenerateRuleDocService
     */
    @Bean
    public IGenerateRuleDocService getIGenerateRuleDocService(){
        this.IGenerateRuleDocService = new GenerateRuleDocServiceImpl();
        return IGenerateRuleDocService;
    }

    /**
     * Constructeur PublicRuleController.
     * <p>
     *     On injecte dans le controller le service.
     * </p>
     *
     * @see ILangAccessService
     */
    public PublicRuleController(@Qualifier("getiLangAccessService") ILangAccessService iLangAccessService) {
        this.iLangAccessService = iLangAccessService;
    }

    /**
     * Méthode allRules.
     * <p>
     *     Méthode qui va afficher la liste des règles locale en fonction de la langue choisit.
     * </p>
     *
     * @param model Pour l'échange de données avec la vue
     * @param locale Langue que l'utilisateur a choisit
     *
     * @see ILangAccessService
     */
    @GetMapping(path = "/{locale:en|fr|es}")
    public String allRules(@PathVariable(name = "locale") String locale, Model model){
        List<Section> sections = this.iLangAccessService.getAllSectionByLang(locale);
        model.addAttribute("sections", sections);
        return "index";
    }

    /**
     * Méthode selectedRules.
     * <p>
     *     Méthode qui va afficher la liste des règles locale en fonction de la langue choisit.
     * </p>
     *
     * @param model Pour l'échange de données avec la vue
     * @param locale Langue que l'utilisateur a choisit
     * @param ruleListId On récupère les identifiants des règles choisit par l'utilisateur
     *
     * @see ILangAccessService
     */
    @GetMapping(path = "/{locale:en|fr|es}/ruleList")
    public String selectedRules(Model model, @PathVariable String locale,
                                  @RequestParam(name = "rules", defaultValue = "null") Set<Long> ruleListId){
        if (ruleListId != null){
            Set<Rule> ruleSet = new HashSet<>();
            for (Long ruleId: ruleListId){
                Rule r = this.iLangAccessService.ruleLangAccess(locale, ruleId);
                if (r != null){
                    ruleSet.add(r);
                }
            }
            model.addAttribute("rules", ruleSet);
            return "ruleSelected";
        }
        return "redirect:/"+locale;
    }

    /**
     * Méthode selectedRules.
     * <p>
     *     Méthode qui va afficher la liste des règles locale en fonction de la langue choisit.
     * </p>
     *
     * @param rules On récupère les identifiants des règles choisit par l'utilisateur
     * @param locale Langue que l'utilisateur a choisit
     * @param response Pour envoyer le document dans la réponse
     *
     * @see ILangAccessService
     * @see IGenerateRuleDocService
     */
    @PostMapping(path = "/{locale:en|fr|es}/generateDoc")
    public void generateRuleDoc(@PathVariable String locale, HttpServletResponse response,
                                  @RequestParam List<Long> rules) throws JAXBException, Docx4JException, IOException {
        if (rules != null){
            Set<Rule> ruleSet = new HashSet<>();
            Set<Section> sectionSet = new HashSet<>();
            Set<SubSection> subsectionSet = new HashSet<>();
            for (Long ruleId: rules){
                Rule r = this.iLangAccessService.ruleLangAccess(locale, ruleId);
                if (r != null){
                    ruleSet.add(r);
                    sectionSet.add(r.getSubSection().getSection());
                    subsectionSet.add(r.getSubSection());
                }
            }
            WordprocessingMLPackage wordMLPackage = this.IGenerateRuleDocService.generateDocxFromHtml(sectionSet, subsectionSet, ruleSet);
            if (wordMLPackage != null){
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                response.setHeader("content-disposition","filename=LocalGolfRules.doc");
                wordMLPackage.save(response.getOutputStream());
            }
        }
    }
}
