package fr.greta.golf.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
public class walkTimeBtHoles implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int walkTime;
    @OneToOne
    private Hole hole1;
    @OneToOne
    private Hole hole2;
}
