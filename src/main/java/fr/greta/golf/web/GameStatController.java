package fr.greta.golf.web;

import fr.greta.golf.dao.CompetitionRepository;
import fr.greta.golf.dao.CourseRepository;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * <b>GameStatController est la classe controller pour la gestion des statistiques</b>
 * <br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher un formulaire avec la liste des compétitions.</li>
 * <li>Un méthode Get pour récupérer les temps d'une compétition.</li>
 * <li>Un méthode Post pour enregistrer les temps.</li>
 * </ul>
 *
 * @see Competition
 * @see CompetitionRepository
 * @see IGameStatServices
 * @see GameStatServicesImpl
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class GameStatController {
    private final CompetitionRepository competitionRepository;
    private IGameStatServices gameStatServices;

    /**
     * Méthode IGameStatServices.
     * <p>
     *     Méthode permettant d'injecter le service dans le controller.
     * </p>
     *
     * @see IGameStatServices
     * @see GameStatServicesImpl
     *
     * @return IGameStatServices
     */
    @Bean
    public IGameStatServices getGameStatServices(){
        this.gameStatServices = new GameStatServicesImpl();
        return gameStatServices;
    }

    /**
     * Constructeur GameStatController.
     * <p>
     *     On injecte dans le controller les repository pour la gestion des compétitions.
     * </p>
     *
     * @param competitionRepository dao gestion des compétitions
     * @see CompetitionRepository
     */
    public GameStatController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;

    }

    /**
     * Méthode selectCompetition.
     * <p>
     *     Méthode qui va les compétitions afin que l'utilisateur en sélectionne une.
     * </p>
     *
     * @param mc Mot clé
     * @param page pagination
     * @param size pagination
     * @param model Pour envoyer les données à la vue
     *
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/gameRate/selectCompetition")
    public String selectCompetition(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                      @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                      @RequestParam(name = "size", defaultValue = "5", required = false)int size,
                                      Model model){
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
        return "forms/formGameRateSelect";
    }

    /**
     * Méthode gameRate.
     * <p>
     *     Méthode qui va afficher les temps de la compétition.
     * </p>
     *
     * @param id Id de la compétition
     * @param model Pour envoyer les données à la vue
     * @param locale Langue choisit par l'utilisateur
     *
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/gameRate/collectData")
    public String gameRate(@PathVariable String locale, @RequestParam(name = "idCompet")Long id, Model model){
        Optional<Competition> competition = competitionRepository.findById(id);
        if (competition.isPresent()){
            model.addAttribute("competition", competition.get());
            model.addAttribute("course", competition.get().getCourse());
        }else {
            return String.format("redirect:/%s/user/gameRate/selectCompetition", locale);
        }
        return "forms/formGameRateData";
    }

    /**
     * Méthode gameRate.
     * <p>
     *     Méthode qui va afficher les temps de la compétition.
     * </p>
     *
     * @param id Id de la compétition
     * @param times Les temps à persister
     *
     */
    @PostMapping(path = "/{locale:en|fr|es}/user/gameRate/collectData")
    public String gameRate(@RequestParam("times") List<String> times, @RequestParam(name = "idCompet")Long id){
        Competition competition = new Competition();
        Optional<Competition> competition1 = competitionRepository.findById(id);
        if (competition1.isPresent())
           competition = competition1.get();
        this.gameStatServices.saveTimes(competition, times);
        return "forms/formGameRateData";
    }
    /*TODO pas de redirection, mauvais code*/
}










