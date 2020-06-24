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
 * <b>Hole est la classe représentant un trou de golf dans la BDD</b>
 * <p>
 * Un trou de golf est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un nom, ce champ n'est pas obligatoire, suceptible d'être changé.</li>
 * <li>Un numéro pour identifier l'ordre des trous dans le parcours, suceptible d'être changé.</li>
 * <li>Un par correspondant au nombre de balles pour chaque joueur, suceptible d'être changé.</li>
 * <li>Un offset(décalage) correspondant au temps supplémentaire que l'arbitre veut ajouter à un trou donné,
 * suceptible d'être changé.</li>
 * <li>Le golf dans lequel est présent le trou, suceptible d'être changé.</li>
 * <li>La liste des parcours dans lesquels est présent le trou, suceptible d'être changé.</li>
 * <li>La liste des temps des parties pour ce trou, suceptible d'être changé.</li>
 * </ul>
 * </p>
 *
 * @see Golf
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Hole implements Serializable {
    /**
     * L'ID du trou de golf. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Hole#getId()
     * @see Hole#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Numéro du trou
     * <li>Valeur comprise entre 1 et 18</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Hole#getNum()
     * @see Hole#setNum(int)
     */
    @NotNull
    @Min(1) @Max(18)
    private int num;
    /**<ul>
     * Nombre de coups(balles) du trou
     * <li>Valeur comprise entre 3 et 5</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Hole#getNum()
     * @see Hole#setNum(int)
     */
    @NotNull
    @Min(3) @Max(5)
    private int par;
    /**
     * Nombre de coups(balles) du trou, longueur comprise entre 1 et 30
     *
     * @see Hole#getName()
     * @see Hole#setName(String)
     */
    @Length(min = 1, max = 30)
    private String name;
    /**
     * Nombre de coups(balles) du trou, longueur comprise entre 1 et 30
     *
     * @see Hole#getOffset()
     * @see Hole#setOffset(int)
     */
    @Min(3) @Max(5)
    private int offset;

    @OneToOne(mappedBy = "hole1",cascade = CascadeType.REMOVE)
    private WalkTimeBtHoles walkTimeBtHoles1;

    @OneToOne(mappedBy = "hole2",cascade = CascadeType.REMOVE)
    private WalkTimeBtHoles walkTimeBtHoles2;
    /**
     * Liste des parcours dans lesquels le trou est présent
     *
     * @see Hole#getCourses()
     * @see Hole#setCourses(List)
     */
    @ManyToMany
    private List<Course> courses;
    /**
     * Les temps des parties pour un trou
     *
     * @see Hole#getCourses()
     * @see Hole#setCourses(List)
     */
    @OneToMany(mappedBy = "hole")
    private List<TimePerHPerG> timesPerHPerG;
    /**
     * Le golf dans lequel est présent le trou, ne peut être nul
     *
     * @see Hole#getGolf()
     * @see Hole#setGolf(Golf)
     */
    @ManyToOne
    @NotNull
    private Golf golf;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hole hole = (Hole) o;
        return  num == hole.num &&
                golf.equals(hole.golf) ||
                name.equals(hole.name) &&
                golf.equals(hole.golf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, name, golf);
    }

    @Override
    public String toString() {
        return "Hole{" +
                "num=" + num +
                ", par=" + par +
                ", name='" + name + '\'' +
                ", offset=" + offset +
                ", golf=" + golf +
                '}';
    }
}
