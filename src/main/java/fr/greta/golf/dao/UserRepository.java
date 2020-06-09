package fr.greta.golf.dao;

import fr.greta.golf.entities.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findByUsernameContains(String mc, Pageable pageable);
}
