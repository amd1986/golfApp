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

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Language implements Serializable {
    @Id @Length(min = 2, max = 2)
    private String language;
    @ManyToMany
    private Set<User> users;
}
