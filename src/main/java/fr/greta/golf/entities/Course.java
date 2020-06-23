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

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull @Length(min = 1, max = 50)
    private String name;
    @NotNull
    @ManyToMany(mappedBy = "courses")
    @Size(min = 18, max = 18)
    private List<Hole> holes;
    @NotNull
    @ManyToOne
    private Golf golf;
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
