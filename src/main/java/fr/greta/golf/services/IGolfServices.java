package fr.greta.golf.services;

import fr.greta.golf.entities.Golf;
import org.springframework.stereotype.Service;

@Service
public interface IGolfServices {
    public void delete(Long id);
}
