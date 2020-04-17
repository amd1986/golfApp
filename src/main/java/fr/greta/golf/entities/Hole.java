package fr.greta.golf.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
public class Hole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int num;
    private int par;
    private String name;
    private int totalWalkTime;
    @OneToOne
    private walkTimeBtHoles timeBtHoles;
}
