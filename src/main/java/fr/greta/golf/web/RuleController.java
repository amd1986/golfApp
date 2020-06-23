package fr.greta.golf.web;

import fr.greta.golf.dao.RuleRepository;
import fr.greta.golf.entities.Rule;
import fr.greta.golf.entities.Section;
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
public class RuleController {
    private final ILangAccessService iLangAccessService;
    private final RuleRepository ruleRepository;

    public RuleController(@Qualifier("getiLangAccessService") ILangAccessService iLangAccessService,
                          RuleRepository ruleRepository) {
        this.iLangAccessService = iLangAccessService;
        this.ruleRepository = ruleRepository;
    }

    @GetMapping(path = "/{locale:en|fr|es}/editor/rule/{id}")
    public String getRule(Model model, @PathVariable(name = "id") Long id,
                          @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        Rule rule = this.iLangAccessService.ruleLangAccess(locale, id);
        if (rule.getCode() == null){
            model.addAttribute("errorMsg", "Sous-rubrique introuvable !");
        }else {
            model.addAttribute("rule", rule);
        }

        return "/rules/display/rule";
    }

    @GetMapping(path = "/{locale:en|fr|es}/editor/searchRule")
    public String displayRules(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                               @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                               @RequestParam(name = "size", defaultValue = "5", required = false)int size, Model model,
                               @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        Page<Rule> rules = ruleRepository.findByLangAndTitleContains(locale, mc, PageRequest.of(page, size));
        if (!rules.hasContent()){
            rules = ruleRepository.findByLangAndTitleContains(locale, mc, PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[rules.getTotalPages()];
        model.addAttribute("rules", rules.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "rules/search/rules";
    }

    @PostMapping(path = "/{locale:en|fr|es}/manager/deleteRule")
    public String deleteRule(@RequestParam(name = "mc")String mc,
                             @RequestParam(name = "page")int page,
                             @RequestParam(name = "size")int size,
                             @RequestParam(name = "idRule")Long id,
                             @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";
        if (this.iLangAccessService.ruleLangAccess(locale, id).getCode() != null)
            ruleRepository.deleteById(id);

        return String.format("redirect:/"+locale+"/editor/searchRule?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/manager/addRule")
    public String addRule(@RequestParam(name = "idSection", required = false, defaultValue = "-1") Long id,
                          @PathVariable String locale, HttpServletRequest request, Model model){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (id != -1){
            Section section = this.iLangAccessService.sectionLangAccess(locale, id);
            if (section.getCode() != null){
                model.addAttribute("section", section);
                model.addAttribute("rule", new Rule());
            }
            else {
                return "redirect:/"+locale+"/editor/searchRule";
            }
        }else {
            model.addAttribute("sections", this.iLangAccessService.getAllSectionByLang(locale));
        }
        return "rules/forms/addRule";
    }

    @PostMapping(path = "/{locale:en|fr|es}/manager/addRule")
    public String addRule(@Validated Rule rule, BindingResult bindingResult,
                          @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (!bindingResult.hasErrors()){
            for (Rule rule1 : ruleRepository.findAll()) {
                if (rule.equals(rule1)) {
                    return "redirect:/"+locale+"/manager/addRule";
                }
            }
            rule.setText(this.iLangAccessService.cleanHtml(rule.getText()));
            ruleRepository.save(rule);
        }
        return "redirect:/"+locale+"/manager/addRule";
    }

    @GetMapping(path = "/{locale:en|fr|es}/editor/editRule")
    public String formRule(@RequestParam(name = "idRule", required = false, defaultValue = "-1") Long id,
                           @PathVariable String locale, HttpServletRequest request, Model model){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (id != -1){
            Rule rule = this.iLangAccessService.ruleLangAccess(locale, id);
            if (rule.getCode() != null){
                model.addAttribute("rule", rule);
            }
            else {
                return "redirect:/"+locale+"/editor/searchRule";
            }
        }
        return "rules/forms/editRule";
    }

    @PostMapping(path = "/{locale:en|fr|es}/editor/editRule")
    public String editRule(@Validated Rule rule, BindingResult bindingResult,
                           @PathVariable String locale, HttpServletRequest request){
        if (this.iLangAccessService.langAccess(locale, request))
            return "redirect:/error";

        if (!bindingResult.hasErrors()){
            Rule rule1 = this.iLangAccessService.ruleLangAccess(locale, rule.getId());
            if (rule1.getCode() != null){
                rule.setText(this.iLangAccessService.cleanHtml(rule.getText()));
                ruleRepository.save(rule);
            }
        }
        return "redirect:/"+locale+"/editor/searchRule";
    }
}
