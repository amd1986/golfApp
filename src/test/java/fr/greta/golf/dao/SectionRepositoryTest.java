package fr.greta.golf.dao;
import fr.greta.golf.entities.Section;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
@SpringBootTest
class SectionRepositoryTest {
    @Autowired
    private SectionRepository sectionRepository;
    /**<ul>
     * Méthode ajouterCategorie:
     * Test unitaire d'ajout d'une catégorie de règle de golf
     * <li>Vérifie que la règle n'est pas déjà présente</li>
     * <li>Persiste la nouvel catégorie</li>
     * <li>Annule l'opération si l'objet fournit est invalide</li>
     * </ul>
     *
     * @see Section
     * @see SectionRepository
     */
    @Test
    @Transactional
    void addNewSection() { //section = catégorie
        Section section = new Section();
        section.setTitle("Section W");
        section.setCode("W");
        section.setLang("fr");
        assertThat(section.getId()).isNull();

        int nbsection = sectionRepository.findAll().size();
        sectionRepository.save(section);
        assertThat(sectionRepository.findAll().size()).isEqualTo(nbsection+1);
        assertThat(section.getId()).isNotNull();
    }
    /*...*/

    /**<ul>
     * Méthode consulterCategorie:
     * Test unitaire de consultation d'une catégorie de règle de golf
     * <li>Vérifie que l'Id fournit n'est pas nul ou négatif</li>
     * <li>Vérifie qu'une catégorie correspond à cet Id</li>
     * <li>Lance une exception si l'Id fournit ne correspond à aucune catégorie</li>
     * </ul>
     *
     * @see Section
     * @see SectionRepository
     */
    @Test
    void consulterCategorie() /*Long idCat en paramètre*/ {
        Long idCategorie = 9L;
        assertThat(idCategorie).isNotNull().isNotNegative();
        Optional<Section> cat = sectionRepository.findById(idCategorie);
        if (cat.isPresent()){
            assertThat(cat.get().getId()).isNotNull(); //je vérifie que je récupère bien la catégorie
        }else {
            fail("Catégorie introuvable");
        }
    }

    /**<ul>
     * Test unitaire pour modifier une catégorie de règle de golf
     * <li>Vérifie que l'Id fournit n'est pas nul ou négatif</li>
     * <li>Vérifie que la catégorie est présente</li>
     * <li>Modifie la catégorie</li>
     * <li>Annule l'opération si l'objet fournit est invalide</li>
     * </ul>
     *
     * @see Section
     * @see SectionRepository
     */
    @Test
    @Transactional
    void modifierCategorie() /*Section categorie*/ {
        Long idCat = 15L; /*on récupère normalement l'Id à partir de categorie(en paramètre)*/
        long nbCat = sectionRepository.count();
        assertThat(idCat).isNotNull().isNotNegative();
        Optional<Section> optCat = sectionRepository.findById(idCat);
        if (optCat.isPresent()){
            Section cat = optCat.get();
            cat.setTitle("test unitaire : modifier la catégorie");
            cat.setCode("A");
            cat.setLang("fr");
            cat.setId(idCat);
            Section categorie = sectionRepository.saveAndFlush(cat);
            assertThat(cat.getTitle()).isEqualTo(categorie.getTitle()); //on vérifie que la cat est bien modifiée
            assertThat(nbCat).isEqualTo(sectionRepository.count()); //on vérifie qu'on a pas ajouté une cat
        }else {
            fail("Catégorie introuvable");
        }
    }

    /**<ul>
     * Test unitaire pour supprimer une catégorie de règle de golf
     * <li>Vérifie que l'Id fournit n'est pas nul ou négatif</li>
     * <li>Vérifie que la catégorie est présente</li>
     * <li>Supprime la catégorie</li>
     * <li>Annule l'opération si l'id ne correspond à aucune catégorie</li>
     * </ul>
     *
     * @see Section
     * @see SectionRepository
     */
    @Test @Transactional
    void supprimerCategorie() /*Long idCategorie*/ {
        Long idCat = 15L;
        assertThat(idCat).isNotNull().isNotNegative();
        int nbCat = sectionRepository.findAll().size();
        Optional<Section> optCat = sectionRepository.findById(idCat);
        if (optCat.isPresent()){
            sectionRepository.delete(optCat.get());
            assertThat(sectionRepository.findAll().size()).isEqualTo(nbCat-1);
        }else {
            fail("Catégorie introuvable");
        }
    }
}
