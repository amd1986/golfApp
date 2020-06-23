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

@Controller
public class GameRatePdfController {
    private final CompetitionRepository competitionRepository;
    private final CourseRepository courseRepository;
    private IExtractor iExtractor;
    private IGameRateDocument iGameRateDocument;
    private IGameRateServices iGameRateServices;

    @Bean
    public IExtractor getIExtractor(){
        this.iExtractor = new ExtractFromXlsxImpl();
        return this.iExtractor;
    }

    @Bean
    public IGameRateDocument getiGameRateDocument(){
        this.iGameRateDocument = new GameRatePdfImpl();
        return this.iGameRateDocument;
    }

    @Bean
    public IGameRateServices getiGameRateServices(){
        this.iGameRateServices = new GameRateServicesImpl1();
        return this.iGameRateServices;
    }

    public GameRatePdfController(CourseRepository courseRepository, CompetitionRepository competitionRepository) {
        this.courseRepository = courseRepository;
        this.competitionRepository = competitionRepository;

    }

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

    @PostMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/competition")
    public String formCompetition(@PathVariable String locale, @Validated Competition competition,
                                  BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "redirect:/"+locale+"/user/generateCompetPdf/competition";
        } else {
            request.getSession().setAttribute("competition", competition);
            return "redirect:/"+locale+"/user/generateCompetPdf/upload";
        }
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/upload")
    public String uploadCompetitions(@RequestParam(name = "error", defaultValue = "", required = false) String err, Model model) {
        model.addAttribute("error", err);
        return "forms/formUploadFile";
    }

    @PostMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/upload")
    public String uploadCompetition(@PathVariable String locale, @RequestParam(name = "file") MultipartFile file, HttpServletRequest request) {
        List<Player> players = this.iExtractor.extractPlayers(file);
        if (!players.isEmpty()) {
            Competition competition = (Competition) request.getSession().getAttribute("competition");
            this.iGameRateServices.buildGames(competition, players);
            request.getSession().setAttribute("competition", competition);
            return "redirect:/"+locale+"/user/generateCompetPdf/gameRateValidate";
        } else {
            return "redirect:/"+locale+"/user/generateCompetPdf/upload";
        }
    }

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

    @PostMapping(path = "/{locale:en|fr|es}/user/generateCompetPdf/gameRateValidate")
    public void gameRateGeneratePdf(@RequestParam("times")List<String> times, HttpServletRequest request, HttpServletResponse response) {
        Competition competition = (Competition) request.getSession().getAttribute("competition");
        this.iGameRateServices.addOffset(competition, times);
        request.getSession().setAttribute("competition", competition);

        response.setContentType("application/pdf");
        this.iGameRateDocument.generateGameRate(competition, response);
    }
}
