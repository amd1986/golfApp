package fr.greta.golf.rules.web;

import fr.greta.golf.rules.entities.Rule;
import fr.greta.golf.rules.entities.Section;
import fr.greta.golf.rules.entities.SubSection;
import fr.greta.golf.rules.services.GenerateRuleDocServiceImpl;
import fr.greta.golf.rules.services.IGenerateRuleDocService;
import fr.greta.golf.rules.services.ILangAccessService;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class PublicRuleController {
    private IGenerateRuleDocService IGenerateRuleDocService;
    private ILangAccessService iLangAccessService;

    public PublicRuleController(@Qualifier("getiLangAccessService") ILangAccessService iLangAccessService) {
        this.iLangAccessService = iLangAccessService;
    }

    @GetMapping(path = "/{locale:en|fr|es}")
    public String allRules(@PathVariable(name = "locale") String locale, Model model){
        List<Section> sections = this.iLangAccessService.getAllSectionByLang(locale);
        model.addAttribute("sections", sections);
        return "index";

    }

    @GetMapping(path = "/{locale:en|fr|es}/ruleList")
    public String selectedRules(Model model, @PathVariable String locale,
                                  @RequestParam(name = "rules", defaultValue = "null") Set<Long> ruleListId){
        if (ruleListId != null){
            Set<Rule> ruleSet = new HashSet<>();
            for (Long ruleId: ruleListId){
                Rule r = this.iLangAccessService.ruleLangAccess(locale, ruleId);
                if (r != null){
                    ruleSet.add(r);
                }
            }
            model.addAttribute("rules", ruleSet);
            return "ruleSelected";
        }
        return "redirect:/"+locale;
    }

    @PostMapping(path = "/{locale:en|fr|es}/generateDoc")
    public void generateRuleDoc(@PathVariable String locale, HttpServletResponse response,
                                  @RequestParam List<Long> rules) throws JAXBException, Docx4JException, IOException {
        if (rules != null){
            Set<Rule> ruleSet = new HashSet<>();
            Set<Section> sectionSet = new HashSet<>();
            Set<SubSection> subsectionSet = new HashSet<>();
            for (Long ruleId: rules){
                Rule r = this.iLangAccessService.ruleLangAccess(locale, ruleId);
                if (r != null){
                    ruleSet.add(r);
                    sectionSet.add(r.getSubSection().getSection());
                    subsectionSet.add(r.getSubSection());
                }
            }
            WordprocessingMLPackage wordMLPackage = this.IGenerateRuleDocService.generateDocxFromHtml(sectionSet, subsectionSet, ruleSet);
            if (wordMLPackage != null){
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                response.setHeader("content-disposition","filename=LocalGolfRules.doc");
                wordMLPackage.save(response.getOutputStream());
            }
        }
    }

    @Bean
    public IGenerateRuleDocService getIGenerateRuleDocService(){
        this.IGenerateRuleDocService = new GenerateRuleDocServiceImpl();
        return IGenerateRuleDocService;
    }
}
