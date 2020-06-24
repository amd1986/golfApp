package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

/**
 * <b>Role est la classe représentant un rôle dans la BDD</b>
 * <p>
 * Un rôle est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement correspondant au nom du rôle.</li>
 * <li>La liste des utilisateurs ayant ce rôle, suceptible d'être changé.</li>
 * </ul>
 * </p>
 *
 * @see User
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity @Table(name = "roles")
@Data @AllArgsConstructor @NoArgsConstructor
public class Role {
    /**
     * <p>
     *     L'ID du rôle de l'utilisateur. Cet identifiant peut être modifié par le SUPERADMIN.
     *     Sa lonhueur ne pourra être suppérieur à 50 caractères
     * </p>
     *
     * @see Role#getRole()
     * @see Role#setRole(String)
     */
    @Id @Length(max = 50)
    private String role;
    /**<p>
     * Liste des utilisateurs ayant ce rôle(C'est le SUPERADMIN qui attribut une langue),
     * un rôle peut être attribué à aucun utilisateur
     * </p>
     *
     * @see Course#getName()
     * @see Course#setName(String)
     */
    @ManyToMany
    private Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return role.equals(role1.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    @Override
    public String toString() {
        return "Role{" +
                "role='" + role + '\'' +
                '}';
    }
}
