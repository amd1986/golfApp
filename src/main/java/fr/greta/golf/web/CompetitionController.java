package fr.greta.golf.web;

import fr.greta.golf.dao.CompetitionRepository;
import fr.greta.golf.dao.GolfRepository;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.entities.Golf;
import fr.greta.golf.services.LogServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
 * <b>CompetitionController est la classe controller pour l'affichage et la gestion des compétitions</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher une compétition à partir de son Id.</li>
 * <li>Un méthode Get pour afficher les compétitions avec un système de pagination.</li>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'une compétition.</li>
 * <li>Un méthode Get pour afficher le formulaire de modification d'une compétition à partir de son Id.</li>
 * <li>Un méthode Post pour ajouter une compétition.</li>
 * <li>Un méthode Post pour modifier une compétition à partir de son Id.</li>
 * <li>Un méthode Post pour supprimer une compétition à partir de son Id.</li>
 * </ul>
 *
 * @see Competition
 * @see CompetitionRepository
 * @see GolfRepository
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class CompetitionController {
    private final CompetitionRepository competitionRepository;
    private final GolfRepository golfRepository;
    private final LogServices logServices;

    /**
     * Constructeur CompetitionController.
     * <p>
     *     On y injecte les repositories pour la gestion des compétitions.
     * </p>
     *
     * @param competitionRepository
     *            Repository pour la gestion des compétitions.
     * @param golfRepository
     *            Repository pour la gestion des golfs.
     *
     * @param logServices Service de journalisation
     * @see CompetitionController#competitionRepository
     * @see CompetitionController#golfRepository
     */
    public CompetitionController(@Qualifier("logService") LogServices logServices,
                                 CompetitionRepository competitionRepository, GolfRepository golfRepository) {

        this.competitionRepository = competitionRepository;
        this.golfRepository = golfRepository;
        this.logServices = logServices;
    }

    /**
     * Méthode competition.
     * <p>
     *     Méthode qui va afficher une compétition.
     * </p>
     *
     * @param id Identifiant de la compétition
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     * @return competition.html qui affiche une compétition
     * @see CompetitionRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/competition/{id}")
    public String competition(Model model, @PathVariable(name = "id") Long id){
        Optional<Competition> competition = competitionRepository.findById(id);
        competition.ifPresent(value -> model.addAttribute("competition", value));
        return "/display/competition";
    }

    /**
     * Méthode displayCompetitions.
     * <p>
     *     Méthode qui va afficher les compétitions avec une recherche par mot clé et une pagination.
     * </p>
     *
     * @param mc Mot clé pour la recherche
     * @param page Nombre de page pour la pagination
     * @param size Nombre d'élément par page pour la pagination
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     * @return competition.html page de gestion des compétitions
     * @see CompetitionRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/searchCompetition")
    public String displayCompetitions(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                      @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                      @RequestParam(name = "size", defaultValue = "5", required = false)int size, Model model){
        Page<Competition> competitions = competitionRepository.findByNameContains(mc, PageRequest.of(page, size));
        if (!competitions.hasContent()){
            competitions = competitionRepository.findByNameContains(mc, PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[competitions.getTotalPages()];
        model.addAttribute("competitions", competitions.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "search/competition";
    }

    /**
     * Méthode deleteCompetition.
     * <p>
     *     Méthode qui va supprimer une compétition.
     * </p>
     *
     * @param mc Mot clé pour la recherche
     * @param page Nombre de page pour la pagination
     * @param size Nombre d'élément par page pour la pagination
     * @param id Identifiant de la compétition
     * @param locale Langue choisit par l'utilisateur
     * @param request On a besoin de récupérer l'utilisateur à partir de HttpServletRequest
     * @return redirect to /{local}/user/searchCompetition
     * @see CompetitionRepository
     */
    /*TODO Le cas où l'utilisateur bidouille les données du formulaire n'a pas été pris en compte*/
    @Transactional
    @PostMapping(path = "/{locale:en|fr|es}/admin/deleteCompetition")
    public String deleteCompetition(@RequestParam(name = "mc")String mc,
                                    @RequestParam(name = "page")int page,
                                    @RequestParam(name = "size")int size,
                                    @RequestParam(name = "idComp")Long id,
                                    @PathVariable String locale,
                                    HttpServletRequest request){
        Optional<Competition> competition = competitionRepository.findById(id);
        if (competition.isPresent()){
            competitionRepository.deleteById(id);
            this.logServices.remove(request, competition.get());
        }
        return String.format("redirect:/"+locale+"/user/searchCompetition?mc=%s&page=%d&size=%d", mc, page, size);
    }

    /**
     * Méthode addCompetition.
     * <p>
     *     Méthode qui va afficher le formulaire permettant d'ajouter une compétition.
     *     Si L'Id est fournit elle affiche les données de la compétition à modifier.
     *     Si l'Id ne correspond pas à une compétition, l'utilisateur est redirigé vers la page affichant toutes des compétitions.
     *     Si l'Id n'est pas renseigné, l'utilisateur est redirigé vers la page d'ajout d'une compétition.
     * </p>
     *
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     * @param id Identifiant de la compétition que l'on souhaite modifier
     * @param locale Langue choisit par l'utilisateur
     * @return addCompetition.html pour afficher le formulaire d'ajout d'une compétition
     * @see GolfRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/admin/formCompetition")
    public String addCompetition(Model model, @RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long id,
                                 @PathVariable String locale){
        if (id != -1){
            Optional<Golf> golf = golfRepository.findById(id);
            if (golf.isPresent()){
                model.addAttribute("golf", golf.get());
            }
            else {
                return String.format("redirect:/%s/user/searchCompetition", locale);
            }
        }else {
            List<Golf> golfs = golfRepository.findAll();
            model.addAttribute("competition", new Competition());
            model.addAttribute("golfs", golfs);
        }
        return "forms/addCompetition";
    }

    /**
     * Méthode addCompetition.
     * <p>
     *     Méthode qui va ajouter une compétition si les données fournit sont valides.
     * </p>
     *
     * @param competition Objet compétition à ajouter
     * @param bindingResult Objet fournit par Spring permettant de valider les données founit par l'utilisateur
     * @param locale Langue choisit par l'utilisateur
     * @param request On a besoin de récupérer l'utilisateur à partir de HttpServletRequest
     * @return redirect to /{local}/admin/formCompetition
     * @see CompetitionRepository
     */
    @PostMapping(path = "/{locale:en|fr|es}/admin/addCompetition")
    public String addCompetition(@Validated Competition competition, BindingResult bindingResult,
                                 @PathVariable String locale, HttpServletRequest request){
        if (!bindingResult.hasErrors()){
            for (Competition competition1 : competitionRepository.findAll()) {
                if (competition.equals(competition1)) {
                    return String.format("redirect:/%s/admin/formCompetition", locale);
                }
            }
            competitionRepository.save(competition);
            this.logServices.add(request, competition);
        }
        return String.format("redirect:/%s/admin/formCompetition", locale);
    }

    /**
     * Méthode formCompetition.
     * <p>
     *     Méthode qui va afficher le formulaire permettant de modifier une compétition.
     *     Si L'Id est est renseigné et que la compétition existe elle affiche les données de la compétition à modifier.
     *     Si l'Id ne correspond pas à une compétition, l'utilisateur est redirigé vers la page affichant toutes des compétitions.
     * </p>
     *
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     * @param id Identifiant de la compétition que l'on souhaite modifier
     * @param locale Langue choisit par l'utilisateur
     * @return editCompetition.html affiche le formulaire de modification d'une compétition
     * @see GolfRepository
     */
    @GetMapping(path = "/{locale:en|fr|es}/admin/editCompetition")
    public String formCompetition(Model model, @RequestParam(name = "idCompet", required = false, defaultValue = "-1") Long id,
                                  @PathVariable String locale){
        if (id != -1){
            Optional<Competition> competition = competitionRepository.findById(id);
            if (competition.isPresent()){
                Competition competition1 = competition.get();
                model.addAttribute("competition", competition1);
                Optional<Golf> golf = golfRepository.findById(competition1.getCourse().getGolf().getId());
                golf.ifPresent(golf1 -> model.addAttribute("courses", golf1.getCourses()));
            }
            else {
                return String.format("redirect:/%s/user/searchCompetition", locale);
            }
        }
        return "forms/editCompetition";
    }

/*TODO  Validation à revoir*/
    /**
     * Méthode addCompetition.
     * <p>
     *     Méthode qui va ajouter une compétition si les données fournit sont valides.
     * </p>
     *
     * @param competition Objet compétition à ajouter
     * @param bindingResult Objet fournit par Spring permettant de valider les données founit par l'utilisateur
     * @param locale Langue choisit par l'utilisateur
     * @param request On a besoin de récupérer l'utilisateur à partir de HttpServletRequest
     * @return redirect to /{local}/admin/formCompetition
     * @see CompetitionRepository
     */
    @PostMapping(path = "/{locale:en|fr|es}/admin/editCompetition")
    public String editCompetition(@Validated Competition competition, BindingResult bindingResult,
                                  @PathVariable String locale, HttpServletRequest request){
        if (!bindingResult.hasErrors()){
            competitionRepository.save(competition);
            this.logServices.edit(request, competition);
            return String.format("redirect:/%s/user/searchCompetition", locale);
        }
        return String.format("redirect:/%s/admin/formCompetition", locale);
    }

}
