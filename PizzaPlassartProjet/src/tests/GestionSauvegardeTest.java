package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.GestionSauvegarde;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Compte;
import pizzas.GestPizzaiolo;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.Pizzaiolo;
import pizzas.TypePizza;

/**
 * Tests JUnit 5 de la classe {@link io.GestionSauvegarde}.
 *
 * @author Kevin SIDER
 */
public class GestionSauvegardeTest {
  
  private GestPizzaiolo gestPizzaiolo;
  private GestionSauvegarde gestionSauvegarde;
  
  // Nom du fichier temporaire pour les tests
  private final String testfile = "test_donnees_pizzeria.txt";
  
  @BeforeEach
  void setUp() {
    // 1. On vide la mémoire statique des comptes avant chaque test
    Compte.resetMemoire();
    
    // 2. On prépare un gestionnaire vide
    Pizzaiolo pizzaiolo = new Pizzaiolo("MarioTest");
    gestPizzaiolo = new GestPizzaiolo(pizzaiolo);
    
    // 3. On initialise le gestionnaire de sauvegarde
    gestionSauvegarde = new GestionSauvegarde(gestPizzaiolo);
  }
  
  @AfterEach
  void tearDown() {
    // Nettoyage : on supprime le fichier créé après chaque test
    File file = new File(testfile);
    if (file.exists()) {
      file.delete();
    }
    Compte.resetMemoire();
  }
  
  @Test
  void testSauvegarderEtChargerComplet() {
    // --- PHASE 1 : PRÉPARATION DES DONNÉES ---
    
    // Création d'ingrédients
    gestPizzaiolo.creerIngredient("Tomate", 0.50);
    gestPizzaiolo.creerIngredient("Mozza", 1.00);
    gestPizzaiolo.creerIngredient("Chorizo", 1.50); // Sera interdit pour végé
    
    // Création d'une pizza
    Pizza p1 = gestPizzaiolo.creerPizza("ChorizoReale", TypePizza.Viande);
    gestPizzaiolo.ajouterIngredientPizza(p1, "Tomate");
    gestPizzaiolo.ajouterIngredientPizza(p1, "Chorizo");
    gestPizzaiolo.setPrixPizza(p1, 12.50);
    try {
      gestPizzaiolo.ajouterPhoto(p1, "image_test.jpg");
    } catch (IOException e) {
      fail("Erreur lors de l'ajout de la photo simulée");
    }
    
    // Création d'un client (via Compte)
    InformationPersonnelle info =
        new InformationPersonnelle("TestNom", "TestPrenom", "AdresseTest", 30);
    Compte.inscription("client@test.com", "password123", info);
    
    // Création d'une interdiction
    // On récupère l'objet ingrédient pour l'interdire
    // (Note: dans une vraie appli on utiliserait une méthode de recherche, ici
    // on filtre la liste)
    gestPizzaiolo.interdireIngredient("Chorizo", TypePizza.Vegetarienne);
    
    // --- PHASE 2 : SAUVEGARDE ---
    try {
      gestionSauvegarde.sauvegarderDonnees(testfile);
    } catch (IOException e) {
      fail("La sauvegarde a échoué : " + e.getMessage());
    }
    
    // --- PHASE 3 : RÉINITIALISATION (Pour simuler un redémarrage) ---
    Compte.resetMemoire(); // Plus de clients
    GestPizzaiolo nouveauGest = new GestPizzaiolo(new Pizzaiolo("LuigiTest"));
    GestionSauvegarde nouvelleSauvegarde = new GestionSauvegarde(nouveauGest);
    
    // --- PHASE 4 : CHARGEMENT ---
    try {
      nouvelleSauvegarde.chargerDonnees(testfile);
    } catch (IOException e) {
      fail("Le chargement a échoué : " + e.getMessage());
    }
    
    // --- PHASE 5 : VÉRIFICATIONS ---
    
    // 1. Vérif Ingrédients
    List<Ingredient> ingredients = nouveauGest.getIngredients();
    assertEquals(3, ingredients.size(), "Il devrait y avoir 3 ingrédients");
    boolean tomateTrouvee = ingredients.stream()
        .anyMatch(i -> i.getNom().equals("Tomate") && i.getPrix() == 0.50);
    assertTrue(tomateTrouvee,
        "L'ingrédient Tomate doit être restauré avec son prix");
    
    // 2. Vérif Pizzas
    Set<Pizza> pizzas = nouveauGest.getPizzas();
    assertEquals(1, pizzas.size(), "Il devrait y avoir 1 pizza");
    Pizza pizzaChargee = pizzas.iterator().next();
    assertEquals("ChorizoReale", pizzaChargee.getNom());
    assertEquals(TypePizza.Viande, pizzaChargee.getType());
    assertEquals(12.50, pizzaChargee.getPrix(), 0.01);
    assertEquals("image_test.jpg", pizzaChargee.getPhoto());
    assertEquals(2, pizzaChargee.getIngredients().size(),
        "La pizza doit avoir ses 2 ingrédients");
    
    // 3. Vérif Clients
    // On vérifie qu'on peut se connecter avec le compte restauré
    boolean connexionOk = Compte.connexion("client@test.com", "password123");
    assertTrue(connexionOk,
        "Le client devrait pouvoir se connecter avec ses identifiants sauvegardés");
    
    // Vérif des infos persos
    InformationPersonnelle infoChargee =
        Compte.getClientConnecte().getCompte().getInfoPersonnelle();
    assertEquals("TestNom", infoChargee.getNom());
    
    // 4. Vérif Interdictions
    // On cherche l'ingrédient Chorizo dans le nouveau gestionnaire
    Ingredient chorizo = ingredients.stream()
        .filter(i -> i.getNom().equals("Chorizo")).findFirst().orElse(null);
    assertNotNull(chorizo);
    assertTrue(
        nouveauGest.estIngredientInterdit(TypePizza.Vegetarienne, chorizo),
        "L'interdiction du Chorizo pour les pizzas Végétariennes doit être restaurée");
  }
  
  /**
   * Teste la robustesse si le fichier n'existe pas (doit lancer une exception).
   */
  @Test
  void testChargementFichierInexistant() {
    File f = new File("fichier_qui_n_existe_pas.txt");
    if (f.exists()) {
      f.delete(); // Sûreté
    }
    
    try {
      gestionSauvegarde.chargerDonnees("fichier_qui_n_existe_pas.txt");
      fail("Une IOException aurait dû être levée");
    } catch (IOException e) {
      // Succès, c'est ce qu'on attendait
      assertNotNull(e.getMessage());
    }
  }
}
