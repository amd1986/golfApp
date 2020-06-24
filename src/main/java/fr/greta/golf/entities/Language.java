package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Set;

/**
 * <b>Language est la classe représentant une langue dans la BDD</b>
 * <p>
 * Une langue est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement, cette identifiant correspond à la langue codée sur 2 caractères.</li>
 * <li>Une liste d'utilisateurs auxquels le SUPERADMIN a attribué cette langue, suceptible d'être changé.</li>
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
public class Language implements Serializable {
    /**
     * <p>
     *     L'ID de la langue de l'utilisateur. Cet identifiant peut être modifié par le SUPERADMIN, est composé de 2 lettres.
     * </p>
     *
     * @see Language#getLanguage()
     * @see Language#setLanguage(String)
     */
    @Id @Length(min = 2, max = 2)
    private String language;
    /**
     * <p>
     *     Liste des utilisateurs ayant cette langue(C'est le SUPERADMIN qui attribut une langue),
     *     une langue peut ne pas avoir d'utilisateur
     * </p>
     *
     * @see Language#getUsers()
     * @see Language#setUsers(Set)
     */
    @ManyToMany
    private Set<User> users;
}
