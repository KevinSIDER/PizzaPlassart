package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Compte;
import pizzas.InformationPersonnelle;


/**
 * Tests JUnit de la classe {pizzas.Compte}.
 *
 * @author Yaouanc kevin
 *
 */

public class TestCompte {
  
  /**
   * Informations personnelles utilisÃ©es pour crÃ©er un compte.
   */
  private InformationPersonnelle info;
  
  /**
   * Un compte de rÃ©fÃ©rence .
   */
  private Compte compte;
  
  /**
   * PrÃ©pare les objets nÃ©cessaires et rÃ©initialise la mÃ©moire statique.
   *
   * @throws Exception ne peut pas Ãªtre levÃ©e ici
   */
  @BeforeEach
  void setUp() throws Exception {
    Compte.resetMemoire();
    info = new InformationPersonnelle("Skywalker", "Luke", "PlanÃ¨te Tatooine",
        20);
    compte = new Compte("luke@example.com", "secret", info);
  }
  
  /**
   * Nettoie la mÃ©moire statique aprÃ¨s chaque test.
   *
   * @throws Exception ne peut pas Ãªtre levÃ©e ici
   */
  @AfterEach
  void tearDown() throws Exception {
    Compte.resetMemoire();
  }
  
  /**
   * VÃ©rifie qu'une inscription valide fonctionne.
   */
  @Test
  void testInscriptionOk() {
    int res = Compte.inscription("luke@example.com", "secret", info);
    assertEquals(res, 0);
    assertEquals(Compte.getComptes().size(), 1);
  }
  
  /**
   * VÃ©rifie qu'une inscription avec email invalide Ã©choue.
   */
  @Test
  void testInscriptionEmailInvalide() {
    int res = Compte.inscription("pasUnEmail", "secret", info);
    assertEquals(res, 1);
    assertEquals(Compte.getComptes().size(), 0);
  }
  
  /**
   * VÃ©rifie qu'on ne peut pas s'inscrire avec un mot de passe vide.
   */
  @Test
  void testInscriptionMotDePasseVide() {
    int res = Compte.inscription("luke@example.com", "", info);
    assertEquals(res, 1);
    assertEquals(Compte.getComptes().size(), 0);
  }
  
  /**
   * VÃ©rifie qu'on ne peut pas s'inscrire deux fois avec le mÃªme email
   * (insensible Ã la casse).
   */
  @Test
  void testInscriptionDoublon() {
    assertEquals(Compte.inscription("luke@example.com", "secret", info), 0);
    assertEquals(Compte.inscription("LUKE@EXAMPLE.COM", "secret2", info), 2);
    assertEquals(Compte.getComptes().size(), 1);
  }
  
  /**
   * VÃ©rifie qu'une connexion valide connecte bien le client.
   */
  @Test
  void testConnexionOk() {
    assertEquals(Compte.inscription("luke@example.com", "secret", info), 0);
    boolean ok = Compte.connexion(" LUKE@example.com ", "secret");
    assertTrue(ok);
    assertTrue(Compte.getClientConnecte() != null);
  }
  
  /**
   * VÃ©rifie qu'une connexion avec mauvais mot de passe Ã©choue.
   */
  @Test
  void testConnexionMauvaisMotDePasse() {
    assertEquals(Compte.inscription("luke@example.com", "secret", info), 0);
    boolean ok = Compte.connexion("luke@example.com", "mauvais");
    assertTrue(!ok);
    assertTrue(Compte.getClientConnecte() == null);
  }
  
  /**
   * VÃ©rifie la dÃ©connexion.
   */
  @Test
  void testDeconnexion() {
    assertEquals(Compte.inscription("luke@example.com", "secret", info), 0);
    assertTrue(Compte.connexion("luke@example.com", "secret"));
    assertTrue(Compte.deconnexion());
    assertTrue(Compte.getClientConnecte() == null);
    assertTrue(!Compte.deconnexion());
  }
  
  /**
   * VÃ©rifie la normalisation d'email.
   */
  @Test
  void testNormaliserEmail() {
    assertEquals(Compte.normaliserEmail("  LUKE@Example.com  "),
        "luke@example.com");
  }
  
  /**
   * VÃ©rifie la validation d'email.
   */
  @Test
  void testEstEmailValide() {
    assertTrue(Compte.estEmailValide("luke@example.com"));
    assertTrue(!Compte.estEmailValide("luke@"));
    assertTrue(!Compte.estEmailValide(null));
  }
  
  /**
   * VÃ©rifie la correspondance email/mot de passe sur un compte.
   */
  @Test
  void testCorrespondaLogin() {
    assertTrue(compte.correspondaLogin(" LUKE@example.com ", "secret"));
    assertTrue(!compte.correspondaLogin("luke@example.com", "mauvais"));
  }
  
}
