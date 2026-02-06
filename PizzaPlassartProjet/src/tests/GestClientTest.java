package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Commande;
import pizzas.CommandeException;
import pizzas.GestClient;
import pizzas.InformationPersonnelle;
import pizzas.NonConnecteException;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Tests JUnit 5 de la classe {@link pizzas.GestClient}.
 *
 * @author Kevin SIDER
 */
public class GestClientTest {
  
  private GestClient gestClient;
  private InformationPersonnelle infoClient;
  private String email;
  private String mdp = "secret";
  private Pizza pizzaTest;
  
  @BeforeEach
  void setUp() {
    gestClient = new GestClient();
    
    // On génère un email unique pour chaque test pour éviter les conflits
    email = "test_" + UUID.randomUUID().toString() + "@pizzeria.fr";
    infoClient =
        new InformationPersonnelle("Martin", "Paul", "10 rue de la Paix", 25);
    pizzaTest = new Pizza("Reine", TypePizza.Viande);
    pizzaTest.setPrix(10.0);
  }
  
  @Test
  void testInscription() {
    int code = gestClient.inscription(email, mdp, infoClient);
    assertEquals(0, code, "L'inscription devrait réussir (code 0)");
    
    // Tentative de réinscription avec le même email
    int codeDoublon = gestClient.inscription(email, mdp, infoClient);
    assertEquals(2, codeDoublon,
        "L'inscription devrait échouer pour un email existant (code 2)");
  }
  
  @Test
  void testConnexionEtDeconnexion() throws NonConnecteException {
    gestClient.inscription(email, mdp, infoClient);
    
    boolean connecte = gestClient.connexion(email, mdp);
    assertTrue(connecte, "Le client devrait être connecté");
    
    gestClient.deconnexion();
    
    assertThrows(NonConnecteException.class, () -> {
      gestClient.debuterCommande();
    }, "Devrait lever une exception car déconnecté");
  }
  
  @Test
  void testConnexionEchouee() {
    gestClient.inscription(email, mdp, infoClient);
    // On teste avec un mauvais mot de passe
    boolean resultat = gestClient.connexion(email, "mauvaisMdp");
    assertFalse(resultat, "Connexion impossible avec mauvais mdp");
  }
  
  @Test
  void testCycleCommande() throws NonConnecteException, CommandeException {
    gestClient.inscription(email, mdp, infoClient);
    gestClient.connexion(email, mdp);
    
    Commande cmd = gestClient.debuterCommande();
    assertNotNull(cmd);
    
    gestClient.ajouterPizza(pizzaTest, 2, cmd);
    assertEquals(2, cmd.getPizzas().size());
    
    gestClient.validerCommande(cmd);
    
    // Vérifie que la commande est passée dans l'historique
    assertTrue(gestClient.getCommandePassees().contains(cmd));
  }
  
  @Test
  void testExceptionNonConnecte() {
    // Pas de connexion ici
    assertThrows(NonConnecteException.class, () -> {
      gestClient.debuterCommande();
    });
  }
  
  @Test
  void testAjouterEvaluation() throws NonConnecteException, CommandeException {
    gestClient.inscription(email, mdp, infoClient);
    gestClient.connexion(email, mdp);
    
    Commande cmd = gestClient.debuterCommande();
    gestClient.ajouterPizza(pizzaTest, 1, cmd);
    gestClient.validerCommande(cmd);
    
    boolean aevalue = gestClient.ajouterEvaluation(pizzaTest, 5, "Top !");
    
    assertTrue(aevalue);
    assertEquals(5.0, gestClient.getNoteMoyenne(pizzaTest));
  }
  
  @Test
  void testEvaluationInterdite() throws NonConnecteException {
    gestClient.inscription(email, mdp, infoClient);
    gestClient.connexion(email, mdp);
    
    // On n'a pas commandé la pizza
    
    assertThrows(CommandeException.class, () -> {
      gestClient.ajouterEvaluation(pizzaTest, 3, "Jamais gouté");
    });
  }
  
  @Test
  void testGetPizzas() {
    Set<Pizza> pizzas = gestClient.getPizzas();
    assertNotNull(pizzas);
  }
}
