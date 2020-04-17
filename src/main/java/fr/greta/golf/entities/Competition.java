package fr.greta.golf.entities;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
public class Competition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate departure;
    private int intervalBtGames;
    @OneToOne
    private Course course;
    @OneToMany
    private Set<Game> games;
}
