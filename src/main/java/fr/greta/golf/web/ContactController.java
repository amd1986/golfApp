package fr.greta.golf.web;

import fr.greta.golf.dao.ContactRepository;
import fr.greta.golf.entities.Contact;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {
    private ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @GetMapping(path = "/fr/user/contactAdmin/sendRequest")
    public String formCompetition(@RequestParam(name = "msg", defaultValue = "", required = false) String msg, Model model) {
        model.addAttribute("msg", msg);
        return "forms/formAdminRequest";
    }

    @PostMapping(path = "/fr/user/contactAdmin/sendRequest")
    public String formCompetition(@Validated Contact contact, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()){
            if (contactRepository.findAll().size() < 100){
                contact.setMessage(contact.getMessage().replaceAll("[<>;}{|@#&]", ""));
                contactRepository.save(contact);
            }
        }
        return "redirect:/fr/user/contactAdmin/sendRequest";
    }
}
