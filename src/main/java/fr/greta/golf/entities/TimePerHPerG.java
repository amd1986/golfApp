package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class TimePerHPerG {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull @Length(min = 5, max = 5)
    private String time;
    @NotNull
    @ManyToOne
    private Hole hole;
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
