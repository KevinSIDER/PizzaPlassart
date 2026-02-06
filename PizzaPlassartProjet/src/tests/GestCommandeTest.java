package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Client;
import pizzas.Commande;
import pizzas.CommandeException;
import pizzas.Compte;
import pizzas.EtatCommande;
import pizzas.GestCommande;
import pizzas.InformationPersonnelle;
import pizzas.NonConnecteException;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Classe de tests unitaires pour la classe {@link GestCommande}.
 * 
 * <p>Cette classe teste les fonctionnalités principales du gestionnaire de
 * commandes, notamment la création, la validation et l'annulation de commandes
 * de pizzas, ainsi que la gestion de la connexion des clients.
 *
 * @author Léo Montay
 * @version 1.0
 */
public class GestCommandeTest {
  
  /**
   * Le gestionnaire de commandes testé.
   */
  private GestCommande gestionnaire;
  
  /**
   * Le client utilisé pour les tests.
   */
  private Client client;
  
  /**
   * Une pizza utilisée dans les tests.
   */
  private Pizza pizza;
  
  /**
   * La commande en cours utilisée dans les tests.
   */
  private Commande commande;
  
  /**
   * Initialise l'environnement de test avant chaque test.
   * 
   * <p>Cette méthode crée un client avec ses informations personnelles, un
   * gestionnaire de commandes connecté, une pizza de test et démarre une
   * nouvelle commande.
   *
   * @throws NonConnecteException si la connexion du client échoue
   */
  @BeforeEach
  void setUp() throws NonConnecteException {
    // 1. Nettoyage de la mémoire statique (important pour les tests)
    Compte.resetMemoire();
    
    // 2. Création et connexion via Compte (pour que Compte.getClientConnecte()
    // ne soit pas null)
    InformationPersonnelle info =
        new InformationPersonnelle("Dupont", "Jean", "1 Rue du Parc", 30);
    Compte.inscription("jeandupont@example.com", "mdp", info);
    Compte.connexion("jeandupont@example.com", "mdp");
    
    // Récupération du client créé par Compte
    client = Compte.getClientConnecte();
    
    // 3. Configuration du gestionnaire
    gestionnaire = new GestCommande();
    gestionnaire.connecter(client); // Met à jour le clientConnecte interne de GestCommande
    
    // Création d'une pizza
    pizza = new Pizza("Margherita", TypePizza.Vegetarienne);
    pizza.setPrix(8.5);
    
    // Début d'une commande
    commande = GestCommande.debuterCommande();
  }
  
  /**
   * Teste la création d'une nouvelle commande.
   * 
   * <p>Vérifie que la commande créée n'est pas nulle, qu'elle est bien associée au
   * client connecté et qu'elle est dans l'état CREE.
   */
  @Test
  void testDebuterCommande() {
    assertNotNull(commande);
    assertEquals(client, commande.getClient());
    assertEquals(EtatCommande.CREE, commande.getEtat());
  }
  
  /**
   * Teste l'ajout de pizzas à une commande.
   * 
   * <p>Vérifie que les pizzas sont correctement ajoutées à la commande avec la
   * quantité spécifiée et que le prix total est calculé correctement.
   *
   * @throws NonConnecteException si le client n'est pas connecté
   * @throws CommandeException si l'ajout de pizza échoue
   */
  @Test
  void testAjouterPizza() throws NonConnecteException, CommandeException {
    gestionnaire.ajouterPizza(pizza, 2, commande);
    assertEquals(2, commande.getPizzas().size());
    assertEquals(17.0, commande.getPrixTotal(), 0.001);
  }
  
  /**
   * Teste la validation d'une commande.
   * 
   * <p>Vérifie que l'état de la commande passe à VALIDEE après validation.
   *
   * @throws NonConnecteException si le client n'est pas connecté
   * @throws CommandeException si la validation échoue
   */
  @Test
  void testValiderCommande() throws NonConnecteException, CommandeException {
    gestionnaire.validerCommande(commande);
    assertEquals(EtatCommande.VALIDEE, commande.getEtat());
  }
  
  /**
   * Teste l'annulation d'une commande.
   * 
   * <p>Vérifie que la commande est correctement supprimée de la liste des
   * commandes en cours du client après annulation.
   *
   * @throws NonConnecteException si le client n'est pas connecté
   * @throws CommandeException si l'annulation échoue
   */
  @Test
  void testAnnulerCommande() throws NonConnecteException, CommandeException {
    gestionnaire.annulerCommande(commande);
    
    // Vérifie que le client n'a plus cette commande dans les commandes en cours
    assertFalse(client.getCommandesEnCours().contains(commande));
  }
  
  /**
   * Teste la levée d'une exception lorsqu'un client non connecté tente de
   * débuter une commande.
   * 
   * <p>Vérifie que la méthode {@code debuterCommande()} lève bien une
   * {@link NonConnecteException} lorsque le client est déconnecté.
   */
  @Test
  void testNonConnecteException() {
    gestionnaire.deconnecter();
    Compte.deconnexion();
    assertThrows(NonConnecteException.class,
        () -> GestCommande.debuterCommande());
  }
}
