package fr.greta.golf.services;

import fr.greta.golf.models.Player;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>LogServicesImpl est la classe implémentant LogServices pour la journalisation des actions de l'utilisateur</b>
 * <p>
 *     Cette classe fournit la méthode permettant de journaliser la gestion des données.
 * </p>
 *
 * @see LogServices
 *
 * @author ahmed
 * @version 1.1.0
 */
public class LogServicesImpl implements LogServices {
    private final Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("fr.greta.golf.tests");

    /**
     * Méthode add.
     * <p>
     *     Méthode qui va journaliser l'ajout d'un objet par un utilisateur.
     * </p>
     *
     * @param request Pour récupérer le nom de l'utilisateur apportant une modification
     * @param obj L'objet ajouté par l'utilisateur
     *
     */
    @Override
    public void add(HttpServletRequest request, Object obj) {
        String username = request.getUserPrincipal().getName();
        this.logger.warn("L'utilisateur "+username+" a ajouté : "+obj.toString());
    }

    /**
     * Méthode edit.
     * <p>
     *     Méthode qui va journaliser la modification d'un objet par un utilisateur.
     * </p>
     *
     * @param request Pour récupérer le nom de l'utilisateur apportant une modification
     * @param obj L'objet modifié par l'utilisateur
     *
     */
    @Override
    public void edit(HttpServletRequest request, Object obj) {
        String username = request.getUserPrincipal().getName();
        this.logger.warn("L'utilisateur "+username+" a modifié : "+obj.toString());
    }

    /**
     * Méthode remove.
     * <p>
     *     Méthode qui va journaliser la suppression d'un objet par un utilisateur.
     * </p>
     *
     * @param request Pour récupérer le nom de l'utilisateur apportant une modification
     * @param obj L'objet supprimé par l'utilisateur
     *
     */
    @Override
    public void remove(HttpServletRequest request, Object obj) {
        String username = request.getUserPrincipal().getName();
        this.logger.warn("L'utilisateur "+username+" a supprimé : "+obj.toString());
    }
}
