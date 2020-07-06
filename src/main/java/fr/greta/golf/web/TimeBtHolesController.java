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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * <b>TimeBtHolesController est la classe controller pour l'affichage et la gestion des temps de marche entre les trous</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher un temps de marche à partir de son Id.</li>
 * <li>Un méthode Get pour afficher les temps de marche avec un système de pagination.</li>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'un temps de marche.</li>
 * <li>Un méthode Get pour afficher le formulaire de modification d'un temps de marche à partir de son Id.</li>
 * <li>Un méthode Post pour ajouter un temps de marche.</li>
 * <li>Un méthode Post pour modifier un temps de marche à partir de son Id.</li>
 * <li>Un méthode Post pour supprimer un temps de marche à partir de son Id.</li>
 * </ul>
 *
 * @see WalkTimeBtHoles
 * @see WalkRepository
 * @see GolfRepository
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class TimeBtHolesController {
    private final WalkRepository walkRepository;
    private final GolfRepository golfRepository;

    public TimeBtHolesController(WalkRepository walkRepository, GolfRepository golfRepository) {
        this.walkRepository = walkRepository;
        this.golfRepository = golfRepository;
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/searchTimeBtHoles")
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

    @PostMapping(path = "/{locale:en|fr|es}/admin/deleteTimeBtHoles")
    public String deleteGolf(@RequestParam(name = "mc")String mc,
                             @RequestParam(name = "page")int page,
                             @RequestParam(name = "size")int size,
                             @RequestParam(name = "idWalk")Long id,
                             @PathVariable String locale){
        golfRepository.deleteById(id);
        return String.format("redirect:/"+locale+"/user/searchTimeBtHoles?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/formTimeBtHoles")
    public String addTimeBtHoles(@RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long idGolf,
                                 @PathVariable String locale, Model model){

        if (idGolf != -1){
            Optional<Golf> golf = golfRepository.findById(idGolf);
            if (golf.isPresent())
                model.addAttribute("golf", golf.get());
            else {
                return String.format("redirect:/%s/user/searchTimeBtHoles", locale);
            }
        }else {
            List<Golf> golfs = golfRepository.findAll();
            model.addAttribute("timeBtHoles", new WalkTimeBtHoles());
            model.addAttribute("golfs", golfs);
        }
        return "/forms/addTimeBtHoles";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/addTimeBtHoles")
    public String addTimeBtHoles(@PathVariable String locale, @Validated WalkTimeBtHoles timeBtHoles, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            for (WalkTimeBtHoles wtbh : walkRepository.findAll()){
                if (timeBtHoles.equals(wtbh))
                    return String.format("redirect:/%s/admin/formTimeBtHoles", locale);
            }
            walkRepository.save(timeBtHoles);
            return String.format("redirect:/%s/user/searchTimeBtHoles", locale);
        }
        return String.format("redirect:/%s/admin/formTimeBtHoles", locale);

    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/editTimeBtHoles")
    public String editTimeBtHoles(@RequestParam(name = "idWalk", required = false, defaultValue = "-1") Long idWalk,
                                  @PathVariable String locale, Model model){

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
        return String.format("redirect:/%s/user/searchTimeBtHoles", locale);
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/editTimeBtHoles")
    public String editTimeBtHoles(@PathVariable String locale, WalkTimeBtHoles timeBtHoles){
            walkRepository.save(timeBtHoles);
        return String.format("redirect:/%s/admin/formTimeBtHoles", locale);
    }

}
