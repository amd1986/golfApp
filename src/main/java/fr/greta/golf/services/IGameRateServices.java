package fr.greta.golf.services;

import fr.greta.golf.entities.Competition;
import fr.greta.golf.models.Player;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IGameRateServices {
    void buildGames(Competition competition, List<Player> players);
    void buildTimes(Competition competition, Long id);
    void addOffset(Competition competition, List<String> times);
}
