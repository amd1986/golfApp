package fr.greta.golf.services;

import fr.greta.golf.entities.Competition;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IGameStatServices {
    void saveTimes(Competition competition, List<String> times);
    List<String> getTimesAverage(Competition competition, List<String> timesG);
}
