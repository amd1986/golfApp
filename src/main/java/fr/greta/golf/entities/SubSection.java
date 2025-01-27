package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <b>Subsection est la classe représentant une sous-catégorie de règle de golf dans la BDD</b><br>
 * Une sous-catégorie est caractérisée par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un code pour trier les sous-catégories, suceptible d'être changé.</li>
 * <li>Un titre, suceptible d'être changé.</li>
 * <li>Un corps correspondant à la description de la sous-catégorie, suceptible d'être changé.</li>
 * <li>Une langue, suceptible d'être changé.</li>
 * <li>Une catégorie dans laquelle est présente la règle, suceptible d'être changé.</li>
 * <li>Une liste de règle de golf, suceptible d'être changé.</li>
 * </ul>
 *
 * @see Section
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity @NoArgsConstructor
public class SubSection implements Serializable {
    /**
     * L'ID de la sous catégorie de règle de golf. Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Section#getId()
     * @see Section#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Code de la sous catégorie de règle de golf
     * <li>Longueur de 1 à 2 caractère(s)</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see SubSection#getCode()
     * @see SubSection#setCode(String)
     */
    @NotNull @Length(min = 1, max = 2)
    private String code;
    /**<ul>
     * Titre de la sous catégorie de règle de golf
     * <li>Longueur max de 100 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see SubSection#getTitle()
     * @see SubSection#setTitle(String)
     */
    @NotNull @Length(max = 100)
    private String title;
    /**<ul>
     * Corps de la sous catégorie de règle de golf
     * <li>Longueur max de 2000 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see SubSection#getDescription()
     * @see SubSection#setDescription(String)
     */
    @NotNull @Length(max = 2000)
    private String description;
    /**<ul>
     * Langue de la sous catégorie de règle de golf
     * <li>Longueur de 2 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see SubSection#getLang()
     * @see SubSection#setLang(String)
     */
    @NotNull @Length(min = 2,max = 2)
    private String lang;
    /**
     * Catégorie de règle de golf dans laquelle est présent la sous règle, ne peut être nul
     *
     * @see SubSection#getSection()
     * @see SubSection#setSection(Section)
     */
    @NotNull
    @ManyToOne
    private Section section;
    /**
     * Liste des règles présentes dans la sous catégorie de règle de golf
     *
     * @see SubSection#getRules()
     * @see SubSection#setRules(List)
     */
    @OneToMany(mappedBy = "subSection")
    private List<Rule> rules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public List<Rule> getRules() {
        return rules.stream().sorted(Comparator.comparing(Rule::getCode)).collect(Collectors.toList());
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubSection that = (SubSection) o;
        return code.equals(that.code) &&
                title.equals(that.title) &&
                lang.equals(that.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, section);
    }

    public String getFullCode(){
        return this.getSection().getCode()+"."+this.getCode();
    }

    @Override
    public String toString() {
        return "SubSection{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
