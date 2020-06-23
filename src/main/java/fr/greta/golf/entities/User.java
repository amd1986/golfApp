package fr.greta.golf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Entity @Table(name = "users")
@Data @AllArgsConstructor @NoArgsConstructor
public class User {
    @Id @Length(min = 5, max = 30)
    private String username;
    @NotNull @Length(min = 8)
    private String password;
    @NotNull
    private boolean active;

    @Size(min = 1)
    @ManyToMany(mappedBy = "users")
    private Set<Language> languages;

    @Size(min = 1)
    @ManyToMany(mappedBy = "users")
    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
