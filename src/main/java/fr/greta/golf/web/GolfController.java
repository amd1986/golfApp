package fr.greta.golf.web;

import fr.greta.golf.dao.CourseRepository;
import fr.greta.golf.dao.GolfRepository;
import fr.greta.golf.dao.HoleRepository;
import fr.greta.golf.entities.Course;
import fr.greta.golf.entities.Golf;
import fr.greta.golf.entities.Hole;
import fr.greta.golf.services.LogServices;
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
import java.util.List;
import java.util.Optional;

/**
 * <b>GolfController est la classe controller pour l'affichage et la gestion des golfs</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher un golf à partir de son Id.</li>
 * <li>Un méthode Get pour afficher les golfs avec un système de pagination.</li>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'un golf.</li>
 * <li>Un méthode Get pour afficher le formulaire de modification d'un golf à partir de son Id.</li>
 * <li>Un méthode Post pour ajouter un golf.</li>
 * <li>Un méthode Post pour modifier un golf à partir de son Id.</li>
 * <li>Un méthode Post pour supprimer un golf à partir de son Id.</li>
 * </ul>
 *
 * @see Golf
 * @see GolfRepository
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class GolfController {
    private final GolfRepository golfRepository;
    private final CourseRepository courseRepository;
    private final HoleRepository holeRepository;
    private final LogServices logServices;

    /**
     * Constructeur GolfController.
     * <p>
     *     On y injecte les repositories pour la gestion des golfs.
     * </p>
     *
     * @param golfRepository
     *            Repository pour la gestion des golfs.
     *
     * @param courseRepository dao gestion des parcours
     * @param holeRepository dao gestion des trous
     * @param logServices Service de journalisation
     * @see GolfController#golfRepository
     */
    public GolfController(GolfRepository golfRepository, CourseRepository courseRepository, HoleRepository holeRepository, LogServices logServices) {
        this.golfRepository = golfRepository;
        this.courseRepository = courseRepository;
        this.holeRepository = holeRepository;
        this.logServices = logServices;
    }

    /**
     * Méthode golf.
     * <p>
     *     Méthode qui va afficher un golf.
     * </p>
     *
     * @param id Identifiant du golf
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     *
     * @see GolfRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/golf/{id}")
    public String golf(Model model, @PathVariable(name = "id") Long id){
        Optional<Golf> golf = golfRepository.findById(id);
        golf.ifPresent(value -> model.addAttribute("golf", value));
        return "/display/golf";
    }

    /**
     * Méthode golfs.
     * <p>
     *     Méthode qui va afficher les golfs avec une recherche par mot clé et une pagination.
     * </p>
     *
     * @param mc Mot clé pour la recherche
     * @param page Nombre de page pour la pagination
     * @param size Nombre d'élément par page pour la pagination
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     *
     * @see GolfRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/searchGolf")
    public String golfs(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                @RequestParam(name = "size", defaultValue = "5", required = false)int size,
                                Model model){
        Page<Golf> golfs = golfRepository.chercherParMotCle("%"+mc.toLowerCase()+"%", PageRequest.of(page, size));
        if (!golfs.hasContent()){
            golfs = golfRepository.chercherParMotCle("%"+mc.toLowerCase()+"%", PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[golfs.getTotalPages()];
        model.addAttribute("golfs", golfs.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "/search/golf";
    }

    /**
     * Méthode deleteGolf.
     * <p>
     *     Méthode qui va supprimer un golf.
     * </p>
     *
     * @param mc Mot clé pour la recherche
     * @param page Nombre de page pour la pagination
     * @param size Nombre d'élément par page pour la pagination
     * @param id Identifiant de la golf
     * @param locale Langue choisit par l'utilisateur
     * @param request On a besoin de récupérer l'utilisateur à partir de HttpServletRequest
     * @see GolfRepository
     */
    @PostMapping(path = "/{locale:en|fr|es}/admin/deleteGolf")
    public String deleteGolf(@RequestParam(name = "mc")String mc,
                             @RequestParam(name = "page")int page,
                             @RequestParam(name = "size")int size,
                             @RequestParam(name = "idGolf")Long id,
                             @PathVariable String locale,
                             HttpServletRequest request){

        Optional<Golf> golf = golfRepository.findById(id);
        if (golf.isPresent()){
            for (Course course: golf.get().getCourses()){
                courseRepository.delete(course);
                for (Hole hole: course.getHoles()){
                    holeRepository.delete(hole);
                }
            }
            golfRepository.delete(golf.get());
            this.logServices.remove(request, golf.get());
        }
        return String.format("redirect:/"+locale+"/user/searchGolf?mc=%s&page=%d&size=%d", mc, page, size);
    }

    /**
     * Méthode formGolf.
     * <p>
     *     Méthode qui va afficher le formulaire permettant d'ajouter un golf.
     *     Si L'Id est fournit elle affiche les données du golf à modifier.
     *     Si l'Id ne correspond pas à un golf, l'utilisateur est redirigé vers la page affichant toutes les golfs.
     *     Si l'Id n'est pas renseigné, l'utilisateur est redirigé vers la page d'ajout d'un golf.
     * </p>
     *
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     * @param id Identifiant du golf que l'on souhaite modifier
     * @param locale Langue choisit par l'utilisateur
     *
     * @see GolfRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/admin/formGolf")
    public String formGolf(Model model, @RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long id,
                           @PathVariable String locale){
        if (id != -1){
            Optional<Golf> golf = golfRepository.findById(id);
            if (golf.isPresent())
                model.addAttribute("golf", golf.get());
            else {
                return String.format("redirect:/%s/user/searchGolf", locale);
            }
        }else {
            List<Golf> golfs = golfRepository.findAll();
            model.addAttribute("golf", new Golf());
            model.addAttribute("golfs", golfs);
        }
        return "/forms/addGolf";
    }

    /**
     * Méthode addGolf.
     * <p>
     *     Méthode qui va ajouter ou modifier un golf si les données fournit sont valides.
     * </p>
     *
     * @param golf Objet golf à ajouter ou à modifier
     * @param bindingResult Objet fournit par Spring permettant de valider les données fournit par l'utilisateur
     * @param locale Langue choisit par l'utilisateur
     * @param request On a besoin de récupérer l'utilisateur à partir de HttpServletRequest
     * @see GolfRepository
     */
    @PostMapping(path = "/{locale:en|fr|es}/admin/editGolf")
    public String addGolf(@PathVariable String locale, HttpServletRequest request,
                          @Validated Golf golf, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            for (Golf golf1 : golfRepository.findAll()){
                if (golf.equals(golf1)){
                    return String.format("redirect:/%s/admin/formGolf", locale);
                }
            }
            golfRepository.save(golf);
            this.logServices.edit(request, golf);
            return String.format("redirect:/%s/user/golf/%d", locale, golf.getId());
        }else {
            return String.format("redirect:/%s/user/searchGolf", locale);
        }
    }

}
