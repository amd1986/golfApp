package fr.greta.golf.web;

import fr.greta.golf.dao.CompetitionRepository;
import fr.greta.golf.dao.GolfRepository;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.entities.Golf;
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

import java.util.List;
import java.util.Optional;

@Controller
public class CompetitionController {
    private final CompetitionRepository competitionRepository;
    private final GolfRepository golfRepository;

    public CompetitionController(CompetitionRepository competitionRepository, GolfRepository golfRepository) {
        this.competitionRepository = competitionRepository;
        this.golfRepository = golfRepository;
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/competition/{id}")
    public String competition(Model model, @PathVariable(name = "id") Long id){
        Optional<Competition> competition = competitionRepository.findById(id);
        competition.ifPresent(value -> model.addAttribute("competition", value));
        return "/display/competition";
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/searchCompetition")
    public String displayCompetitions(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
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
        return "search/competition";
    }

    @Transactional
    @PostMapping(path = "/fr/admin/deleteCompetition")
    public String deleteCompetition(@RequestParam(name = "mc")String mc,
                                    @RequestParam(name = "page")int page,
                                    @RequestParam(name = "size")int size,
                                    @RequestParam(name = "idComp")Long id){
        competitionRepository.deleteById(id);
        return String.format("redirect:/fr/user/searchCompetition?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/formCompetition")
    public String addCompetition(Model model, @RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long id){
        if (id != -1){
            Optional<Golf> golf = golfRepository.findById(id);
            if (golf.isPresent()){
                model.addAttribute("golf", golf.get());
            }
            else {
                return "redirect:/fr/user/searchCompetition";
            }
        }else {
            List<Golf> golfs = golfRepository.findAll();
            model.addAttribute("competition", new Competition());
            model.addAttribute("golfs", golfs);
        }
        return "forms/addCompetition";
    }

    @PostMapping(path = "/fr/admin/addCompetition")
    public String addCompetition(@Validated Competition competition, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            for (Competition competition1 : competitionRepository.findAll()) {
                if (competition.equals(competition1)) {
                    return "redirect:/fr/admin/formCompetition";
                }
            }
            competitionRepository.save(competition);
        }
        return "redirect:/fr/admin/formCompetition";
    }

    @GetMapping(path = "/f{locale:en|fr|es}r/admin/editCompetition")
    public String formCompetition(Model model, @RequestParam(name = "idCompet", required = false, defaultValue = "-1") Long id){
        if (id != -1){
            Optional<Competition> competition = competitionRepository.findById(id);
            if (competition.isPresent()){
                Competition competition1 = competition.get();
                model.addAttribute("competition", competition1);
                Optional<Golf> golf = golfRepository.findById(competition1.getCourse().getGolf().getId());
                golf.ifPresent(golf1 -> model.addAttribute("courses", golf1.getCourses()));
            }
            else {
                return "redirect:/fr/user/searchCompetition";
            }
        }
        return "forms/editCompetition";
    }

    @PostMapping(path = "/fr/admin/editCompetition")
    public String editCompetition(@Validated Competition competition, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            competitionRepository.save(competition);
        }
        return "redirect:/fr/admin/formCompetition";
    }

}
