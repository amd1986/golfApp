package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * <b>Golf est la classe représentant un golf dans la BDD</b>
 * <p>
 * Une golf est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un nom, suceptible d'être changé.</li>
 * <li>Une adresse, suceptible d'être changé.</li>
 * <li>Un code postal, suceptible d'être changé.</li>
 * <li>La ville où est situé le golf, suceptible d'être changé.</li>
 * <li>Le pays dans lequel se trouve le golf, suceptible d'être changé.</li>
 * <li>Un golf a une liste de trous, suceptible de changer.</li>
 * <li>Un golf a une liste de parcours, suceptible de changer.</li>
 * </ul>
 * </p>
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Golf implements Serializable {
    /**
     * L'ID du golf. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Golf#getId()
     * @see Golf#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Nom du golf
     * <li>Longueur comprise entre 5 et 50</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Golf#getName()
     * @see Golf#setName(String)
     */
    @NotNull @Length(min = 5, max = 50)
    private String name;
    /**<ul>
     * Adresse du golf
     * <li>Longueur comprise entre 5 et 100</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Golf#getName()
     * @see Golf#setName(String)
     */
    @NotNull @Length(min = 5, max = 100)
    private String address;
    /**<ul>
     * Code postal du golf
     * <li>Valeur comprise entre 1 et 999 999 999/li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Golf#getZipCode()
     * @see Golf#setZipCode(int)
     */
    @NotNull @Min(1) @Max(1000000000)
    private int zipCode;
    /**<ul>
     * Ville où est situé le golf
     * <li>Longueur comprise entre 1 et 50</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Golf#getCity()
     * @see Golf#setCity(String)
     */
    @NotNull @Length(min = 1, max = 50)
    private String city;
    /**<ul>
     * Pays où est situé le golf
     * <li>Longueur comprise entre 5 et 50</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Golf#getCountry()
     * @see Golf#setCountry(String)
     */
    @NotNull @Length(min = 4, max = 50)
    private String country;
    /**
     * Liste des trous du golf, les trous du golf sont supprimé si celui-ci est supprimé
     *
     * @see Golf#getHoles()
     * @see Golf#setHoles(Set)
     */
    @OneToMany(mappedBy = "golf", cascade = CascadeType.ALL)
    private Set<Hole> holes;
    /**
     * Liste des trous du golf, les parcours du golf sont supprimé si celui-ci est supprimé
     *
     * @see Golf#getCourses()
     * @see Golf#setCourses(Set)
     */
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
