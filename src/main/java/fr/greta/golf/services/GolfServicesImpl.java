package fr.greta.golf.services;

import fr.greta.golf.dao.CourseRepository;
import fr.greta.golf.dao.GolfRepository;
import fr.greta.golf.dao.HoleRepository;
import fr.greta.golf.entities.Golf;

public class GolfServicesImpl implements IGolfServices {
    private final GolfRepository golfRepository;
    private final CourseRepository courseRepository;
    private final HoleRepository holeRepository;

    public GolfServicesImpl(GolfRepository golfRepository,
                            CourseRepository courseRepository,
                            HoleRepository holeRepository) {
        this.golfRepository = golfRepository;
        this.courseRepository = courseRepository;
        this.holeRepository = holeRepository;
    }

    @Override
    public void delete(Long id) {
        Golf golf = golfRepository.findById(id).get();

    }
}
