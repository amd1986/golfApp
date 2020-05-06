package fr.greta.golf.web;

import fr.greta.golf.dao.CompetitionRepository;
import fr.greta.golf.dao.TimesRepository;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.entities.TimePerHPerG;
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

import java.util.List;
import java.util.Optional;

@Controller
public class TimeController {
    private final TimesRepository timesRepository;
    private final CompetitionRepository competitionRepository;

    public TimeController(TimesRepository timesRepository, CompetitionRepository competitionRepository) {
        this.timesRepository = timesRepository;
        this.competitionRepository = competitionRepository;
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/time/{id}")
    public String searchTime(Model model, @PathVariable(name = "id") Long id){
        Optional<TimePerHPerG> timePerHPerG = timesRepository.findById(id);
        timePerHPerG.ifPresent(value -> model.addAttribute("time", value));
        return "/display/time";
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/searchTime")
    public String searchTime(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                             @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                             @RequestParam(name = "size", defaultValue = "5", required = false)int size,
                                Model model){
        Page<TimePerHPerG> timesPerHPerG = timesRepository.chercherParMC("%"+mc.trim()+"%", PageRequest.of(page, size));
        if (!timesPerHPerG.hasContent()){
            timesPerHPerG = timesRepository.chercherParMC("%"+mc.trim()+"%", PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[timesPerHPerG.getTotalPages()];
        model.addAttribute("times", timesPerHPerG.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "/search/time";
    }

  @GetMapping(path = "/{locale:en|fr|es}/admin/addTime")
    public String addTime(Model model, @RequestParam(name = "idCompet", required = false, defaultValue = "-1") Long id,
                          @PathVariable String locale){
        if (id != -1){
            Optional<Competition> competition = competitionRepository.findById(id);
            if (competition.isPresent()){
                model.addAttribute("competition", competition.get());
                model.addAttribute("course", competition.get().getCourse());
            }else {
                return "redirect:/"+locale+"/user/searchTime";
            }
        }else {
            List<Competition> competitions = competitionRepository.findAll();
            model.addAttribute("time", new TimePerHPerG());
            model.addAttribute("competitions", competitions);
        }
        return "/forms/addTime";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/addTime")
    public String addTime(@PathVariable String locale, @Validated TimePerHPerG timePerHPerG, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            for (TimePerHPerG perHPerG : timesRepository.findAll()){
                if (timePerHPerG.equals(perHPerG))
                    timePerHPerG.setId(perHPerG.getId());
            }
            timesRepository.save(timePerHPerG);
        }
        return "redirect:/"+locale+"/admin/addTime";
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/editTime")
    public String editTime(Model model, @RequestParam(name = "idTime", required = false, defaultValue = "-1") Long id,
                           @PathVariable String locale){
        Optional<TimePerHPerG> perHPerG = timesRepository.findById(id);
        if (perHPerG.isPresent()){
            TimePerHPerG timePerHPerG = perHPerG.get();
            model.addAttribute("time", timePerHPerG);
            return "/forms/editTime";
        }else {
            return "redirect:/"+locale+"/admin/addTime";
        }
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/editTime")
    public String editTime(@PathVariable String locale, @Validated TimePerHPerG timePerHPerG, BindingResult bindingResult){
        if (!bindingResult.hasErrors())
            timesRepository.save(timePerHPerG);
        return "redirect:/"+locale+"/admin/addHole";
    }
}
