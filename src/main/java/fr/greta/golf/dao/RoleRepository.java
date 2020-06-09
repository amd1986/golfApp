package fr.greta.golf.dao;

import fr.greta.golf.entities.security.Role;
import fr.greta.golf.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, String> {
}
