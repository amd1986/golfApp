package fr.greta.golf.entities;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private String name;
    @OneToMany
    private Set<Hole> holes;
}
