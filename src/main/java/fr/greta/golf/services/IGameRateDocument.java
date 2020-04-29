package fr.greta.golf.services;

import fr.greta.golf.entities.Competition;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface IGameRateDocument {
    void generateGameRate(Competition competition, HttpServletResponse response);
}
