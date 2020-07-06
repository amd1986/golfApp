package fr.greta.golf.entities;
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
 * <b>Section est la classe représentant un catégorie de règle de golf dans la BDD</b><br>
 * Une catégorie est caractérisée par les information suivantes :
 * <ul>
 * <li>Un identifiant unique attribué définitivement.</li>
 * <li>Un code permettant de trier les catégories, suceptible d'être changé.</li>
 * <li>Un titre, suceptible d'être changé.</li>
 * <li>Un langue, suceptible d'être changé.</li>
 * <li>Une liste de sous-catégories, suceptible d'être changé.</li>
 * </ul>
 *
 * @author ahmed
 * @version 1.1.0
 */
@Entity @NoArgsConstructor
public class Section implements Serializable {
    /**
     * L'ID de la catégorie de règle de golf.
     * Cet ID n'est pas modifiable et auto incrémenté.
     *
     * @see Section#getId()
     * @see Section#setId(Long)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**<ul>
     * Code de la catégorie de règle de golf
     * <li>Longueur de 1 caractère</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Section#getCode()
     * @see Section#setCode(String)
     */
    @NotNull @Length(min = 1, max = 1)
    private String code;
    /**<ul>
     * Titre de la catégorie de règle de golf
     * <li>Longueur max de 100 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Section#getTitle()
     * @see Section#setTitle(String)
     */
    @NotNull @Length(max = 100)
    private String title;
    /**<ul>
     * Langue de la catégorie de règle de golf
     * <li>Longueur de 2 caractères</li>
     * <li>ne doit pas être nul</li>
     * </ul>
     *
     * @see Section#getLang()
     * @see Section#setLang(String)
     */
    @NotNull @Length(min = 2,max = 2)
    private String lang;
    /**
     * Liste des sous catégories de la catégorie de règle de golf, peut être nul
     *
     * @see Section#getSubSections()
     * @see Section#setSubSections(List)
     */
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<SubSection> subSections;
    /*...*/

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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<SubSection> getSubSections() {
        return subSections.stream().sorted(Comparator.comparing(SubSection::getCode)).collect(Collectors.toList());
    }

    public void setSubSections(List<SubSection> subSections) {
        this.subSections = subSections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return code.equals(section.code) &&
                lang.equals(section.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, lang);
    }

    @Override
    public String toString() {
        return "Section{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
