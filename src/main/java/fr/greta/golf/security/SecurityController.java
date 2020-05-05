package fr.greta.golf.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {

    @GetMapping(path = "/{locale:en|fr|es}")
    public String index(){
        return "main";
    }

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
}
