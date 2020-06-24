package fr.greta.golf.entities;

import fr.greta.golf.models.Player;
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
 * <b>Game est la classe représentant une partie d'une compétition dans la BDD</b>
 * <p>
 * Une partie est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un numéro de partie, attribué lors de la phase Générateur de cadence de jeu.</li>
 * <li>Un nom de partie, suceptible d'être changé.</li>
 * <li>La compétition à laquelle participe cette partie, suceptible d'être changé.</li>
 * <li>L'heure de départ de la partie.</li>
 * <li>La liste des temps de cette partie pour les différents trous.</li>
 * <li>La liste des joueurs de cette partie, les informations des joueurs ne sont pas persistées dans la BDD.</li>
 * </ul>
 * </p>
 *
 * @see Competition
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Game implements Serializable {
    /**
     * L'ID de la partie de compétition. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Game#getId()
     * @see Game#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Numéro de la partie de compétition
     * <li>Valeur comprise entre 1 et 50</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Game#getNum()
     * @see Game#setNum(int)
     */
    @NotNull @Min(1) @Max(50)
    private int num;
    /**<ul>
     * Nom de la compétition
     * <li>Longueur comprise entre 5 et 50</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Game#getName()
     * @see Game#setName(String)
     */
    @NotNull @Length(min = 5, max = 50)
    private String name;
    /**<ul>
     * La compétition à laquelle participent les parties(joueurs), ne peut être nul
     *
     * @see Game#getName()
     * @see Game#setName(String)
     */
    @NotNull
    @ManyToOne
    private Competition competition;
    /**
     * Le temps pour chaque trou pour une partie
     *
     * @see Game#getTimesPerHPerG()
     * @see Game#setTimesPerHPerG(List)
     */
    @OneToMany(mappedBy = "game")
    private List<TimePerHPerG> timesPerHPerG;
    /**
     * Le temps pour chaque trou pour une partie en chaîne de caractère, n'est pas persisté en BDD
     *
     * @see Game#getTimesPerHPerG()
     * @see Game#setTimesPerHPerG(List)
     */
    @Transient
    private List<String> times;
    @Length(min = 5, max = 5)
    /**
     * Le temps de départ de la partie
     *
     * @see Game#getDhour()
     * @see Game#setDhour(String)
     */
    private String dhour;
    /**
     * Liste des joueurs de la partie, n'est pas persisté dans la BDD pour des raisons de protection des données
     *
     * @see Game#getPlayers()
     * @see Game#setPlayers(List)
     */
    @Transient
    private List<Player> players;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return  num == game.num &&
                competition.equals(game.competition) ||
                name.equals(game.name) &&
                competition.equals(game.competition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, name, competition);
    }

    @Override
    public String toString() {
        return "Game{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}
