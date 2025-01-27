package fr.greta.golf.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * <b>MyErrorController est la classe controller pour la gestion des erreurs.</b><br>
 * Cette classe founit les méthodes suivantes :
 * <ul>
 * <li>Un méthode héritée permettant de récupérer le path error.</li>
 * <li>Un méthode héritée permettant la gestion des routes en fonction des erreurs.</li>
 * </ul>
 *
 * @see ErrorController
 *
 * @author ahmed
 * @version 1.1.0
 */
@Controller
public class MyErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // TODO: log error details here

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // display specific error page
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "/error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "/error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "/error/403";
            }
        }

        // display generic error
        return "/error/default";
    }
}
