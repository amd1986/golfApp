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
import java.util.Objects;

/**
 * <b>WalkTimeBtHoles est la classe représentant un temps de marche entre 2 trous dans la BDD</b>
 * <p>
 * Un temps est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un temps de marche, suceptible d'être changé.</li>
 * <li>Un intitulé, suceptible d'être changé.</li>
 * <li>Un premier trou, suceptible d'être changé.</li>
 * <li>Un second trou, suceptible d'être changé.</li>
 * </ul>
 * </p>
 *
 * @see Hole
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class WalkTimeBtHoles implements Serializable {
    /**
     * L'ID d'un temps de marche entre 2 trous. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see WalkTimeBtHoles#getId()
     * @see WalkTimeBtHoles#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Temps en marche
     * <li>Valeur comprise entre 0 et 15</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see WalkTimeBtHoles#getWalkTime()
     * @see WalkTimeBtHoles#setWalkTime(int)
     */
    @NotNull
    @Min(0) @Max(15)
    private int walkTime;
    /**
     * Intitulé du temps de marche, longueur max de 50 caractères
     *
     * @see WalkTimeBtHoles#getDescription()
     * @see WalkTimeBtHoles#setDescription(String)
     */
    @Length(max = 50)
    private String description;
    /**
     * Trou n°1 associé à ce temps de marche
     *
     * @see WalkTimeBtHoles#getHole1()
     * @see WalkTimeBtHoles#setHole1(Hole)
     */
    @OneToOne
    @NotNull
    private Hole hole1;
    /**
     * Trou n°2 associé à ce temps de marche
     *
     * @see WalkTimeBtHoles#getHole2()
     * @see WalkTimeBtHoles#setHole2(Hole)
     */
    @OneToOne
    @NotNull
    private Hole hole2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalkTimeBtHoles that = (WalkTimeBtHoles) o;
        return  hole1.equals(that.hole1) &&
                hole2.equals(that.hole2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hole1, hole2);
    }

    @Override
    public String toString() {
        return "WalkTimeBtHoles{" +
                "walkTime=" + walkTime +
                ", description='" + description + '\'' +
                '}';
    }
}
