package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * <b>Course est la classe représentant le tamps d'une partie pour un trou donné dans la BDD</b>
 * <p>
 * Un temps est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un temps correspondant à une partie pour un trou donné, suceptible d'être changé.</li>
 * </ul>
 * </p>
 *
 * @see Hole
 * @see Game
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class TimePerHPerG {
    /**
     * L'ID d'un temps pour un trou et une partie. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see TimePerHPerG#getId()
     * @see TimePerHPerG#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Temps en châine de caractères
     * <li>la longueur doit être de 5 caracxtères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see TimePerHPerG#getTime()
     * @see TimePerHPerG#setTime(String)
     */
    @NotNull @Length(min = 5, max = 5)
    private String time;
    /**
     * Trou associé à ce temps, ne peut être nul
     *
     * @see TimePerHPerG#getHole()
     * @see TimePerHPerG#setHole(Hole)
     */
    @NotNull
    @ManyToOne
    private Hole hole;
    /**
     * Partie associé à ce temps, ne peut être nul
     *
     * @see TimePerHPerG#getHole()
     * @see TimePerHPerG#setHole(Hole)
     */
    @NotNull
    @ManyToOne
    private Game game;

    @Override
    public String toString() {
        return "TimePerHPerG{" +
                "time='" + time + '\'' +
                ", hole=" + hole +
                ", game=" + game +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePerHPerG that = (TimePerHPerG) o;
        return  hole.equals(that.hole) &&
                game.equals(that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hole, game);
    }
}
