package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Section implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull @Length(min = 1, max = 1)
    private String code;
    @NotNull @Length(max = 100)
    private String title;
    @NotNull @Length(min = 2,max = 2)
    private String lang;
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<SubSection> subSections;

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
