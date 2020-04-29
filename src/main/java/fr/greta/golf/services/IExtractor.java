package fr.greta.golf.services;

import fr.greta.golf.model.Player;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface IExtractor {
    List<Player> extractPlayers(MultipartFile file);
}
