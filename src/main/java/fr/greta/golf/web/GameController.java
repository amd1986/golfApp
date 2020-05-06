package fr.greta.golf.web;

import fr.greta.golf.dao.CompetitionRepository;
import fr.greta.golf.dao.GameRepository;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.entities.Game;
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

import java.util.List;
import java.util.Optional;

@Controller
public class GameController {
    private final GameRepository gameRepository;
    private final CompetitionRepository competitionRepository;

    public GameController(GameRepository gameRepository, CompetitionRepository competitionRepository) {
        this.gameRepository = gameRepository;
        this.competitionRepository = competitionRepository;
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/game/{id}")
    public String game(Model model, @PathVariable(name = "id") Long id){
        Optional<Game> game = gameRepository.findById(id);
        game.ifPresent(value -> model.addAttribute("game", value));
        return "/display/game";
    }

    @GetMapping(path = "/{locale:en|fr|es}/user/searchGame")
    public String games(@RequestParam(name = "mc", defaultValue = "", required = false)String mc,
                                @RequestParam(name = "page", defaultValue = "0", required = false)int page,
                                @RequestParam(name = "size", defaultValue = "5", required = false)int size,
                                Model model){
        Page<Game> games = gameRepository.findByNameContains(mc.trim(), PageRequest.of(page, size));
        if (!games.hasContent()){
            games = gameRepository.findByNameContains(mc.trim(), PageRequest.of(0, 5));
            page = 0;
        }
        int[] pages = new int[games.getTotalPages()];
        model.addAttribute("games", games.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("mc", mc);
        model.addAttribute("pages", pages);
        return "search/game";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/deleteGame")
    public String deleteGame(@RequestParam(name = "mc")String mc,
                             @RequestParam(name = "page")int page,
                             @RequestParam(name = "size")int size,
                             @RequestParam(name = "idGame")Long id,
                             @PathVariable String locale){
        gameRepository.deleteById(id);
        return String.format("redirect:/"+locale+"/user/searchGame?mc=%s&page=%d&size=%d", mc, page, size);
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/formGame")
    public String addGame(Model model, @RequestParam(name = "idCompet", required = false, defaultValue = "-1") Long id,
                          @PathVariable String locale){
        if (id != -1){
            Optional<Competition> competition = competitionRepository.findById(id);
            if (competition.isPresent()) {
                model.addAttribute("competition", competition.get());
            } else {
                return "redirect:/"+locale+"/user/searchGame";
            }
        }else {
            List<Competition> competitions = competitionRepository.findAll();
            model.addAttribute("game", new Game());
            model.addAttribute("competitions", competitions);
        }
        return "forms/addGame";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/addGame")
    public String addGame(@PathVariable String locale, @Validated Game game, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            for (Game game1 : gameRepository.findAll()){
                if (game.equals(game1))
                    return "redirect:/fr/admin/formGame";
            }
            gameRepository.save(game);
            return "redirect:/"+locale+"/user/searchGame";
        }
        return "redirect:/"+locale+"/admin/formGame";
    }

    @GetMapping(path = "/{locale:en|fr|es}/admin/editGame")
    public String editGame(Model model, @RequestParam(name = "idGame", required = false, defaultValue = "-1") Long id,
                           @PathVariable String locale){
        if (id != -1){
            Optional<Game> game = gameRepository.findById(id);
            if (game.isPresent()){
                model.addAttribute("game", game.get());
                Competition competition = game.get().getCompetition();
                model.addAttribute("competition", competition);
                return "forms/editGame";
            }
            else {
                return "forms/addGame";
            }
        }
        return "redirect:/"+locale+"/user/searchGame";
    }

    @PostMapping(path = "/{locale:en|fr|es}/admin/editGame")
    public String editGame(@PathVariable String locale, @Validated Game game, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            gameRepository.save(game);
            return "redirect:/"+locale+"/user/searchGame";
        }else {
            return "redirect:/"+locale+"/admin/formGame";
        }
    }

}
