package fr.greta.golf.services;

import fr.greta.golf.dao.TimesRepository;
import fr.greta.golf.entities.Competition;
import fr.greta.golf.entities.Game;
import fr.greta.golf.entities.Hole;
import fr.greta.golf.entities.TimePerHPerG;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class GameStatServicesImpl implements IGameStatServices {
    @Autowired
    private TimesRepository timesRepository;

    @Override
    public void saveTimes(Competition competition, List<String> timesG) {
        List<Hole> holes = competition.getCourse().getHoles();
        List<Game> games = competition.getGames();

        List<TimePerHPerG> timesPerHPerG = timesRepository.findAll();
        int t = 0;
        for (Game game: games){
            for (Hole hole: holes){
                TimePerHPerG timePerHPerG = new TimePerHPerG();
                if (t<timesG.size())
                    timePerHPerG.setTime(timesG.get(t));
                timePerHPerG.setHole(hole);
                timePerHPerG.setGame(game);
                boolean add = true;
                for (TimePerHPerG perHPerG: timesPerHPerG){
                    if (timePerHPerG.equals(perHPerG)){
                        add = false;
                        break;
                    }
                }
                if (add) timesRepository.save(timePerHPerG);
                t++;
            }
        }
    }

    @Override
    public List<String> getTimesAverage(Competition competition, List<String> timesG) {
        int sizeG = competition.getGames().size();
        int sizeH = competition.getCourse().getHoles().size();
        int[] times = new int[sizeH];
        for (int x = 0; x < sizeH; x++)
            times[x] = 0;
        for (String s : timesG) {
            for (int i = 0; i < sizeH; i++) {
                String[] tab = s.split(":");
                int intTime = Integer.parseInt(tab[0]) * 60 + Integer.parseInt(tab[1]);
                times[i] += intTime;
            }
        }
        List<String> srtTimes = new ArrayList<>();
        for (int j = 0; j < sizeH; j++){
            int avr = times[j] / sizeG;
            srtTimes.add(String.format("%02d:%02d", avr / 60, avr % 60));
        }
        return srtTimes;
    }
}
