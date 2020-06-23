package fr.greta.golf.web;

import fr.greta.golf.dao.UserRepository;
import fr.greta.golf.entities.Language;
import fr.greta.golf.entities.User;
import fr.greta.golf.dao.SectionRepository;
import fr.greta.golf.entities.Section;
import fr.greta.golf.services.ILangAccessService;
import fr.greta.golf.services.LangAccessServiceImpl;
import org.springframework.context.annotation.Bean;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class SectionController {
    private ILangAccessService iLangAccessService;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;

    public SectionController(SectionRepository sectionRepository, UserRepository userRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
    }

    /*TODO Faire l'affichage d'eereur suite à la validation */

    @GetMapping(path = "/{locale:en|fr|es}/editor/section/{id}")
    public String getSection(Model model, @PathVariable(name = "id") Long id,
                             @PathVariable String locale ,HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        Section section =  this.iLangAccessService.sectionLangAccess(locale, id);
        if (section.getCode() == null){
            model.addAttribute("errorMsg", "Rubrique introuvable !");
        }else {
            model.addAttribute("section", section);
        }
        return "/rules/display/section";
    }

    @GetMapping(path = "/{locale:en|fr|es}/editor/searchSection")
    public String displaySections(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                  @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                  @RequestParam(name = "size", defaultValue = "5", required = false)int size, Model model,
                                  @PathVariable String locale ,HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        Page<Section> sections = sectionRepository.findByLangAndTitleContains(locale, mc, PageRequest.of(page, size));
        if (!sections.hasContent()) {
            sections = sectionRepository.findByLangAndTitleContains(locale, mc, PageRequest.of(0, 5));
            page = 0;
        }

        int[] pages = new int[sections.getTotalPages()];
        model.addAttribute("sections", sections.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "rules/search/section";
    }

    @PostMapping(path = "/{locale:en|fr|es}/manager/deleteSection")
    public String deleteSection(@RequestParam(name = "mc")String mc,
                                @RequestParam(name = "page")int page,
                                @RequestParam(name = "size")int size,
                                @RequestParam(name = "idSection")Long id,
                                @PathVariable String locale,
                                HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";
        if (this.iLangAccessService.sectionLangAccess(locale, id).getCode() != null)
            this.iLangAccessService.deleteSection(id);

        return String.format("redirect:/"+locale+"/editor/searchSection?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/manager/addSection")
    public String addSection(Model model, @PathVariable String locale ,HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request)){
            return "redirect:/error";
        }

        Set<Language> languages = new HashSet<>();
        String username = request.getUserPrincipal().getName();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            languages = user.get().getLanguages();
        }

        model.addAttribute("languages", languages);
        model.addAttribute("section", new Section());
        return "rules/forms/addSection";
    }

    @PostMapping(path = "/{locale:en|fr|es}/manager/addSection")
    public String addSection(@Validated Section section, BindingResult bindingResult,
                             @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (!bindingResult.hasErrors()){
            for (Section section1 : sectionRepository.findAll()) {
                if (section.equals(section1)) {
                    return "redirect:/"+locale+"/manager/addSection";
                }
            }
        }
        Section section1 = sectionRepository.save(section);
        return "redirect:/"+locale+"/editor/section/"+section1.getId();
    }

    @GetMapping(path = "/{locale:en|fr|es}/editor/editSection")
    public String formSection(Model model, @RequestParam(name = "idSection", required = false, defaultValue = "-1") Long id,
                                  @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (id != -1){
            Section section = this.iLangAccessService.sectionLangAccess(locale, id);
            if (section.getCode() != null){
                model.addAttribute("section", section);
            } else {
                model.addAttribute("section", new Section());
            }
        }
        Set<Language> languages = new HashSet<>();
        String username = request.getUserPrincipal().getName();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            languages = user.get().getLanguages();
        }

        model.addAttribute("languages", languages);
        return "rules/forms/editSection";
    }

    @PostMapping(path = "/{locale:en|fr|es}/editor/editSection")
    public String editSection(@Validated Section section, BindingResult bindingResult,
                              @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (!bindingResult.hasErrors()){
            Section section1  = this.iLangAccessService.sectionLangAccess(locale, section.getId());
            if (section1.getCode() != null)
                sectionRepository.save(section);
        }
        return "redirect:/"+locale+"/editor/searchSection";
    }

    @Bean
    public ILangAccessService getiLangAccessService(){
        this.iLangAccessService = new LangAccessServiceImpl();
        return iLangAccessService;
    }
}
