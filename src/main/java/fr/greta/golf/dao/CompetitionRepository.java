package fr.greta.golf.dao;

import fr.greta.golf.entities.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Zaps the roadrunner with the number of volts you specify.
 * <p>
 * Do not exceed more than 30 volts or the zap function will backfire.
 * For another way to kill a roadrunner, see the {@link Dynamite#blowDynamite()} method.
 *
 * @exception IOException if you don't enter a data type amount for the voltage
 * @param voltage the number of volts you want to send into the roadrunner's body
 * @see #findRoadRunner
 * @see Dynamite#blowDynamite
 */
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Page<Competition> findByNameContains(String mc, Pageable pageable);
}
