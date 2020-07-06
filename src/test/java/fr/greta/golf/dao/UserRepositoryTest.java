package fr.greta.golf.dao;

import fr.greta.golf.entities.Language;
import fr.greta.golf.entities.Role;
import fr.greta.golf.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LangRepository langRepository;

    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**<ul>
     * Méthode ajouterUtilisateur:
     * Test unitaire d'ajout d'un utilisateur
     * <li>username doit être unique</li>
     * <li>Persiste la nouvel utilisateur</li>
     * <li>Annule l'opération si l'objet fournit est invalide</li>
     * </ul>
     *
     * @see User
     * @see UserRepository
     */
    @Test
    @Transactional
    void ajouterUtilisateur() {
        long nbUsers = userRepository.count();
        User user = new User();
        String mdp = passwordEncoder.encode("123456789");

        user.setUsername("Test_toto1"); //Pour être valide la chaîne de caractère doit être d'au moins 5 caractères
        Optional<User> userOpt = userRepository.findById(user.getUsername());
        if (!userOpt.isPresent()){
            user.setPassword(mdp); //Pour être valide la chaîne de caractère doit être entre 8 et 255 caractères
            user.setActive(true); //Ne peut être null
            userRepository.save(user); //Ceci doit marcher car l'objet user est valide
            assertThat(userRepository.count()).isEqualTo(nbUsers+1);
            assertThat(passwordEncoder.encode("123456789")).isNotEqualTo(user.getPassword());
            /*cet assert doit renvoyé une exception car chaque même si les mots de passe sont équivalents, leur BCrypt hash est différent */

        }
    }

    /**<ul>
     * Méthode modifierUtilisateur:
     * Test unitaire de modification d'un utilisateur
     * <li>utilisateur doit être présent ds la BDD</li>
     * <li>on modifie l'utilisateur et on vérifie si les changements sont bien effectués</li>
     * <li>Annule l'opération si l'objet fournit est invalide</li>
     * </ul>
     *
     * @see User
     * @see UserRepository
     * @see RoleRepository
     * @see LangRepository
     */
    @Test
    @Transactional
    void modifierUtilisateur() {
        long nbUsers = userRepository.count(); //Nombre d'utilisateurs ds la BDD
        assertThat(nbUsers).isNotNull();
        User user = new User();
        user.setUsername("Test_toto1"); //on simule la réception du 'username' en paramètre
        Optional<User> u = userRepository.findById(user.getUsername());
        if (u.isPresent())
            user = u.get();
        assertThat(user.getPassword()).isNotNull();//on vérifie la présence de l'utilisateur

        assertThat(roleRepository.count()).isNotNull(); //on vérifie la présence de rôle(s)
        Role role = roleRepository.findAll().get(0); //on récupère le premier rôle
        user.getRoles().add(role); //l'utilisateur doit au moins avoir un rôle
        assertThat(langRepository.count()).isNotNull(); //on vérifie la présence de langue(s)
        Language lang = langRepository.findAll().get(0); //on récupère le premier langue
        user.getLanguages().add(lang); //l'utilisateur doit avoir au moins une langue d'attribuée

        user.setActive(false); //on désactive cet utilisateur
        userRepository.save(user); //on sauvegarde la modification
        assertThat(userRepository.count()).isEqualTo(nbUsers); //Le nombre d'utilisateur n'a pas changé
        Optional<User> user1 = userRepository.findById(user.getUsername());
        user1.ifPresent(value -> assertThat(value.isActive()).isEqualTo(false));
        //on vérifie que l'utilisateur est inactif

        user.setActive(true);
        userRepository.save(user);
        Optional<User> user2 = userRepository.findById(user.getUsername());
        user2.ifPresent(value -> assertThat(value.isActive()).isEqualTo(true));
    }

    /**<p>
     * Méthode consulterUtilisateur:
     * Test unitaire pour consulter un utilisateur, condition : l'utilisateur doit être présent ds la BDD
     * </p>
     *
     * @see User
     * @see UserRepository
     */
    @Test
    void consulterUtilisateur() /*Long isUser en paramètre*/ {
        User user = new User();
        Optional<User> userOptional = userRepository.findById("Test_toto1");
        if (userOptional.isPresent())
            user = userOptional.get();
        assertThat(user.getUsername()).isNotNull();
        System.out.println(user.toString());
    }

    /**<p>
     * Méthode deleteUtilisateur:
     * Test unitaire pour supprimer un utilisateur, condition : l'utilisateur doit être présent ds la BDD
     * </p>
     *
     * @see User
     * @see UserRepository
     */
    @Test
    void deleteUtilisateur() /*Long isUser en paramètre*/ {
        long nbUser = userRepository.count();
        User user = new User();
        Optional<User> userOptional = userRepository.findById("Test_toto1");
        if (userOptional.isPresent())
            user = userOptional.get();
        assertThat(user.getUsername()).isNotNull();

        userRepository.delete(user);
        assertThat(userRepository.count()).isEqualTo(nbUser-1);
    }
}
