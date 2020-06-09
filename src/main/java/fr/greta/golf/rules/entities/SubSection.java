package fr.greta.golf.rules.entities;

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
public class SubSection implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull @Length(min = 1, max = 2)
    private String code;
    @NotNull @Length(max = 100)
    private String title;
    @NotNull @Length(max = 2000)
    private String description;
    @NotNull @Length(min = 2,max = 2)
    private String lang;
    @NotNull
    @ManyToOne
    private Section section;
    @OneToMany(mappedBy = "subSection")
    private List<Rule> rules;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubSection that = (SubSection) o;
        return code.equals(that.code) &&
                lang.equals(that.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, lang);
    }

    @Override
    public String toString() {
        return "SubSection{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getFullCode(){
        return this.getSection().getCode()+"."+this.getCode();
    }
}
