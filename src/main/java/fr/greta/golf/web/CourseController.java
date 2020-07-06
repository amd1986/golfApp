package fr.greta.golf.web;

import fr.greta.golf.dao.CourseRepository;
import fr.greta.golf.dao.GolfRepository;
import fr.greta.golf.entities.Course;
import fr.greta.golf.entities.Golf;
import fr.greta.golf.entities.Hole;
import fr.greta.golf.services.LogServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.Optional;

/**
 * <b>CourseController est la classe controller pour l'affichage et la gestion des parcours de golf</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode Get pour afficher un parcours à partir de son Id.</li>
 * <li>Un méthode Get pour afficher les parcours avec un système de pagination.</li>
 * <li>Un méthode Get pour afficher le formulaire d'ajout d'un parcours.</li>
 * <li>Un méthode Get pour afficher le formulaire de modification d'un parcours à partir de son Id.</li>
 * <li>Un méthode Post pour ajouter un parcours.</li>
 * <li>Un méthode Post pour modifier un parcours à partir de son Id.</li>
 * <li>Un méthode Post pour supprimer un parcours à partir de son Id.</li>
 * </ul>
 *
 * @see Course
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class CourseController {
    private final CourseRepository courseRepository;
    private final GolfRepository golfRepository;
    private final LogServices logServices;

    public CourseController(CourseRepository courseRepository, GolfRepository golfRepository, LogServices logServices) {
        this.courseRepository = courseRepository;
        this.golfRepository = golfRepository;
        this.logServices = logServices;
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/course/{id}")
    public String afficherCourse(Model model, @PathVariable(name = "id") Long id){
        Optional<Course> course = courseRepository.findById(id);
        course.ifPresent(value -> model.addAttribute("course", value));
        return "/display/course";
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/searchCourse")
    public String courseView(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                @RequestParam(name = "size", defaultValue = "5", required = false)int size,
                                Model model){
        Page<Course> courses = courseRepository.findByNameContains(mc.trim(), PageRequest.of(page, size));
        if (!courses.hasContent()){
            courses = courseRepository.findByNameContains(mc.trim(), PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[courses.getTotalPages()];
        model.addAttribute("courses", courses.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "search/course";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/deleteCourse")
    public String deleteGolf(@RequestParam(name = "mc")String mc,
                             @RequestParam(name = "page")int page,
                             @RequestParam(name = "size")int size,
                             @RequestParam(name = "idCourse")Long id,
                             @PathVariable String locale,
                             HttpServletRequest request){
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()){
            courseRepository.delete(course.get());
            this.logServices.remove(request, course.get());
        }
        return String.format("redirect:/"+locale+"/user/searchCourse?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/formCourse")
    public String formCourse(@RequestParam(name = "idGolf", required = false, defaultValue = "-1") Long id,
                             @PathVariable String locale, Model model){
        if (id != -1){
            Optional<Golf> golf = golfRepository.findById(id);
            if (golf.isPresent()){
                model.addAttribute("golf", golf.get());
            }
            else {
                return String.format("redirect:/%s/user/searchCourse", locale);
            }
        }else {
            model.addAttribute("golfs", golfRepository.findAll());
        }
        return "/forms/addCourse";
    }

    @Transactional
    @PostMapping(path = "/{locale:en|fr|es}/admin/addCourse")
    public String addCourse(@Validated Course course, BindingResult bindingResult,
                            @PathVariable String locale, HttpServletRequest request){
        if (!bindingResult.hasErrors()){
            for (Course course1 : courseRepository.findAll()){
                if (course.equals(course1))
                    return String.format("redirect:/%s/admin/formCourse", locale);
            }
            if (course.getHoles() != null){
                course.getHoles().forEach(hole -> hole.getCourses().add(course));
            }
            courseRepository.save(course);
            this.logServices.add(request, course);
            return String.format("redirect:/%s/user/course/%d", locale, course.getId());
        }
        return String.format("redirect:/%s/admin/formCourse", locale);
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/editCourse")
    public String editCourse(@RequestParam(name = "idCourse", required = false, defaultValue = "-1") Long id,
                             @PathVariable String locale, Model model){
        if (id != -1){
            Optional<Course> course1 = courseRepository.findById(id);
            if (course1.isPresent()){
                Course course = course1.get();
                model.addAttribute("course", course);
                Optional<Golf> golf = golfRepository.findById(course.getGolf().getId());
                golf.ifPresent(golf1 -> model.addAttribute("golf", golf1));
            }
        } else {
            return String.format("redirect:/%s/user/searchCourse", locale);
        }
        return "/forms/editCourse";
    }

    @Transactional
    @PostMapping(path = "/{locale:en|fr|es}/admin/editCourse")
    public String editCourse(@Validated Course course, BindingResult bindingResult,
                             @PathVariable String locale, HttpServletRequest request){
        if (!bindingResult.hasErrors()){
            Optional<Course> c = courseRepository.findById(course.getId());
            if (c.isPresent()){
                if (c.get().getHoles() != null){
                    for (Hole hole : c.get().getHoles()) {
                        hole.getCourses().remove(c.get());
                    }
                }if (course.getHoles() != null){
                    for (Hole hole : course.getHoles()) {
                        hole.getCourses().add(course);
                    }
                }
                courseRepository.save(course);
                this.logServices.edit(request, course);
            }
        }
        return String.format("redirect:/%s/user/course/%d", locale, course.getId());
    }

}
