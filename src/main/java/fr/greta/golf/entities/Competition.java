package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Competition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Données pour construire l'entête du document PDF */
    private String description;
    @NotNull
    private String type;
    @NotNull
    private String name;
    @NotNull
    private String dateCompetition;

    /* Données permettant de générer chaque partie(ligne du tableau PDF) */
    @NotNull
    private int nbPlayersByGame;
    @NotNull
    private int intervalBtGames;
    @NotNull
    private String departureHour;
    @NotNull
    @ManyToOne
    private Course course;
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private Set<Game> games;

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
