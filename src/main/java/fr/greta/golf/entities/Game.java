package fr.greta.golf.entities;

import fr.greta.golf.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @NotNull
    private int num;
    @NotNull
    private String name;
    @NotNull
    @ManyToOne
    private Competition competition;
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
