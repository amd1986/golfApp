package fr.greta.golf.web;

import fr.greta.golf.dao.SectionRepository;
import fr.greta.golf.dao.UserRepository;
import fr.greta.golf.entities.Language;
import fr.greta.golf.entities.Section;
import fr.greta.golf.entities.User;
import fr.greta.golf.services.ILangAccessService;
import fr.greta.golf.services.LangAccessServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * <b>SectionController est la classe controller pour l'affichage et la gestion des catégories de règle de golf</b>
 * <p>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher une catégorie à partir de son Id.</li>
 * <li>Un méthode Get pour afficher les catégories avec un système de pagination.</li>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'une catégorie.</li>
 * <li>Un méthode Get pour afficher le formulaire de modification d'une catégorie à partir de son Id.</li>
 * <li>Un méthode Post pour ajouter une catégorie.</li>
 * <li>Un méthode Post pour modifier une catégorie à partir de son Id.</li>
 * <li>Un méthode Post pour supprimer une catégorie à partir de son Id.</li>
 * </ul>
 * </p>
 *
 * @see Section
 * @see SectionRepository
 * @see UserRepository
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class SectionController {
    private ILangAccessService iLangAccessService;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;

    /**
     * Constructeur SectionController.
     * <p>
     *     On y injecte les repositories pour la gestion des catégories de golf.
     * </p>
     *
     * @param sectionRepository
     *            Repository pour la gestion des catégories de règles de golf.
     * @param userRepository
     *            Repository pour la gestion des utilisateurs.
     *
     * @see SectionController#sectionRepository
     * @see SectionController#userRepository
     */
    public SectionController(SectionRepository sectionRepository, UserRepository userRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Méthode getSection.
     * <p>
     *     Méthode qui va afficher une catégorie.
     * </p>
     *
     * @param id Identifiant de la catégorie.
     *
     * @see SectionRepository
     */
    /*TODO Faire l'affichage d'erreur suite à la validation */
    @GetMapping(path = "/{locale:en|fr|es}/editor/section/{id}")
    public String getSection(Model model, @PathVariable(name = "id") Long id,
                             @PathVariable String locale ,HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        Section section =  this.iLangAccessService.sectionLangAccess(locale, id);
        if (section.getCode() == null){
            model.addAttribute("errorMsg", "Rubrique introuvable !");
        }else {
            model.addAttribute("section", section);
        }
        return "/rules/display/section";
    }

    /**
     * Méthode displaySections.
     * <p>
     *     Méthode qui va afficher les catégories avec une recherche par mot clé et une pagination.
     * </p>
     *
     * @param mc Mot clé pour la recherche
     * @param page Nombre de page pour la pagination
     * @param size Nombre d'élément par page pour la pagination
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     *
     * @see SectionRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/editor/searchSection")
    public String displaySections(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                  @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                  @RequestParam(name = "size", defaultValue = "5", required = false)int size, Model model,
                                  @PathVariable String locale ,HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        Page<Section> sections = sectionRepository.findByLangAndTitleContains(locale, mc, PageRequest.of(page, size));
        if (!sections.hasContent()) {
            sections = sectionRepository.findByLangAndTitleContains(locale, mc, PageRequest.of(0, 5));
            page = 0;
        }

        int[] pages = new int[sections.getTotalPages()];
        model.addAttribute("sections", sections.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "rules/search/section";
    }

    /**
     * Méthode deleteSection.
     * <p>
     *     Méthode qui va supprimer une catégorie.
     * </p>
     *
     * @param mc Mot clé pour la recherche
     * @param page Nombre de page pour la pagination
     * @param size Nombre d'élément par page pour la pagination
     * @param id Identifiant de la catégorie
     * @param locale Langue choisit par l'utilisateur
     *
     * @see SectionRepository
     */
    @PostMapping(path = "/{locale:en|fr|es}/manager/deleteSection")
    public String deleteSection(@RequestParam(name = "mc")String mc,
                                @RequestParam(name = "page")int page,
                                @RequestParam(name = "size")int size,
                                @RequestParam(name = "idSection")Long id,
                                @PathVariable String locale,
                                HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";
        if (this.iLangAccessService.sectionLangAccess(locale, id).getCode() != null)
            this.iLangAccessService.deleteSection(id);

        return String.format("redirect:/"+locale+"/editor/searchSection?mc=%s&page=%d&size=%d", mc, page, size);
    }

    /**
     * Méthode addSection.
     * <p>
     *     Méthode qui va afficher le formulaire permettant d'ajouter une catégorie.
     *     Si l'utilisateur n'a pas le droit de modifier le contenu dans cette langue, il sera redirigé vers une page d'erreur
     *     Sinon on affiche le formulaire avec les langues attribuées à l'utilisateur.
     * </p>
     *
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     * @param request Pour récupérer le username(ensuite vérifier que l'utilisateur a le droit de modification dans cette langue)
     * @param locale Langue choisit par l'utilisateur
     *
     * @see ILangAccessService
     * @see UserRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/manager/addSection")
    public String addSection(Model model, @PathVariable String locale ,HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request)){
            return "redirect:/error";
        }

        Set<Language> languages = new HashSet<>();
        String username = request.getUserPrincipal().getName();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            languages = user.get().getLanguages();
        }

        model.addAttribute("languages", languages);
        model.addAttribute("section", new Section());
        return "rules/forms/addSection";
    }

    /**
     * Méthode addSection.
     * <p>
     *     Méthode qui va ajouter une catégorie si les données fournit sont valides.
     * </p>
     *
     * @param section Objet catégorie à ajouter
     * @param bindingResult Objet fournit par Spring permettant de valider les données founit par l'utilisateur
     * @param locale Langue choisit par l'utilisateur
     *
     * @see SectionRepository
     */
    @PostMapping(path = "/{locale:en|fr|es}/manager/addSection")
    public String addSection(@Validated Section section, BindingResult bindingResult,
                             @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (!bindingResult.hasErrors()){
            for (Section section1 : sectionRepository.findAll()) {
                if (section.equals(section1)) {
                    return "redirect:/"+locale+"/manager/addSection";
                }
            }
        }
        Section section1 = sectionRepository.save(section);
        return "redirect:/"+locale+"/editor/section/"+section1.getId();
    }

    /**
     * Méthode formSection.
     * <p>
     *     Méthode qui va afficher le formulaire permettant de modifier une catégorie.
     *     Si L'Id est est renseigné et que la catégorie existe elle affiche les données de la catégorie à modifier.
     *     Si l'Id ne correspond pas à une catégorie, l'utilisateur est redirigé vers la page affichant toutes des catégories.
     * </p>
     *
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     * @param id Identifiant de la catégorie que l'on souhaite modifier
     * @param locale Langue choisit par l'utilisateur
     * @param request Pour récupérer le username de l'utilisateur
     *
     * @see SectionRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/editor/editSection")
    public String formSection(Model model, @RequestParam(name = "idSection", required = false, defaultValue = "-1") Long id,
                                  @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (id != -1){
            Section section = this.iLangAccessService.sectionLangAccess(locale, id);
            if (section.getCode() != null){
                model.addAttribute("section", section);
            } else {
                model.addAttribute("section", new Section());
            }
        }
        Set<Language> languages = new HashSet<>();
        String username = request.getUserPrincipal().getName();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            languages = user.get().getLanguages();
        }

        model.addAttribute("languages", languages);
        return "rules/forms/editSection";
    }

    /**
     * Méthode editSection.
     * <p>
     *     Méthode qui va ajouter une catégorie si les données fournit sont valides.
     * </p>
     *
     * @param section Objet catégorie à ajouter
     * @param bindingResult Objet fournit par Spring permettant de valider les données founit par l'utilisateur
     * @param locale Langue choisit par l'utilisateur
     *
     * @see SectionRepository
     */
    @PostMapping(path = "/{locale:en|fr|es}/editor/editSection")
    public String editSection(@Validated Section section, BindingResult bindingResult,
                              @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (!bindingResult.hasErrors()){
            Section section1  = this.iLangAccessService.sectionLangAccess(locale, section.getId());
            if (section1.getCode() != null)
                sectionRepository.save(section);
        }
        return "redirect:/"+locale+"/editor/searchSection";
    }

    /**
     * Méthode getiLangAccessService.
     * <p>
     *     Méthode permettant d'injecter le service dans le controller.
     * </p>
     *
     * @see ILangAccessService
     * @see LangAccessServiceImpl
     *
     * @return ILangAccessService
     */
    @Bean
    public ILangAccessService getiLangAccessService(){
        this.iLangAccessService = new LangAccessServiceImpl();
        return iLangAccessService;
    }
}
