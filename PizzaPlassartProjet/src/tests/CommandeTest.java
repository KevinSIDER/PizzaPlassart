package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Client;
import pizzas.Commande;
import pizzas.CommandeException;
import pizzas.Compte;
import pizzas.EtatCommande;
import pizzas.InformationPersonnelle;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Classe de test unitaire pour la classe {@link Commande}.
 * 
 * <p>Cette classe teste l'ensemble des fonctionnalités liées à la gestion des
 * commandes de pizzas, notamment l'ajout et le retrait de pizzas, le calcul du
 * prix total et la validation des commandes.
 *
 * @author Léo Montay
 * @version 1.0
 */
public class CommandeTest {
  
  /**
   * Instance de la commande testée.
   */
  private Commande commande;
  
  /**
   * Client associé à la commande.
   */
  private Client client;
  
  /**
   * Compte du client.
   */
  private Compte compte;
  
  /**
   * Première pizza utilisée dans les tests (Margherita).
   */
  private Pizza pizza1;
  
  /**
   * Deuxième pizza utilisée dans les tests (Pepperoni).
   */
  private Pizza pizza2;
  
  /**
   * Méthode d'initialisation exécutée avant chaque test.
   * 
   * <p>Crée un client avec ses informations personnelles, un compte, une commande
   * et deux pizzas de test.
   */
  @BeforeEach
  void setUp() {
    // Création d'un compte et d'un client
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean", "1 Rue du Parc", 30);
    compte = new Compte("jeandupont@example.com", "motdepasse", info);
    client = new Client(compte);
    
    // Création de la commande
    commande = new Commande(1, client);
    
    // Création des pizzas
    pizza1 = new Pizza("Margherita", TypePizza.Vegetarienne);
    pizza1.setPrix(8.5);
    
    pizza2 = new Pizza("Pepperoni", TypePizza.Viande);
    pizza2.setPrix(10.0);
  }
  
  /**
   * Teste l'ajout d'une pizza à la commande.
   * 
   * <p>Vérifie que la pizza est correctement ajoutée à la liste des pizzas, que la
   * taille de la liste est incrémentée et que le prix total est mis à jour.
   */
  @Test
  void testAjoutPizza() {
    commande.ajouterPizza(pizza1);
    assertEquals(1, commande.getPizzas().size());
    assertTrue(commande.getPizzas().contains(pizza1));
    assertEquals(8.5, commande.getPrixTotal(), 0.001);
  }
  
  /**
   * Teste le retrait d'une pizza de la commande.
   * 
   * <p>Vérifie que la pizza retirée n'est plus présente dans la liste, que la
   * taille de la liste est décrémentée et que le prix total est recalculé
   * correctement.
   */
  @Test
  void testRetraitPizza() {
    commande.ajouterPizza(pizza1);
    commande.ajouterPizza(pizza2);
    commande.retirerPizza(pizza1);
    
    assertEquals(1, commande.getPizzas().size());
    assertFalse(commande.getPizzas().contains(pizza1));
    assertEquals(10.0, commande.getPrixTotal(), 0.001);
  }
  
  /**
   * Teste le calcul du prix total de la commande.
   * 
   * <p>Vérifie que le prix total est recalculé correctement après modification du
   * prix d'une pizza dans la commande.
   */
  @Test
  void testCalculerPrixTotal() {
    commande.ajouterPizza(pizza1);
    commande.ajouterPizza(pizza2);
    
    // Modification du prix pour tester le recalcul
    pizza1.setPrix(9.0);
    commande.calculerPrixTotal();
    
    assertEquals(19.0, commande.getPrixTotal(), 0.001);
  }
  
  /**
   * Teste la validation d'une commande en état CREE.
   * 
   * <p>Vérifie que l'état de la commande passe de CREE à VALIDEE après l'appel de
   * la méthode valider().
   *
   * @throws CommandeException si la validation échoue
   */
  @Test
  void testValiderCommande() throws CommandeException {
    assertEquals(EtatCommande.CREE, commande.getEtat());
    
    commande.valider();
    assertEquals(EtatCommande.VALIDEE, commande.getEtat());
  }
  
  /**
   * Teste la levée d'exception lors de la validation d'une commande déjà
   * validée.
   * 
   * <p>Vérifie qu'une {@link CommandeException} est levée lorsqu'on tente de
   * valider une commande qui est déjà dans l'état VALIDEE.
   */
  @Test
  void testValiderCommandeException() {
    commande.setEtat(EtatCommande.VALIDEE);
    
    assertThrows(CommandeException.class, () -> commande.valider());
  }
  
  /**
   * Teste les accesseurs (getters) de la classe Commande.
   * 
   * <p>Vérifie que les méthodes getIdCommande(), getClient() et getEtat()
   * retournent les valeurs attendues après l'initialisation.
   */
  @Test
  void testGetters() {
    assertEquals(1, commande.getIdCommande());
    assertEquals(client, commande.getClient());
    assertEquals(EtatCommande.CREE, commande.getEtat());
  }
}
