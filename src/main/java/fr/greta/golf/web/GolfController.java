package fr.greta.golf.web;

import fr.greta.golf.dao.GolfRepository;
import fr.greta.golf.entities.Golf;
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
public class GolfController {
    private final GolfRepository golfRepository;

    public GolfController(GolfRepository golfRepository) {
        this.golfRepository = golfRepository;
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/golf/{id}")
    public String golf(Model model, @PathVariable(name = "id") Long id){
        Optional<Golf> golf = golfRepository.findById(id);
        golf.ifPresent(value -> model.addAttribute("golf", value));
        return "/display/golf";
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/searchGolf")
    public String golfs(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                @RequestParam(name = "size", defaultValue = "5", required = false)int size,
                                Model model){
        Page<Golf> golfs = golfRepository.chercherParMotCle("%"+mc.toLowerCase()+"%", PageRequest.of(page, size));
        if (!golfs.hasContent()){
            golfs = golfRepository.chercherParMotCle("%"+mc.toLowerCase()+"%", PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[golfs.getTotalPages()];
        model.addAttribute("golfs", golfs.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "/search/golf";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/deleteGolf")
    public String deleteGolf(@RequestParam(name = "mc")String mc,
                             @RequestParam(name = "page")int page,
                             @RequestParam(name = "size")int size,
                             @RequestParam(name = "idGolf")Long id,
                             @PathVariable String locale){
        golfRepository.deleteById(id);
        return String.format("redirect:/"+locale+"/user/searchGolf?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/formGolf")
    public String formGolf(Model model, @RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long id,
                           @PathVariable String locale){
        if (id != -1){
            Optional<Golf> golf = golfRepository.findById(id);
            if (golf.isPresent())
                model.addAttribute("golf", golf.get());
            else {
                return "redirect:/"+locale+"/user/searchGolf";
            }
        }else {
            List<Golf> golfs = golfRepository.findAll();
            model.addAttribute("golf", new Golf());
            model.addAttribute("golfs", golfs);
        }
        return "/forms/addGolf";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/editGolf")
    public String addGolf(@PathVariable String locale, @Validated Golf golf, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            for (Golf golf1 : golfRepository.findAll()){
                if (golf.equals(golf1)){
                    return "redirect:/"+locale+"/admin/formGolf";
                }
            }
            golfRepository.save(golf);
            return "redirect:/"+locale+"/user/golf/"+golf.getId();
        }else {
            return "redirect:/"+locale+"/user/searchGolf";
        }
    }

}
