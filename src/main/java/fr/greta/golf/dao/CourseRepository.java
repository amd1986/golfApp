package fr.greta.golf.dao;

import fr.greta.golf.entities.Course;
import fr.greta.golf.entities.Golf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    public Page<Course> findByNameContains(String mc, Pageable pageable);
}
