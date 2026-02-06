package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Client;
import pizzas.Commande;
import pizzas.Compte;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.Statistique;
import pizzas.TypePizza;

/**
 * Tests JUnit de la classe {@link pizzas.Statistique Statistique}.
 *
 * <p>Ces tests vérifient :</p>
 * <ul>
 * <li>le calcul des bénéfices par pizza, commande et total</li>
 * <li>le nombre de pizzas commandées</li>
 * <li>le bénéfice généré par client</li>
 * <li>le classement des pizzas par popularité</li>
 * </ul>
 */
public class StatistiqueTest {
  
  /**
   * Première pizza utilisée pour les tests.
   */
  private Pizza p1;
  
  /**
   * Deuxième pizza utilisée pour les tests.
   */
  private Pizza p2;
  
  /**
   * Première commande (client 1).
   */
  private Commande c1;
  
  /**
   * Deuxième commande (client 2).
   */
  private Commande c2;
  
  /**
   * Informations personnelles du premier client.
   */
  private InformationPersonnelle infoClient1;
  
  /**
   * Informations personnelles du second client.
   */
  private InformationPersonnelle infoClient2;
  
  /**
   * Initialise les pizzas, clients et commandes avant chaque test.
   */
  @BeforeEach
  void setUp() {
    
    /*
     * ============================ 1. Préparation des pizzas
     * ============================
     */
    
    /*
     * P1 : - Coût ingrédients : 1.0€ - Coût réel avec marge interne : 1.4€ -
     * Prix de vente : 10.0€ - Bénéfice attendu : 8.6€
     */
    p1 = new Pizza("P1", TypePizza.Viande);
    p1.ajouterIngredient(new Ingredient("Ing1", 1.0));
    p1.setPrix(10.0);
    
    /*
     * P2 : - Coût ingrédients : 2.0€ - Coût réel avec marge interne : 2.8€ -
     * Prix de vente : 5.0€ - Bénéfice attendu : 2.2€
     */
    p2 = new Pizza("P2", TypePizza.Vegetarienne);
    p2.ajouterIngredient(new Ingredient("Ing2", 2.0));
    p2.setPrix(5.0);
    
    /*
     * ============================ 2. Préparation des clients
     * ============================
     */
    
    infoClient1 = new InformationPersonnelle("Nom1", "Prenom1", "Ad1", 20);
    infoClient2 = new InformationPersonnelle("Nom2", "Prenom2", "Ad2", 30);
    
    Compte compte1 = new Compte("c1@test.com", "mdp123", infoClient1);
    Client client1 = new Client(compte1);
    
    /*
     * ============================ 3. Préparation des commandes
     * ============================
     */
    
    /*
     * Commande C1 (client 1) : - 2 pizzas P1
     */
    c1 = new Commande(1, client1);
    c1.ajouterPizza(p1);
    c1.ajouterPizza(p1);
    
    Compte compte2 = new Compte("c2@test.com", "mdp456", infoClient2);
    Client client2 = new Client(compte2);
    
    /*
     * Commande C2 (client 2) : - 1 pizza P2
     */
    c2 = new Commande(1, client2);
    c2.ajouterPizza(p2);
  }
  
  /**
   * Vérifie le calcul du bénéfice pour une pizza.
   */
  @Test
  void testCalculerBeneficePizza() {
    assertEquals(8.6, Statistique.calculerBeneficePizza(p1), 0.001);
    assertEquals(2.2, Statistique.calculerBeneficePizza(p2), 0.001);
  }
  
  /**
   * Vérifie le calcul du bénéfice pour une commande.
   */
  @Test
  void testCalculerBeneficeCommande() {
    // C1 contient 2 pizzas P1 => 2 * 8.6 = 17.2
    assertEquals(17.2, Statistique.calculerBeneficeCommande(c1), 0.001);
  }
  
  /**
   * Vérifie le calcul du bénéfice total pour une liste de commandes.
   */
  @Test
  void testCalculerBeneficeTotal() {
    List<Commande> commandes = new ArrayList<>();
    commandes.add(c1); // 17.2
    commandes.add(c2); // 2.2
    
    assertEquals(19.4, Statistique.calculerBeneficeTotal(commandes), 0.001);
  }
  
  /**
   * Vérifie le nombre de pizzas commandées pour une pizza donnée.
   */
  @Test
  void testNombrePizzasCommandees() {
    List<Commande> commandes = new ArrayList<>();
    commandes.add(c1); // 2 x P1
    commandes.add(c2); // 1 x P2
    
    assertEquals(2, Statistique.nombrePizzasCommandees(commandes, p1));
    assertEquals(1, Statistique.nombrePizzasCommandees(commandes, p2));
  }
  
  /**
   * Vérifie le calcul du bénéfice généré par client.
   */
  @Test
  void testBeneficeParClient() {
    List<Commande> commandes = new ArrayList<>();
    commandes.add(c1);
    commandes.add(c2);
    
    Map<InformationPersonnelle, Double> res =
        Statistique.beneficeParClient(commandes);
    
    assertEquals(17.2, res.get(infoClient1), 0.001);
    assertEquals(2.2, res.get(infoClient2), 0.001);
  }
  
  /**
   * Vérifie le classement des pizzas en fonction du nombre de commandes.
   */
  @Test
  void testClassementPizzas() {
    List<Commande> commandes = new ArrayList<>();
    commandes.add(c1); // P1 commandée 2 fois
    commandes.add(c2); // P2 commandée 1 fois
    
    Set<Pizza> catalogue = new HashSet<>();
    catalogue.add(p1);
    catalogue.add(p2);
    
    List<Pizza> classement = Statistique.classementPizzas(commandes, catalogue);
    
    assertEquals(p1, classement.get(0));
    assertEquals(p2, classement.get(1));
  }
}
