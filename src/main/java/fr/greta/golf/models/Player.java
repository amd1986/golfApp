package fr.greta.golf.models;

import fr.greta.golf.entities.Course;
import fr.greta.golf.entities.Golf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * <b>Player est la classe modèle d'un joueur de golf</b>
 * Une joueur est caractérisé par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un nom, suceptible d'être changé.</li>
 * <li>Un numéro de licence, suceptible d'être changé.</li>
 * <li>Un index correspondant au niveau du joueur, suceptible d'être changé.</li>
 * <li>Une catégorie dans laquelle évolue le joueur, suceptible d'être changé.</li>
 * <li>Une nationalité, suceptible d'être changé.</li>
 * <li>Un golf auquel le joueur est rattaché, suceptible d'être changé.</li>
 * <li>Un commentaire, suceptible d'être changé.</li>
 * </ul>
 *
 * @see fr.greta.golf.entities.Competition
 *
 * @author ahmed
 * @version 1.1.0
 */
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Player {
    /**
     * Le nom du joueur de golf, ne peut être nul.
     *
     * @see Player#getName()
     * @see Player#setName(String)
     */
    @NotNull
    private String name;
    /**
     * La licence du joueur de golf, ne peut être nul.
     *
     * @see Player#getLicence()
     * @see Player#setLicence(int)
     */
    @NotNull
    private int licence;
    /**
     * L'indice du joueur de golf, ne peut être nul.
     *
     * @see Player#getIdx()
     * @see Player#setIdx(double)
     */
    @NotNull
    private double idx;
    /**
     * La série dans laquelle évolue le joueur de golf, ne peut être nul.
     *
     * @see Player#getSerie()
     * @see Player#setSerie(String)
     */
    @NotNull
    private String serie;
    /**
     * La catégorie du joueur de golf, ne peut être nul.
     *
     * @see Player#getRep()
     * @see Player#setRep(String)
     */
    @NotNull
    private String rep;
    /**
     * La nationalité du joueur de golf, ne peut être nul.
     *
     * @see Player#getNat()
     * @see Player#setNat(String)
     */
    @NotNull
    private String nat;
    /**
     * Le club de golf auquel est affilié le joueur de golf, ne peut être nul.
     *
     * @see Player#getClub()
     * @see Player#setClub(String)
     */
    @NotNull
    private String club;
    /**
     * Le commentaire laissé par l'arbitre pour le joueur de golf, ne peut être nul.
     *
     * @see Player#getComment()
     * @see Player#setComment(String)
     */
    private String comment;


}
