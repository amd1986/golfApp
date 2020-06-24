package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * <b>Contact es l'entité de gestion des données du formulaire de contact, permettant à l'arbitre de contacter l'admin</b>
 * <p>
 * Un contact est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un prénom correspondant à celui de l'arbitre.</li>
 * <li>Un nom correspondant à celui de l'arbitre.</li>
 * <li>Un email correspondant à celui de l'arbitre.</li>
 * <li>Le message de l'arbitre.</li>
 * </ul>
 * </p>
 *
 * @see User
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Contact {
    /**
     * L'ID de l'entité Contact. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Contact#getId()
     * @see Contact#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Prénom de l'arbitre
     * <li>Longueur comprise entre 2 et 30</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Contact#getFirstName()
     * @see Contact#setFirstName(String)
     */
    @NotNull
    @Length(min = 2, max = 30)
    private String firstName;
    /**<ul>
     * Nom de l'arbitre
     * <li>Longueur comprise entre 2 et 30</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Contact#getLastName()
     * @see Contact#setLastName(String)
     */
    @NotNull
    @Length(min = 2, max = 30)
    private String lastName;
    /**<ul>
     * Email de l'arbitre
     * <li>Longueur comprise entre 2 et 30</li>
     * <li>Validation de l'email</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Contact#getEmail()
     * @see Contact#setEmail(String)
     */
    @NotNull
    @Email
    @Length(min = 6, max = 50)
    private String email;
    /**<ul>
     * Message de l'arbitre
     * <li>le contenu ne doit pas excéder 250 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Contact#getMessage()
     * @see Contact#setMessage(String)
     */
    @NotNull
    @Length(max = 250)
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return  firstName.equals(contact.firstName) &&
                lastName.equals(contact.lastName) ||
                email.equals(contact.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
