package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * <b>Competition est la classe représentant une compétition dans la BDD</b><br>
 * Une compétition est caractérisée par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un nom, suceptible d'être changé.</li>
 * <li>Un titre, suceptible d'être changé.</li>
 * <li>Un type, suceptible d'être changé, par exemple : Tournois Senior.</li>
 * <li>Une date de début de compétition, suceptible d'être changé.</li>
 * <li>Une heure de début de compétition, suceptible d'être changé.</li>
 * <li>Un nombre de joueurs par partie, suceptible d'être changé.</li>
 * <li>Un interval, c'est le temps entre chaque partie(entier), suceptible d'être changé.</li>
 * <li>Le parcours sur lequel la compétition va avoir lieu, suceptible d'être changé.</li>
 * </ul>
 * <p>
 * De plus, une compétition a une liste de parties(groupes de joueurs).
 * Cette liste de joueurs sera fournit par l'utilisateur dans la fonctionnalité <b>Générateur de cadence de jeu</b>.
 * </p>
 *
 * @see Course
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity
@Data @NoArgsConstructor
public class Competition implements Serializable {
    /**
     * L'ID de la compétition. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Competition#getId()
     * @see Competition#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Données pour construire l'entête du document PDF */

    /**<ul>
     * Prénom de l'arbitre
     * <li>Longueur comprise entre 2 et 30</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Competition#getDescription()
     * @see Competition#setDescription(String)
     */
    @Length(min = 5, max = 100)
    private String description;
    /**
     * Type de compétition. Par exemple Tournois Senior
     *
     * @see Competition#getType()
     * @see Competition#setType(String)
     */
    @NotNull @Length(min = 2, max = 30)
    private String type;
    /**
     * Nom de la compétition, la longeur de la chaîne de caractère doit être comprise entre 2 et 50.
     *
     * @see Competition#getName()
     * @see Competition#setName(String)
     */
    @NotNull @Length(min = 2, max = 50)
    private String name;
    /**
     * Date du début de la compétition au format String, la longueur chaîne de caractère est de 10.
     *
     * @see Competition#getName()
     * @see Competition#setName(String)
     */
    @NotNull @Length(min = 10, max = 10)
    private String dateCompetition;

    /* Données permettant de générer chaque partie(ligne du tableau PDF) */
    /**<ul>
     * Nombre de joueurs par partie
     * <li>il doit être compris entre 2 et 5</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Competition#getNbPlayersByGame()
     * @see Competition#setNbPlayersByGame(int)
     */
    @NotNull
    @Min(2) @Max(5)
    private int nbPlayersByGame;
    /**<ul>
     * Temps entre les parties
     * <li>il doit être compris entre 7 et 15</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Competition#getIntervalBtGames()
     * @see Competition#setIntervalBtGames(int)
     */
    @NotNull
    @Min(7) @Max(15)
    private int intervalBtGames;
    /**<ul>
     * Heure de départ de la compétition
     * <li>La longueur de la châine de caractère doit être égale à 5</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Competition#getDepartureHour()
     * @see Competition#setDepartureHour(String)
     */
    @NotNull @Length(min = 5, max = 5)
    private String departureHour;
    @NotNull
    /*
      Le parcours sur lequel se déroule la compétition

      @see Competition#getCourse()
     * @see Competition#setCourse(Course)
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;
    /**
     * Liste des parties d'une compétition
     *
     * @see Competition#getGames()
     * @see Competition#setGames(List)
     */
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private List<Game> games;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competition that = (Competition) o;
        return  name.equals(that.name) &&
                course.equals(that.course) ||
                dateCompetition.equals(that.dateCompetition) &&
                course.equals(that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateCompetition, course);
    }

    @Override
    public String toString() {
        return "Competition{" +
                "description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", dateCompetition=" + dateCompetition +
                ", nbPlayersByGame=" + nbPlayersByGame +
                ", intervalBtGames=" + intervalBtGames +
                ", departureHour=" + departureHour +
                '}';
    }
}
