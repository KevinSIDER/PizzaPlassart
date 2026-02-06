package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;

/**
 * Tests JUnit de la classe {@link pizzas.Ingredient Ingredient}.
 *
 * <p>Ces tests vérifient :</p>
 * <ul>
 * <li>les getters et setters</li>
 * <li>la redéfinition de {@code equals} et {@code hashCode}</li>
 * <li>le format d'affichage avec {@code toString}</li>
 * </ul>
 */
public class TestIngredient {

  /**
   * Ingrédient tomate utilisé pour les tests.
   */
  private Ingredient tomate;

  /**
   * Ingrédient jambon utilisé pour les tests.
   */
  private Ingredient jambon;

  /**
   * Initialise les ingrédients avant chaque test.
   */
  @BeforeEach
  void setUp() {
    tomate = new Ingredient("Tomate", 0.50);
    jambon = new Ingredient("Jambon", 2.00);
  }

  /**
   * Vérifie le bon fonctionnement des getters.
   */
  @Test
  void testGetters() {
    assertEquals("Tomate", tomate.getNom());
    assertEquals(0.50, tomate.getPrix());
  }

  /**
   * Vérifie que le setter du prix modifie correctement la valeur.
   */
  @Test
  void testSetPrix() {
    tomate.setPrix(0.60);
    assertEquals(0.60, tomate.getPrix());
  }

  /**
   * Vérifie la méthode {@code equals}.
   *
   * <p>Deux ingrédients sont considérés comme égaux
   * s'ils possèdent le même nom, indépendamment du prix.
   */
  @Test
  void testEquals() {
    Ingredient autreTomate =
        new Ingredient("Tomate", 10.0); // Même nom, prix différent
    Ingredient fauxJambon =
        new Ingredient("JambonCru", 2.00);

    assertEquals(tomate, autreTomate,
        "Deux ingrédients avec le même nom doivent être égaux");
    assertNotEquals(tomate, jambon);
    assertNotEquals(jambon, fauxJambon);
  }

  /**
   * Vérifie la cohérence entre {@code equals} et {@code hashCode}.
   *
   * <p>Deux ingrédients égaux doivent avoir le même hashCode.
   */
  @Test
  void testHashCode() {
    Ingredient autreTomate = new Ingredient("Tomate", 0.99);

    assertEquals(tomate.hashCode(), autreTomate.hashCode(),
        "Le hashCode doit être identique si les noms sont identiques");
  }

  /**
   * Vérifie le format d'affichage français de la méthode {@code toString}.
   *
   * <p>Le prix doit :
   * <ul>
   *   <li>être affiché avec une virgule comme séparateur décimal</li>
   *   <li>contenir deux chiffres après la virgule</li>
   *   <li>être suivi du symbole euro (€)</li>
   * </ul>
   *
   * <p>Exemple attendu : {@code "Truffe (12,50€)"}
   */
  @Test
  void testToStringFormatFrancais() {
    Ingredient cher = new Ingredient("Truffe", 12.50);
    String affichage = cher.toString();

    assertEquals("Truffe (12,50€)", affichage);
  }
}
