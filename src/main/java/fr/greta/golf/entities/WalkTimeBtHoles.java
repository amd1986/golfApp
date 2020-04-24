package fr.greta.golf.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class WalkTimeBtHoles implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private int walkTime;
    private String description;
    @OneToOne
    @NotNull
    private Hole hole1;
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
