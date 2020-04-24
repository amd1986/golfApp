package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Golf implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private int zipCode;
    @NotNull
    private String city;
    @NotNull
    private String country;
    @OneToMany(mappedBy = "golf", cascade = CascadeType.ALL)
    private Set<Hole> holes;
    @OneToMany(mappedBy = "golf", cascade = CascadeType.ALL)
    private Set<Course> courses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Golf golf = (Golf) o;
        return  name.equals(golf.name) &&
                city.equals(golf.city) &&
                country.equals(golf.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, country);
    }

    @Override
    public String toString() {
        return "Golf{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
