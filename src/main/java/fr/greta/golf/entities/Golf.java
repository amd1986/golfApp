package fr.greta.golf.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
public class Golf implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private int zipCode;
    private String city;
    private String country;
    @OneToMany
    private Set<Hole> holes;
    @OneToMany
    private Set<Course> courses;
}
