package fr.greta.golf.web;

import fr.greta.golf.dao.GolfRepository;
import fr.greta.golf.dao.HoleRepository;
import fr.greta.golf.entities.Golf;
import fr.greta.golf.entities.Hole;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <b>HoleController est la classe controller pour l'affichage et la gestion des trous de golf</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher un trou à partir de son Id.</li>
 * <li>Un méthode Get pour afficher les trous avec un système de pagination.</li>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'un trou.</li>
 * <li>Un méthode Get pour afficher le formulaire de modification d'un trou à partir de son Id.</li>
 * <li>Un méthode Post pour ajouter un trou.</li>
 * <li>Un méthode Post pour modifier un trou à partir de son Id.</li>
 * <li>Un méthode Post pour supprimer un trou à partir de son Id.</li>
 * </ul>
 *
 * @see Hole
 * @see HoleRepository
 * @see GolfRepository
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class HoleController {
    private final HoleRepository holeRepository;
    private final GolfRepository golfRepository;

    public HoleController(HoleRepository holeRepository, GolfRepository golfRepository) {
        this.holeRepository = holeRepository;
        this.golfRepository = golfRepository;
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/hole/{id}")
    public String afficherGolf(Model model,
                               @PathVariable(name = "id") Long id,
                               @PathVariable String locale){
        Optional<Hole> hole = holeRepository.findById(id);
        hole.ifPresent(value -> model.addAttribute("hole", value));
        return "/display/hole";
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/searchHole")
    public String tableHoles(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                             @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                             @RequestParam(name = "size", defaultValue = "5", required = false)int size,
                                Model model){
        Page<Hole> holes = holeRepository.findByNameContains(mc.trim(), PageRequest.of(page, size));
        if (!holes.hasContent()){
            holes = holeRepository.findByNameContains(mc.trim(), PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[holes.getTotalPages()];
        model.addAttribute("holes", holes.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "/search/hole";
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/deleteHole")
    public String deleteHole(Model model, @RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long id){
        if (id != -1){
            Optional<Golf> golf = golfRepository.findById(id);
            if (golf.isPresent()){
                List<Hole> holes = new ArrayList<>();
                Golf golf1 = golf.get();
                golf1.getHoles().forEach(hole -> {
                    if (hole.getCourses().isEmpty())
                        holes.add(hole);
                });
                model.addAttribute("holes", holes);
            }
        }else {
            List<Golf> golfs = golfRepository.findAll();
            model.addAttribute("hole", new Hole());
            model.addAttribute("golfs", golfs);
        }
        return "/forms/deleteHole";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/deleteHole")
    public String deleteHole(@RequestParam(name = "idHole")Long id, @PathVariable String locale){
        holeRepository.deleteById(id);
        return String.format("redirect:/%s/user/searchHole", locale);
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/addHole")
    public String addHole(Model model, @RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long id,
                          @PathVariable String locale){
        if (id != -1){
            Optional<Golf> golf = golfRepository.findById(id);
            if (golf.isPresent()){
                model.addAttribute("golf", golf.get());
            }else {
                return String.format("redirect:/%s/user/searchHole", locale);
            }
        }else {
            List<Golf> golfs = golfRepository.findAll();
            model.addAttribute("hole", new Hole());
            model.addAttribute("golfs", golfs);
        }
        return "/forms/addHole";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/addHole")
    public String addHole(@PathVariable String locale, @Validated Hole hole, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            for (Hole hole1 : holeRepository.findAll()){
                if (hole.equals(hole1))
                    return String.format("redirect:/%s/admin/addHole", locale);
            }
            holeRepository.save(hole);
        }
        return String.format("redirect:/%s/admin/addHole", locale);
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/editHole")
    public String editHole(Model model, @RequestParam(name = "idHole", required = false, defaultValue = "-1") Long id,
                           @PathVariable String locale){
        Optional<Hole> hole1 = holeRepository.findById(id);
        if (hole1.isPresent()){
            Hole hole = hole1.get();
            model.addAttribute("hole", hole);
            Optional<Golf> golf = golfRepository.findById(hole.getGolf().getId());
            golf.ifPresent(golf1 -> model.addAttribute("golf", golf1));
        }else {
            return String.format("redirect:/%s/admin/addHole", locale);
        }
        return "/forms/editHole";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/editHole")
    public String editHole(@PathVariable String locale, @Validated Hole hole, BindingResult bindingResult){
        if (!bindingResult.hasErrors())
            holeRepository.save(hole);
        return String.format("redirect:/%s/admin/addHole", locale);
    }

}
