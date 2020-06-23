package fr.greta.golf.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class SecurityController {

    @GetMapping(path = "/login")
    public String login(Model model, @RequestParam(required = false, defaultValue = "") String logout){
        model.addAttribute("logout", logout);
        return "/security/login";
    }

    @GetMapping(path = "/forgottenPassword")
    public String forgottenPassword(){
        return "/security/forgot-password";
    }

    @GetMapping(path = "/register")
    public String register(){ return "/security/register"; }

    @GetMapping(path = "/")
    public String index(HttpServletRequest request){
        Locale locale = request.getLocale();
        String[] locals = {"fr", "es", "en"};
        for (String local: locals){
            if (locale.toString().equals(local) ){
                return "redirect:/"+local;
            }
        }
        return "redirect:/en";
    }
}
