package tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Filtre;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Tests JUnit 5 de la classe {@link pizzas.Filtre}.
 *
 * @author Kevin SIDER
 */
public class FiltreTest {
  
  // Création des ingrédients pour les tests
  private final Ingredient tomate = new Ingredient("Tomate", 0.50);
  private final Ingredient fromage = new Ingredient("Fromage", 1.20);
  private final Ingredient jambon = new Ingredient("Jambon", 2.00);
  private final Ingredient champignon = new Ingredient("Champignon", 1.00);
  private final Ingredient truffe = new Ingredient("Truffe", 5.00);
  
  // Création des pizzas pour les tests
  private final Pizza p1 =
      creerPizza("Margherita", TypePizza.Vegetarienne, 2.40, tomate, fromage);
  private final Pizza p2 = creerPizza("Reine", TypePizza.Viande, 5.20, tomate,
      fromage, jambon, champignon);
  private final Pizza p3 = creerPizza("Végétale", TypePizza.Vegetarienne, 3.40,
      tomate, fromage, champignon);
  private final Pizza p4 = creerPizza("4 Fromages", TypePizza.Vegetarienne,
      10.00, fromage, new Ingredient("Chèvre", 3.00));
  private final Pizza p5 =
      creerPizza("Truffe Royale", TypePizza.Regionale, 25.00, truffe, fromage);
  
  // Variables pour les filtres
  private Set<Pizza> pizzas;
  private Filtre filtre;
  
  /**
   * Méthode utilitaire pour créer une pizza pour le test.
   */
  private Pizza creerPizza(String nom, TypePizza type, double prixVente,
      Ingredient... ingredients) {
    Pizza p = new Pizza(nom, type);
    for (Ingredient ing : ingredients) {
      p.ajouterIngredient(ing);
    }
    p.setPrix(prixVente);
    return p;
  }
  
  /**
   * Initialisation des pizzas et du filtre avant chaque test. REMARQUE
   * : @Before devient @BeforeEach en JUnit 5
   */
  @BeforeEach
  public void setUp() {
    pizzas = new HashSet<>(Arrays.asList(p1, p2, p3, p4, p5));
    filtre = new Filtre(pizzas);
  }
  
  /**
   * Teste que le filtre retourne toutes les pizzas sans filtre.
   */
  @Test
  public void testConstructeurSansFiltre() {
    assertEquals(pizzas, filtre.selectionPizzaFiltres());
    assertEquals(5, filtre.selectionPizzaFiltres().size());
  }
  
  /**
   * Teste le filtre par type (Végétarienne : P1, P3, P4).
   */
  @Test
  public void testAjouterFiltreParTypeVegetarienne() {
    filtre.ajouterFiltre(TypePizza.Vegetarienne);
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertEquals(3, resultats.size());
    assertTrue(resultats.containsAll(Arrays.asList(p1, p3, p4)));
    assertFalse(resultats.contains(p2));
  }
  
  /**
   * Teste le filtre par type (Régionale : P5).
   */
  @Test
  public void testAjouterFiltreParTypeRegionale() {
    filtre.ajouterFiltre(TypePizza.Regionale);
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertEquals(1, resultats.size());
    assertTrue(resultats.contains(p5));
  }
  
  /**
   * Teste l'exception lors de l'ajout d'un type null.
   */
  @Test
  public void testAjouterFiltreParTypeNull() {
    assertThrows(IllegalArgumentException.class,
        () -> filtre.ajouterFiltre((TypePizza) null));
  }
  
  /**
   * Teste le filtre par prix maximum (Prix &lt;= 5.00€ : P1, P3, P2).
   */
  @Test
  public void testAjouterFiltreParPrixValide() {
    filtre.ajouterFiltre(5.00);
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    assertEquals(2, resultats.size());
    // On change le filtre pour un prix un peu plus élevé
    filtre.supprimerFiltres();
    filtre.ajouterFiltre(5.20);
    Set<Pizza> resultats2 = filtre.selectionPizzaFiltres();
    // P2 coute 5.20, donc <= 5.20 doit l'inclure
    assertEquals(3, resultats2.size());
    // Ici P1(2.40), P3(3.40), P2(5.20) -> P4(10.00) exclu, P5(25.00) exclu.
    assertTrue(resultats2.containsAll(Arrays.asList(p1, p3, p2)));
  }
  
  /**
   * Teste le filtre par prix avec une valeur qui ne retourne aucun résultat.
   */
  @Test
  public void testAjouterFiltreParPrixAucunResultat() {
    filtre.ajouterFiltre(1.00);
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertTrue(resultats.isEmpty());
  }
  
  /**
   * Teste l'exception pour un prix inférieur ou égal à 0.
   */
  @Test
  public void testAjouterFiltreParPrixInvalide() {
    assertThrows(IllegalArgumentException.class,
        () -> filtre.ajouterFiltre(0.0));
    assertThrows(IllegalArgumentException.class,
        () -> filtre.ajouterFiltre(-1.0));
    
    assertDoesNotThrow(() -> filtre.ajouterFiltre(0.01));
  }
  
  /**
   * Teste le filtre par ingrédient unique (Jambon : P2).
   */
  @Test
  public void testAjouterFiltreParIngredientUnique() {
    filtre.ajouterFiltre("jambon");
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertEquals(1, resultats.size());
    assertTrue(resultats.contains(p2));
  }
  
  /**
   * Teste le filtre par ingrédients multiples (Tomate ET Fromage : P1, P2, P3).
   */
  @Test
  public void testAjouterFiltreParIngredientMultiple() {
    filtre.ajouterFiltre("tomate", "fromage");
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    // P1 (Tomate, Fromage) -> OK
    // P2 (Tomate, Fromage, Jambon...) -> OK
    // P3 (Tomate, Fromage, Champignon) -> OK
    // P4 (Fromage, Chèvre) -> NON (Manque Tomate)
    // P5 (Truffe, Fromage) -> NON (Manque Tomate)
    assertEquals(3, resultats.size());
    assertTrue(resultats.containsAll(Arrays.asList(p1, p2, p3)));
    assertFalse(resultats.contains(p5));
  }
  
  /**
   * Teste le filtre par ingrédient avec une casse différente.
   */
  @Test
  public void testAjouterFiltreParIngredientCasse() {
    filtre.ajouterFiltre("JaMbOn");
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertEquals(1, resultats.size());
    assertTrue(resultats.contains(p2));
  }
  
  /**
   * Teste l'exception lors de l'ajout d'un tableau d'ingrédients null.
   */
  @Test
  public void testAjouterFiltreParIngredientNullTableau() {
    assertThrows(IllegalArgumentException.class,
        () -> filtre.ajouterFiltre((String[]) null));
  }
  
  /**
   * Teste la combinaison des filtres : Type (Viande : P2) + Prix (&lt;= 5.00€).
   */
  @Test
  public void testCombinaisonFiltresTypeEtPrix() {
    filtre.ajouterFiltre(TypePizza.Viande); // Garde seulement P2 (5.20€)
    filtre.ajouterFiltre(5.00); // Prix Max 5.00€ -> P2 est trop chère
    
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertTrue(resultats.isEmpty());
  }
  
  /**
   * Teste la combinaison des filtres : Ingrédient + Type.
   */
  @Test
  public void testCombinaisonFiltresTypeEtIngredients() {
    filtre.ajouterFiltre(TypePizza.Vegetarienne); // P1, P3, P4
    filtre.ajouterFiltre("champignon"); // Parmi eux, seul P3 a du champignon
    // (P2 en a aussi, mais n'est pas Végétarienne)
    
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertEquals(1, resultats.size());
    assertTrue(resultats.contains(p3));
  }
  
  /**
   * Teste la combinaison des trois filtres.
   */
  @Test
  public void testCombinaisonFiltresTroisFiltres() {
    filtre.ajouterFiltre(TypePizza.Regionale); // P5
    filtre.ajouterFiltre("truffe"); // P5 a truffe
    filtre.ajouterFiltre(30.00); // P5 coute 25.00 -> OK
    
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertEquals(1, resultats.size());
    assertTrue(resultats.contains(p5));
  }
  
  /**
   * Teste la combinaison des filtres qui ne donne aucun résultat.
   */
  @Test
  public void testCombinaisonFiltresAucunResultat() {
    filtre.ajouterFiltre(TypePizza.Viande); // P2
    filtre.ajouterFiltre("truffe"); // P2 n'a pas de truffe
    
    Set<Pizza> resultats = filtre.selectionPizzaFiltres();
    
    assertTrue(resultats.isEmpty());
  }
  
  /**
   * Teste la suppression des filtres.
   */
  @Test
  public void testSupprimerFiltres() {
    filtre.ajouterFiltre(TypePizza.Vegetarienne);
    filtre.ajouterFiltre("champignon");
    
    assertEquals(1, filtre.selectionPizzaFiltres().size());
    
    filtre.supprimerFiltres();
    
    // La sélection doit retourner toutes les pizzas
    assertEquals(pizzas.size(), filtre.selectionPizzaFiltres().size());
    assertTrue(filtre.selectionPizzaFiltres().containsAll(pizzas));
    
    // Tester que l'ajout d'un nouveau filtre fonctionne après suppression
    filtre.ajouterFiltre(TypePizza.Regionale);
    assertEquals(1, filtre.selectionPizzaFiltres().size());
    assertTrue(filtre.selectionPizzaFiltres().contains(p5));
  }
}
