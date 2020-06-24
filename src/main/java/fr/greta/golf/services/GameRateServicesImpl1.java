package fr.greta.golf.services;

import fr.greta.golf.dao.CourseRepository;
import fr.greta.golf.dao.GameRepository;
import fr.greta.golf.dao.WalkRepository;
import fr.greta.golf.entities.*;
import fr.greta.golf.models.Player;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <b>ExtractFromXlsxImpl est la classe implémentant IGameRateServices pour configurer la compétition.</b>
 * <p>
 *     Cette classe fournit les méthodes permettant de générer les parties et ajusté leur temps.
 * </p>
 *
 * @see IGameRateServices
 * @see CourseRepository
 * @see WalkRepository
 * @see GameRepository
 *
 * @author ahmed
 * @version 1.1.0
 */
public class GameRateServicesImpl1 implements IGameRateServices {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private WalkRepository walkRepository;
    @Autowired
    private GameRepository gameRepository;

    /**
     * Méthode buildGames.
     * <p>
     *     Méthode qui va construire la liste des parties.
     * </p>
     *
     * @param competition Compétition sélectionnée par l'utilisateur
     * @param players Joueurs participant à la compétition
     *
     */
    @Override
    public void buildGames(Competition competition, List<Player> players) {
        Game game = new Game();
        game.setPlayers(new ArrayList<>());
        competition.setGames(new ArrayList<>());
        int nbPlayers = competition.getNbPlayersByGame();
        int num = 0;
        List<Player> gamePlayers = new ArrayList<>();
        List<Game> games = gameRepository.findAll();
        for (Player player : players) {
            if (gamePlayers.size() == nbPlayers) {
                game.setCompetition(competition);
                game.setNum(num + 1);
                game.setName("Partie " + (num + 1));
                boolean add = true;
                for (Game game1: games){
                    if (game.equals(game1)) {
                        add = false;
                        break;
                    }
                }
                if (add) gameRepository.save(game);
                game.setPlayers(gamePlayers);
                competition.getGames().add(game);
                game = new Game();
                gamePlayers = new ArrayList<>();
                num++;
            }
            gamePlayers.add(player);
        }
    }

    /**
     * Méthode buildTimes.
     * <p>
     *     Méthode qui va ajouté les temps de chaque partie à la compétition.
     * </p>
     *
     * @param competition Compétition sélectionnée par l'utilisateur
     * @param id Identifiant de la compétition
     *
     */
    @Override
    public void buildTimes(Competition competition, Long id) {
        List<Game> games = competition.getGames();
        Course course = courseRepository.findById(id).get();
        List<Hole> holes = course.getHoles();

        String departure = competition.getDepartureHour();
        String[] tab = departure.split(":");
        int tps = Integer.parseInt(tab[0]) * 60 + Integer.parseInt(tab[1]);

        int balles = competition.getNbPlayersByGame();
        int interval = competition.getIntervalBtGames();
        List<Integer> times = new ArrayList<>();
        times.add(tps);

        for (int h = 0; h < holes.size(); h++) {
            int par = holes.get(h).getPar();
            int offset = holes.get(h).getOffset();
            int wtbh = 0;
            if (h > 0) {
                Optional<WalkTimeBtHoles> walkTimeBtHoles = walkRepository.chercherParTrous(holes.get(h), holes.get(h - 1));
                if (walkTimeBtHoles.isPresent()) {
                    wtbh = walkTimeBtHoles.get().getWalkTime();
                }
            }

            if (par == 3 && balles == 3) {
                tps += 11;
            } else if (par == 4 && balles == 3) {
                tps += 14;
            } else if (par == 5 && balles == 3) {
                tps += 17;
            } else if (par == 3) {
                tps += 9;
            } else if (par == 4) {
                tps += 11;
            } else {
                tps += 14;
            }
            tps += offset + wtbh;
            times.add(tps);
        }
        List<Game> allGames = gameRepository.findAll();
        List<TimePerHPerG> timesphpg = new ArrayList<>();
        TimePerHPerG perHPerG = new TimePerHPerG();
        for (int i = 0; i < games.size(); i++){
            for (int t = 0; t < times.size(); t++){
                int time = times.get(t) + i*interval;
                String strTime = String.format("%02d:%02d", time / 60, time % 60);
                if (t == 0){
                    games.get(i).setDhour(strTime);
                    for (Game game: allGames){
                        if (games.get(i).equals(game)){
                            games.get(i).setId(game.getId());
                            break;
                        }
                    }
                    gameRepository.save(games.get(i));
                }else {
                    perHPerG.setTime(strTime);
                    perHPerG.setGame(games.get(i));
                    if (t < times.size()-1)
                    perHPerG.setHole(holes.get(t));
                    timesphpg.add(perHPerG);
                    perHPerG = new TimePerHPerG();
                }
            }
            games.get(i).setTimesPerHPerG(timesphpg);
            timesphpg = new ArrayList<>();
        }

        course.setHoles(holes);
        competition.setCourse(course);
        competition.setGames(games);
    }

    /**
     * Méthode addOffset.
     * <p>
     *     Méthode qui va ajuster les temps de chaque partie à la compétition.
     * </p>
     *
     * @param competition Compétition sélectionnée par l'utilisateur
     * @param times Liste des temps ajustés par l'utilisateur
     *
     */
    @Override
    public void addOffset(Competition competition, List<String> times) {
        int interval = competition.getIntervalBtGames();
        for (int j = 0; j < competition.getGames().size(); j++){
            List<TimePerHPerG> timesPerHPerG = competition.getGames().get(j).getTimesPerHPerG();
            for (int i = 0; i < competition.getGames().get(j).getTimesPerHPerG().size(); i++){
                String[] str = times.get(i).split(":");
                int intTime = (Integer.parseInt(str[0]) * 60) + Integer.parseInt(str[1]) + (interval*j) ;
                timesPerHPerG.get(i).setTime(String.format("%02d:%02d", intTime / 60, intTime % 60));
            }
        }
    }
}
