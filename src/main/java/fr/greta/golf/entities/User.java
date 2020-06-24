package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

/**
 * <b>User est la classe représentant un utilisateur dans la BDD</b>
 * <p>
 * Un utilisateur est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement correspondant au nom de l'utilisateur.</li>
 * <li>Un mot de passe encodé, suceptible d'être changé.</li>
 * <li>Un status de l'utilisateur(actif ou non), suceptible d'être changé.</li>
 * <li>Une liste de langue correspondant aux langue(s) attribuée(s) à cet utilisateur, suceptible d'être changé.</li>
 * <li>Une liste de rôle correspondant aux rôle(s) attribué(s) à cet utilisateur, suceptible d'être changé.</li>
 * </ul>
 * </p>
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity @Table(name = "users")
@Data @AllArgsConstructor @NoArgsConstructor
public class User {
    /**<ul>
     * Le nom d'utilisateur.
     * <li>Cet identifiant peut être attribué et modifié par le SUPERADMIN.</li>
     * <li>La longueur du nom d'utilisateur doit être comprise entre 5 et 30 caractères.</li>
     * </ul>
     *
     * @see User#getUsername()
     * @see User#setUsername(String)
     */
    @Id @Length(min = 5, max = 30)
    private String username;
    /**<ul>
     * Le mot de passe de l'utilisateur.
     * <li>Il est attribué par le SUPERADMIN.</li>
     * <li>La longueur du mot de passe de l'utilisateur doit être comprise entre 8 et 255 caractères.</li>
     * </ul>
     *
     * @see User#getPassword()
     * @see User#setPassword(String)
     */
    @NotNull @Length(min = 8)
    private String password;
    /**
     * Status de l'utilisateur, actif ou non(2 valeurs possibles 0 ou 1)
     *
     * @see User#isActive()
     * @see User#setActive(boolean)
     */
    @NotNull
    private boolean active;
    /**<ul>
     * Liste des langues de l'utilisateur.
     * <li>Attribué par le SUPERADMIN.</li>
     * <li>Un utilisateur doit avoir au moins une langue qui lui soit attribuée</li>
     * </ul>
     *
     * @see User#getLanguages()
     * @see User#setLanguages(Set)
     */
    @Size(min = 1)
    @ManyToMany(mappedBy = "users")
    private Set<Language> languages;
    /**<ul>
     * Liste des roles de l'utilisateur.
     * <li>Attribué par le SUPERADMIN.</li>
     * <li>Un utilisateur doit avoir au moins une rôle qui lui soit attribuée</li>
     * </ul>
     *
     * @see User#getRoles()
     * @see User#setRoles(Set)
     */
    @Size(min = 1)
    @ManyToMany(mappedBy = "users")
    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
