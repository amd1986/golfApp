package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * <b>Rule est la classe représentant un règle de golf dans la BDD</b>
 * <p>
 * Une règle de golf est caractérisée par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un code pour trier les règles, suceptible d'être changé.</li>
 * <li>Un titre, suceptible d'être changé.</li>
 * <li>Un corps correspondant à la description de la règle, suceptible d'être changé.</li>
 * <li>Une langue, suceptible d'être changé.</li>
 * <li>Une sous-catégorie dans laquelle est présente la règle, suceptible d'être changé.</li>
 * </ul>
 * </p>
 *
 * @see Section
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Rule implements Serializable {
    /**
     * L'ID de la règle de golf. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Rule#getId()
     * @see Rule#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Code de la règle de golf
     * <li>Longueur comprise entre 1 et 2</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Rule#getCode()
     * @see Rule#setCode(String)
     */
    @NotNull @Length(min = 1, max = 2)
    private String code;
    /**<ul>
     * Titre de la règle de golf
     * <li>Longueur max de 100 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Rule#getTitle()
     * @see Rule#setTitle(String)
     */
    @NotNull @Length(max = 100)
    private String title;
    /**<ul>
     * Corps de la règle de golf
     * <li>Longueur max de 2000 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Rule#getText()
     * @see Rule#setText(String)
     */
    @NotNull @Length(max = 2000)
    private String text;
    /**<ul>
     * Langue de la règle de golf
     * <li>Longueur de 2 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Rule#getLang()
     * @see Rule#setLang(String)
     */
    @NotNull @Length(min = 2,max = 2)
    private String lang;
    /**
     * Sous catégorie de la règle de golf, ne doit pas être nul
     *
     * @see Rule#getSubSection()
     * @see Rule#setSubSection(SubSection)
     */
    @NotNull
    @ManyToOne
    private SubSection subSection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return code.equals(rule.code) &&
                lang.equals(rule.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, lang);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }

    public String getFullCode(){
        return this.subSection.getSection().getCode()+"."+this.subSection.getCode()+"."+this.getCode();
    }
}
