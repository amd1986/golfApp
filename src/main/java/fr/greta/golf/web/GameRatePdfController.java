package fr.greta.golf.web;

import fr.greta.golf.dao.CompetitionRepository;
import fr.greta.golf.dao.CourseRepository;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.entities.Course;
import fr.greta.golf.models.Player;
import fr.greta.golf.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * <b>GameRatePdfController est la classe controller pour la génération de document Pdf de candence de jeu</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher un formulaire avec la liste des compétitions.</li>
 * <li>Un méthode Post pour éditer les données d'une compétition.</li>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'un fichier Excel(liste des joueurs FFGOLF).</li>
 * <li>Un méthode Post pour la gestion du fichier Excel envoyé par l'utilisateur.</li>
 * <li>Un méthode Get pour calculer les temps et les afficher à l'utilisateur.</li>
 * <li>Un méthode Post pour générer le document Pdf de cadence de jeu.</li>
 * </ul>
 *
 * @see Competition
 * @see CompetitionRepository
 * @see CourseRepository
 * @see IExtractor
 * @see IGameRateServices
 * @see IGameRateDocument
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class GameRatePdfController {
    private final CompetitionRepository competitionRepository;
    private final CourseRepository courseRepository;
    private IExtractor iExtractor;
    private IGameRateDocument iGameRateDocument;
    private IGameRateServices iGameRateServices;

    /**
     * Méthode getIExtractor.
     * <p>
     *     Méthode permettant d'injecter le service dans le controller.
     * </p>
     *
     * @see IExtractor
     * @see ExtractFromXlsxImpl
     *
     * @return IExtractor
     */
    @Bean
    public IExtractor getIExtractor(){
        this.iExtractor = new ExtractFromXlsxImpl();
        return this.iExtractor;
    }

    /**
     * Méthode getiGameRateDocument.
     * <p>
     *     Méthode permettant d'injecter le service dans le controller.
     * </p>
     *
     * @see IGameRateDocument
     * @see GameRatePdfImpl
     *
     * @return IGameRateDocument
     */
    @Bean
    public IGameRateDocument getiGameRateDocument(){
        this.iGameRateDocument = new GameRatePdfImpl();
        return this.iGameRateDocument;
    }

    /**
     * Méthode getiGameRateServices.
     * <p>
     *     Méthode permettant d'injecter le service dans le controller.
     * </p>
     *
     * @see IGameRateServices
     * @see GameRateServicesImpl1
     *
     * @return IGameRateDocument
     */
    @Bean
    public IGameRateServices getiGameRateServices(){
        this.iGameRateServices = new GameRateServicesImpl1();
        return this.iGameRateServices;
    }

    /**
     * Constructeur GameRatePdfController.
     * <p>
     *     On injecte dans le controller les repositories pour la gestion des parcours et des compétitions.
     * </p>
     * @param competitionRepository dao gestion des compétitions
     * @param courseRepository dao gestion des parcours
     * @see CourseRepository
     * @see CompetitionRepository
     */
    public GameRatePdfController(CourseRepository courseRepository, CompetitionRepository competitionRepository) {
        this.courseRepository = courseRepository;
        this.competitionRepository = competitionRepository;

    }

    /**
     * Méthode formCompetition.
     * <p>
     *     Méthode qui va afficher la liste des compétitions dans un formulaire, pour que l'utilisateur choisissent une compétion.
     * </p>
     *
     * @param id Identifiant de la compétition
     * @param request On a besoin de récupérer l'utilisateur à partir de HttpServletRequest
     * @param model Objet fournit par Spring pour envoyer des données à la vue
     * @see CompetitionRepository
     * @return formCompetition.html affichage du formulaire de sélection d'une compétition
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/competition")
    public String formCompetition(@RequestParam(name = "idCompet", defaultValue = "-1", required = false) Long id,
                                  HttpServletRequest request, Model model) {
        if (id == -1) {
            List<Course> courses = courseRepository.findAll();
            model.addAttribute("courses", courses);
        } else {
            Optional<Competition> c = competitionRepository.findById(id);
            c.ifPresent(competition -> {
                model.addAttribute("competition", competition);
                request.getSession().setAttribute("courseId", competition.getCourse().getId());
            });
        }
        return "forms/formCompetition";
    }

    /**
     * Méthode formCompetition.
     * <p>
     *     Méthode qui va redirigé l'utilisateur en cas d'erreur et afficher le formulaire pour uploader le fichier Excel.
     * </p>
     *
     * @param locale Langue choisit par l'utilisateur
     * @param competition Objet compétition sélectionné par l'utilisateur
     * @param bindingResult Objet de Spring permettant de valider les données
     * @param request Pour enregistrer les données dans la session utilisateur
     *
     */
    @PostMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/competition")
    public String formCompetition(@PathVariable String locale, @Validated Competition competition,
                                  BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/%s/user/generateCompetPdf/competition", locale);
        } else {
            request.getSession().setAttribute("competition", competition);
            return String.format("redirect:/%s/user/generateCompetPdf/upload", locale);
        }
    }

    /**
     * Méthode uploadCompetitions.
     * <p>
     *     Méthode qui va le formulaire pour uploader le fichier Excel.
     * </p>
     *
     * @param err Message d'erreur
     * @param model Objet de Spring pour envoyer des données à la vue
     *
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/upload")
    public String uploadCompetitions(@RequestParam(name = "error", defaultValue = "", required = false) String err, Model model) {
        model.addAttribute("error", err);
        return "forms/formUploadFile";
    }

    /**
     * Méthode uploadCompetition.
     * <p>
     *     Méthode qui va extraire les données du fichier Excel, construire les partie de la compétition
     *     et enregistrer les données dans la session.
     * </p>
     *
     * @param locale Langue choisit par l'utilisateur
     * @param file Fichier Excel envoyé par l'utilisateur
     * @param request Pour enregistrer les données dans la session utilisateur
     *
     */
    @PostMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/upload")
    public String uploadCompetition(@PathVariable String locale, @RequestParam(name = "file") MultipartFile file, HttpServletRequest request) {
        List<Player> players = this.iExtractor.extractPlayers(file);
        if (!players.isEmpty()) {
            Competition competition = (Competition) request.getSession().getAttribute("competition");
            this.iGameRateServices.buildGames(competition, players);
            request.getSession().setAttribute("competition", competition);
            return String.format("redirect:/%s/user/generateCompetPdf/gameRateValidate", locale);
        } else {
            return String.format("redirect:/%s/user/generateCompetPdf/upload", locale);
        }
    }

    /**
     * Méthode gameRateValidate.
     * <p>
     *     Méthode qui va afficher les temps calculés pour chaque trou.
     * </p>
     *
     * @param request Pour récupérer les données dans la session
     * @param model Objet de Spring pour envoyer des données à la vue
     *
     */
    @GetMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/gameRateValidate")
    public String gameRateValidate(HttpServletRequest request, Model model) {
        Competition competition = (Competition) request.getSession().getAttribute("competition");
        Long courseId = (Long) request.getSession().getAttribute("courseId");
        this.iGameRateServices.buildTimes(competition, courseId);
        request.getSession().setAttribute("competition", competition);

        model.addAttribute("competition", competition);
        model.addAttribute("game", competition.getGames().get(0));
        return "forms/formGameRateValidate";
    }

    /**
     * Méthode gameRateGeneratePdf.
     * <p>
     *     Méthode qui va générer le fichier Pdf de cadence de jeu.
     * </p>
     *
     * @param request Pour récupérer les données dans la session
     * @param response Pour envoyer le document dans l'objet HttpServletResponse
     * @param times Ceux sont les temps ajustés par l'utilisateur
     *
     */
    @PostMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/gameRateValidate")
    public void gameRateGeneratePdf(@RequestParam("times")List<String> times, HttpServletRequest request, HttpServletResponse response) {
        Competition competition = (Competition) request.getSession().getAttribute("competition");
        this.iGameRateServices.addOffset(competition, times);
        request.getSession().setAttribute("competition", competition);

        response.setContentType("application/pdf");
        response.setHeader("content-disposition","filename=CadenceDeJeu.pdf");
        this.iGameRateDocument.generateGameRate(competition, response);
    }
}
