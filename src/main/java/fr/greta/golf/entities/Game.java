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

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Game implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull @Min(1) @Max(50)
    private int num;
    @NotNull @Length(min = 5, max = 50)
    private String name;
    @NotNull
    @ManyToOne
    private Competition competition;
    @OneToMany(mappedBy = "game")
    private List<TimePerHPerG> timesPerHPerG;
    @Transient
    private List<String> times;
    @Length(min = 5, max = 5)
    private String dhour;
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
