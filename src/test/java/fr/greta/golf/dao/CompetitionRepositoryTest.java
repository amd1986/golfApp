package fr.greta.golf.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CompetitionRepositoryTest {


    @Test
    @Transactional
    void addCompetition() {

        /*Assert.assertThat(sousCategorieRepository.count()).isEqualTo(11);
        SousCategorie sousCat = new SousCategorie();
        sousCat.setTitle("Hello");
        Regle regle= new Regle();
        sousCat.getRegles().add(regle);
        regleRepository.save(regle);
        sousCategorieRepository.save(sousCat);

        assertThat(sousCategorieRepository.count()).isEqualTo(12);
        SousCategorie scat0 = sousCategorieRepository.getOne(sousCat.getId());
        assertThat(scat0.getRegles().size()).isEqualTo(1);*/
    }
}
