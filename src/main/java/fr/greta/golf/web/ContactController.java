package fr.greta.golf.web;

import fr.greta.golf.dao.ContactRepository;
import fr.greta.golf.entities.Contact;
import fr.greta.golf.services.LogServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * <b>ContactController est la classe controller pour l'affichage et la gestion des messages pour l'admin</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'un message pour l'admin.</li>
 * <li>Un méthode Post pour ajouter un message pour l'admin.</li>
 * </ul>
 *
 * @see Contact
 * @see ContactRepository
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class ContactController {
    private ContactRepository contactRepository;
    private final LogServices logServices;

    public ContactController(ContactRepository contactRepository, LogServices logServices) {
        this.contactRepository = contactRepository;
        this.logServices = logServices;
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/contactAdmin/sendRequest")
    public String formCompetition(@RequestParam(name = "msg", defaultValue = "", required = false) String msg, Model model, @PathVariable String locale) {
        model.addAttribute("msg", msg);
        return "forms/formAdminRequest";
    }

    @PostMapping(path = "/{locale:en|fr|es}/user/contactAdmin/sendRequest")
    public String formCompetition(@PathVariable String locale, HttpServletRequest request,
                                  @Validated Contact contact, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()){
            if (contactRepository.findAll().size() < 100){
                contact.setMessage(contact.getMessage().replaceAll("[<>;}{|@#&]", ""));
                contactRepository.save(contact);
                this.logServices.edit(request, contact);
            }
        }
        return String.format("redirect:/%s/user/contactAdmin/sendRequest", locale);
    }
}
