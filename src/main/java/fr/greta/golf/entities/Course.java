package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * <b>Course est la classe représentant un parcours dans la BDD</b><br>
 * Une parcours est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un nom, suceptible d'être changé.</li>
 * <li>Un parcours a une liste de trous(18), suceptible de changer.</li>
 * <li>Un parcours a une liste de compétitions, correspondant à la liste des compétitions utilisant ce parcours,
 * suceptible de changer.</li>
 * <li>Le golf dans lequel le parcours est présent, suceptible d'être changé.</li>
 * </ul>
 *
 * @see Golf
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity
@Data @NoArgsConstructor
public class Course implements Serializable {
    /**
     * L'ID du parcours de golf. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Course#getId()
     * @see Course#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Nom du parcours
     * <li>Longueur comprise entre 1 et 50</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Course#getName()
     * @see Course#setName(String)
     */
    @NotNull @Length(min = 1, max = 50)
    private String name;
    /**<ul>
     * Liste des trous du parcours de golf
     * <li>Il doit y avoir exactement 18 trous</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Course#getHoles()
     * @see Course#setHoles(List)
     */
    @NotNull
    @ManyToMany(mappedBy = "courses")
    @Size(min = 18, max = 18)
    private List<Hole> holes;
    /**
     * Le club de golf du parcours, ne peut être nul.
     *
     * @see Course#getGolf()
     * @see Course#setGolf(Golf)
     */
    @NotNull
    @ManyToOne
    private Golf golf;
    /**<ul>
     * Liste des compétitions utilisant ce parcours.
     * <li>Il doit y avoir exactement 18 trous</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Course#getCompetitions()
     * @see Course#setCompetitions(List)
     */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Competition> competitions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(name, course.name) &&
                Objects.equals(golf, course.golf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, golf);
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                '}';
    }
}
