package fr.greta.golf.web;

import fr.greta.golf.dao.SubsectionRepository;
import fr.greta.golf.entities.Section;
import fr.greta.golf.entities.SubSection;
import fr.greta.golf.services.ILangAccessService;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Controller
public class SubsectionController {
    private final ILangAccessService iLangAccessService;
    private final SubsectionRepository subsectionRepository;

    public SubsectionController(@Qualifier("getiLangAccessService") ILangAccessService iLangAccessService,
                                SubsectionRepository subsectionRepository) {
        this.iLangAccessService = iLangAccessService;
        this.subsectionRepository = subsectionRepository;
    }

    @GetMapping(path = "/{locale:en|fr|es}/editor/subsection/{id}")
    public String getSubsection(Model model, @PathVariable(name = "id") Long id,
                                @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        SubSection subSection =  this.iLangAccessService.subsectionLangAccess(locale, id);
        if (subSection.getCode() == null){
            model.addAttribute("errorMsg", "Sous-rubrique introuvable !");
        }else {
            model.addAttribute("subsection", subSection);
        }
        return "/rules/display/subsection";
    }

    @GetMapping(path = "/{locale:en|fr|es}/editor/searchSubsection")
    public String displaySubsections(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                     @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                     @RequestParam(name = "size", defaultValue = "5", required = false)int size, Model model,
                                     @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        Page<SubSection> subSections = subsectionRepository.findByLangAndTitleContains(locale, mc, PageRequest.of(page, size));
        if (!subSections.hasContent()){
            subSections = subsectionRepository.findByLangAndTitleContains(locale, mc, PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[subSections.getTotalPages()];
        model.addAttribute("subsections", subSections.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "rules/search/subsection";
    }

    @PostMapping(path = "/{locale:en|fr|es}/manager/deleteSubsection")
    public String deleteSubsection(@RequestParam(name = "mc")String mc,
                                   @RequestParam(name = "page")int page,
                                   @RequestParam(name = "size")int size,
                                   @RequestParam(name = "idSubsection")Long id,
                                   @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";
        if (this.iLangAccessService.subsectionLangAccess(locale, id).getCode() != null)
            this.iLangAccessService.deleteSubsection(id);

        return String.format("redirect:/"+locale+"/editor/searchSubsection?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/manager/addSubsection")
    public String addSubsection(@RequestParam(name = "idSection", required = false, defaultValue = "-1") Long id,
                                @PathVariable String locale, HttpServletRequest request, Model model){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (id != -1){
            Section section = this.iLangAccessService.sectionLangAccess(locale, id);
            if (section.getCode() != null){
                model.addAttribute("section", section);
                model.addAttribute("subsection", new SubSection());
            }
            else {
                return "redirect:/"+locale+"/editor/searchSubsection";
            }
        }else {
            model.addAttribute("sections", this.iLangAccessService.getAllSectionByLang(locale));
        }
        return "rules/forms/addSubsection";
    }

    @PostMapping(path = "/{locale:en|fr|es}/manager/addSubsection")
    public String addSubsection(@Validated SubSection subSection, BindingResult bindingResult,
                                @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (!bindingResult.hasErrors()){
            for (SubSection subSection1 : subsectionRepository.findAll()) {
                if (subSection.equals(subSection1)) {
                    return "redirect:/"+locale+"/manager/addSubsection";
                }
            }
            subSection.setDescription(this.iLangAccessService.cleanHtml(subSection.getDescription()));
            subsectionRepository.save(subSection);
        }
        return "redirect:/"+locale+"/manager/addSubsection";
    }

    @GetMapping(path = "/{locale:en|fr|es}/editor/editSubsection")
    public String formSubsection(Model model, @RequestParam(name = "idSubsection", required = false, defaultValue = "-1") Long id,
                                 @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (id != -1){
            SubSection subSection  = this.iLangAccessService.subsectionLangAccess(locale, id);
            if (subSection.getCode() != null){
                model.addAttribute("subsection", subSection);
                return "rules/forms/editSubsection";
            }
        }
        return "redirect:/"+locale+"/editor/searchSubsection";
    }

    @PostMapping(path = "/{locale:en|fr|es}/editor/editSubsection")
    public String editSubsection(@Validated SubSection subSection, BindingResult bindingResult,
                                 @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (!bindingResult.hasErrors()){
            SubSection subSection1  = this.iLangAccessService.subsectionLangAccess(locale, subSection.getId());
            if (subSection1.getCode() != null){
                subSection.setDescription(this.iLangAccessService.cleanHtml(subSection.getDescription()));
                subsectionRepository.save(subSection);
            }
        }
        return "redirect:/"+locale+"/editor/searchSubsection";
    }
}
