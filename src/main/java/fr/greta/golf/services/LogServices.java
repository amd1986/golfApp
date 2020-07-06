package fr.greta.golf.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface LogServices {
    void add(HttpServletRequest request, Object obj);
    void edit(HttpServletRequest request, Object obj);
    void remove(HttpServletRequest request, Object obj);
}
