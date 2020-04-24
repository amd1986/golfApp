package fr.greta.golf.web;

import fr.greta.golf.dao.GolfRepository;
import fr.greta.golf.dao.WalkRepository;
import fr.greta.golf.entities.Golf;
import fr.greta.golf.entities.WalkTimeBtHoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class TimeBtHolesController {
    private final WalkRepository walkRepository;
    private final GolfRepository golfRepository;

    public TimeBtHolesController(WalkRepository walkRepository, GolfRepository golfRepository) {
        this.walkRepository = walkRepository;
        this.golfRepository = golfRepository;
    }

    @GetMapping(path = "/fr/user/searchTimeBtHoles")
    public String timeBtGamesView(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                @RequestParam(name = "size", defaultValue = "5", required = false)int size,
                                Model model){
        Page<WalkTimeBtHoles> timeBtHoles = walkRepository.findByDescriptionContains(mc.trim(), PageRequest.of(page, size));
        if (!timeBtHoles.hasContent()){
            walkRepository.findByDescriptionContains(mc.trim(), PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[timeBtHoles.getTotalPages()];
        model.addAttribute("timesBtHoles", timeBtHoles.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "search/timeBtHoles";
    }

    @PostMapping(path = "/fr/admin/deleteTimeBtHoles")
    public String deleteGolf(@RequestParam(name = "mc")String mc,
                             @RequestParam(name = "page")int page,
                             @RequestParam(name = "size")int size,
                             @RequestParam(name = "idWalk")Long id){
        golfRepository.deleteById(id);
        return String.format("redirect:/fr/user/searchTimeBtHoles?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/fr/admin/formTimeBtHoles")
    public String addTimeBtHoles(@RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long idGolf, Model model){

        if (idGolf != -1){
            Optional<Golf> golf = golfRepository.findById(idGolf);
            if (golf.isPresent())
                model.addAttribute("golf", golf.get());
            else {
                return "redirect:/fr/user/searchTimeBtHoles";
            }
        }else {
            List<Golf> golfs = golfRepository.findAll();
            model.addAttribute("timeBtHoles", new WalkTimeBtHoles());
            model.addAttribute("golfs", golfs);
        }
        return "/forms/addTimeBtHoles";
    }

    @PostMapping(path = "/fr/admin/addTimeBtHoles")
    public String addTimeBtHoles(@Validated WalkTimeBtHoles timeBtHoles, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            for (WalkTimeBtHoles wtbh : walkRepository.findAll()){
                if (timeBtHoles.equals(wtbh))
                    return "redirect:/fr/admin/formTimeBtHoles";
            }
            walkRepository.save(timeBtHoles);
            return "redirect:/fr/user/searchTimeBtHoles";
        }
        return "redirect:/fr/admin/formTimeBtHoles";

    }

    @GetMapping(path = "/fr/admin/editTimeBtHoles")
    public String editTimeBtHoles(@RequestParam(name = "idWalk", required = false, defaultValue = "-1") Long idWalk, Model model){

        if (idWalk != -1){
            Optional<WalkTimeBtHoles> walkTimeBtHoles = walkRepository.findById(idWalk);
            if (walkTimeBtHoles.isPresent()){
                WalkTimeBtHoles wtbh = walkTimeBtHoles.get();
                model.addAttribute("timeBtHoles", wtbh);
                return "/forms/editTimeBtHoles";
            }
            else {
                return "/forms/addTimeBtHoles";
            }
        }
        return "redirect:/fr/user/searchTimeBtHoles";
    }

    @PostMapping(path = "/fr/admin/editTimeBtHoles")
    public String editTimeBtHoles(WalkTimeBtHoles timeBtHoles){
            walkRepository.save(timeBtHoles);
        return "redirect:/fr/admin/formTimeBtHoles";
    }

}
