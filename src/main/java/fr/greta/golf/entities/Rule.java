package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Rule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull @Length(min = 1, max = 2)
    private String code;
    @NotNull @Length(max = 100)
    private String title;
    @NotNull @Length(max = 2000)
    private String text;
    @NotNull @Length(min = 2,max = 2)
    private String lang;
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
