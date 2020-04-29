package fr.greta.golf.entities;

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
public class Hole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int num;
    @NotNull
    private int par;
    private String name;
    private int offset;
    @OneToOne(mappedBy = "hole1",cascade = CascadeType.REMOVE)
    private WalkTimeBtHoles walkTimeBtHoles1;
    @OneToOne(mappedBy = "hole2",cascade = CascadeType.REMOVE)
    private WalkTimeBtHoles walkTimeBtHoles2;
    @ManyToMany
    private List<Course> courses;
    @OneToMany(mappedBy = "hole")
    private List<TimePerHPerG> timesPerHPerG;
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
