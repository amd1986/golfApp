package fr.greta.golf.web;
import fr.greta.golf.dao.LangRepository;
import fr.greta.golf.dao.RoleRepository;
import fr.greta.golf.dao.UserRepository;
import fr.greta.golf.entities.Language;
import fr.greta.golf.entities.Role;
import fr.greta.golf.entities.User;
import fr.greta.golf.services.LogServices;
import fr.greta.golf.services.LogServicesImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * <b>HoleController est la classe controller pour l'affichage et la gestion des trous de golf</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher un trou à partir de son Id.</li>
 * <li>Un méthode Get pour afficher les trous avec un système de pagination.</li>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'un trou.</li>
 * <li>Un méthode Get pour afficher le formulaire de modification d'un trou à partir de son Id.</li>
 * <li>Un méthode Post pour ajouter un trou.</li>
 * <li>Un méthode Post pour modifier un trou à partir de son Id.</li>
 * <li>Un méthode Post pour supprimer un trou à partir de son Id.</li>
 * </ul>
 *
 * @see User
 * @see UserRepository
 * @see RoleRepository
 * @see LangRepository
 * @see fr.greta.golf.config.WebMvcConfig
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class UserController {
    /*dkfjlgdz16556&&&*/
    private LogServices logServices;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LangRepository langRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, LangRepository langRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.langRepository = langRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean(name = "logService")
    public LogServices getLogServices(){
        this.logServices = new LogServicesImpl();
        return logServices;
    }

    @GetMapping(path = "/{locale:en|fr|es}/superAdmin/user/{username}")
    public String competition(Model model, @PathVariable(name = "username") String username){
        Optional<User> user = userRepository.findById(username);
        user.ifPresent(value -> model.addAttribute("user", value));
        return "/display/user";
    }

    @GetMapping(path = "/{locale:en|fr|es}/superAdmin/searchUser")
    public String displayUsers(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                      @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                      @RequestParam(name = "size", defaultValue = "5", required = false)int size, Model model){
        Page<User> users = userRepository.findByUsernameContains(mc, PageRequest.of(page, size));
        if (!users.hasContent()){
            users = userRepository.findByUsernameContains(mc, PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[users.getTotalPages()];
        model.addAttribute("users", users.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "search/user";
    }

    @Transactional
    @PostMapping(path = "/{locale:en|fr|es}/superAdmin/deleteUser")
    public String deleteUser(@RequestParam(name = "mc")String mc,
                             @RequestParam(name = "page")int page,
                             @RequestParam(name = "size")int size,
                             @RequestParam(name = "idUser")String id,
                             @PathVariable String locale, HttpServletRequest request){

        Optional<User> user1 = userRepository.findById(id);
        if (user1.isPresent()){
            User user = user1.get();
            for (Role role: roleRepository.findAll()){
                role.getUsers().remove(user);
                roleRepository.save(role);
            }

            for (Language lang: langRepository.findAll()){
                lang.getUsers().remove(user);
                langRepository.save(lang);
            }

            userRepository.deleteById(id);
            this.logServices.remove(request, user);
        }
        return String.format("redirect:/"+locale+"/superAdmin/searchUser?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/superAdmin/formUser")
    public String formUser(@RequestParam(name = "idUser", required = false, defaultValue = "-1") String id,
                          @RequestParam(name = "err", required = false) String err,
                          @PathVariable String locale, Model model){

        List<Role> roles = roleRepository.findAll();
        List<Language> languages = langRepository.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("languages", languages);
        model.addAttribute("err", err);

        if (!id.equals("-1")){
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()){
                model.addAttribute("user", user.get());
            }
            else {
                return String.format("redirect:/%s/superAdmin/searchUser", locale);
            }
        }else {
            model.addAttribute("user", new User());
        }
        return "forms/addUser";
    }

    @Transactional
    @PostMapping(path = "/{locale:en|fr|es}/superAdmin/formUser")
    public String formUser(@PathVariable String locale, @Validated User user, BindingResult bindingResult,
                          @RequestParam(required = false, defaultValue = "") Set<String> roles,
                          @RequestParam(required = false, defaultValue = "") Set<String> languages,
                          @RequestParam(name = "up", required = false, defaultValue = "") String id,
                          HttpServletRequest request){

        if (!bindingResult.hasErrors()){
            User u = user;
            if (id.equals("")){
                for (User user1 : userRepository.findAll()){
                    if (user.equals(user1)){
                        return String.format("redirect:/%s/superAdmin/searchUser?mc=%s", locale, user1.getUsername());
                    }
                }
                user.setPassword(this.passwordEncoder.encode(user.getPassword()));
                u = userRepository.save(user);
            }
            if (!roles.isEmpty() && !languages.isEmpty()){

                Set<Role> roleSet = new HashSet<>();
                for (Role role: roleRepository.findAll()){
                    for (String r: roles){
                        if (r.equals(role.getRole())){
                            roleSet.add(role);
                        }
                    }
                    role.getUsers().remove(u);
                    roleRepository.save(role);
                }
                for (Role role: roleSet){
                    role.getUsers().add(u);
                    roleRepository.save(role);
                }

                Set<Language> languageSet = new HashSet<>();
                for (Language lang: langRepository.findAll()){
                    for (String l: languages){
                        if (l.equals(lang.getLanguage())){
                            languageSet.add(lang);
                        }
                    }
                    lang.getUsers().remove(u);
                    langRepository.save(lang);
                }
                for (Language language: languageSet){
                    language.getUsers().add(u);
                    langRepository.save(language);
                }

                this.logServices.edit(request, user);
                return String.format("redirect:/%s/superAdmin/user/%s", locale, user.getUsername());
            }else {
                return String.format("redirect:/%s/superAdmin/formUser?err=%s", locale, "select");
            }

        }else {
            return String.format("redirect:/%s/superAdmin/formUser?err=%s", locale, Objects.requireNonNull(bindingResult.getFieldError()).getField());
         }
    }

}
