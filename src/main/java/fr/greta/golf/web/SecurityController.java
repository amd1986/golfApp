package fr.greta.golf.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * <b>SecurityController est la classe controller pour la gestion des routes non protégées.</b>
 * <p>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher la page d'acceuil en fonction de la langue locale.</li>
 * <li>Un méthode Get pour afficher la page de connexion.</li>
 * <li>Un méthode Get pour afficher la page d'ajout d'utilisateur.</li>
 * <li>Un méthode Get pour afficher la page de mot de passe oublié.</li>
 * </ul>
 * </p>
 *
 * @author ahmed
 * @version 1.1.0
 */
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
    /*TODO problème : l'affichage ne se fait pas en fonction de la lague locale au premier accès, à corriger*/
}
