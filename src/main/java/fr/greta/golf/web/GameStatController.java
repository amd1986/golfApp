package fr.greta.golf.web;

import fr.greta.golf.dao.CompetitionRepository;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.services.GameStatServicesImpl;
import fr.greta.golf.services.IGameStatServices;
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

@Controller
public class GameStatController {
    private final CompetitionRepository competitionRepository;
    private IGameStatServices gameStatServices;

    public GameStatController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;

    }

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

    @GetMapping(path = "/{locale:en|fr|es}/user/gameRate/collectData")
    public String gameRate(@PathVariable String locale, @RequestParam(name = "idCompet")Long id, Model model){
        Optional<Competition> competition = competitionRepository.findById(id);
        if (competition.isPresent()){
            model.addAttribute("competition", competition.get());
            model.addAttribute("course", competition.get().getCourse());
        }else {
            return "redirect:/"+locale+"/user/gameRate/selectCompetition";
        }
        return "forms/formGameRateData";
    }

    @PostMapping(path = "/{locale:en|fr|es}/user/gameRate/collectData")
    public String gameRate(@RequestParam("times") List<String> times, @RequestParam(name = "idCompet")Long id){
        Competition competition = new Competition();
        Optional<Competition> competition1 = competitionRepository.findById(id);
        if (competition1.isPresent())
           competition = competition1.get();
        this.gameStatServices.saveTimes(competition, times);
        return "forms/formGameRateData";
    }

    @Bean
    public IGameStatServices getGameStatServices(){
        this.gameStatServices = new GameStatServicesImpl();
        return gameStatServices;
    }
}










