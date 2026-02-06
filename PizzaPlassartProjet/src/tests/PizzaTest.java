package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Tests JUnit de la classe {@link pizzas.Pizza Pizza}.
 *
 * <p>Ces tests vérifient :</p>
 * <ul>
 * <li>la gestion des ingrédients (ajout, suppression, doublons)</li>
 * <li>le calcul du prix minimal d'une pizza</li>
 * <li>la gestion du type de pizza</li>
 * </ul>
 */
public class PizzaTest {

  /**
   * Pizza utilisée pour les tests (pizza Reine).
   */
  private Pizza pizzaReine;

  /**
   * Ingrédient tomate.
   */
  private Ingredient tomate;

  /**
   * Ingrédient fromage.
   */
  private Ingredient fromage;

  /**
   * Ingrédient jambon.
   */
  private Ingredient jambon;

  /**
   * Initialise les objets nécessaires avant chaque test.
   */
  @BeforeEach
  void setUp() {
    pizzaReine = new Pizza("Reine", TypePizza.Viande);
    tomate = new Ingredient("Tomate", 1.00);
    fromage = new Ingredient("Fromage", 2.00);
    jambon = new Ingredient("Jambon", 1.50);
  }

  /**
   * Vérifie que l'ajout d'un ingrédient fonctionne correctement.
   */
  @Test
  void testAjoutIngredient() {
    pizzaReine.ajouterIngredient(tomate);

    assertEquals(1, pizzaReine.getIngredients().size());
    assertTrue(pizzaReine.getIngredients().contains(tomate));
  }

  /**
   * Vérifie que l'ajout d'un ingrédient déjà présent
   * ne crée pas de doublon.
   */
  @Test
  void testAjoutDoublonIngredient() {
    pizzaReine.ajouterIngredient(tomate);
    pizzaReine.ajouterIngredient(tomate); // Deuxième ajout du même ingrédient

    assertEquals(1, pizzaReine.getIngredients().size(),
        "On ne doit pas avoir de doublons");
  }

  /**
   * Vérifie que la suppression d'un ingrédient fonctionne correctement.
   */
  @Test
  void testRetirerIngredient() {
    pizzaReine.ajouterIngredient(tomate);
    pizzaReine.ajouterIngredient(jambon);

    pizzaReine.retirerIngredient(tomate);

    assertEquals(1, pizzaReine.getIngredients().size());
    assertFalse(pizzaReine.getIngredients().contains(tomate));
    assertTrue(pizzaReine.getIngredients().contains(jambon));
  }

  /**
   * Vérifie le calcul du prix minimal pour une pizza simple.
   *
   * <p>Exemple :
   * <ul>
   *   <li>Tomate : 1.00€</li>
   *   <li>Fromage : 2.00€</li>
   *   <li>Total ingrédients : 3.00€</li>
   *   <li>Marge de 40% : 3.00 * 1.4 = 4.20€</li>
   * </ul>
   */
  @Test
  void testCalculPrixMinimalSimple() {
    pizzaReine.ajouterIngredient(tomate);
    pizzaReine.ajouterIngredient(fromage);

    assertEquals(4.20, pizzaReine.calculerPrixMinimal(), 0.001);
  }

  /**
   * Vérifie que le prix minimal est correctement arrondi
   * à la dizaine de centimes supérieure.
   *
   * <p>Exemple :
   * <ul>
   *   <li>Ingrédient à 1.11€</li>
   *   <li>+40% = 1.11 * 1.4 = 1.554€</li>
   *   <li>Arrondi attendu : 1.60€</li>
   * </ul>
   */
  @Test
  void testCalculPrixMinimalArrondiSup() {
    Ingredient trucCher = new Ingredient("Or", 1.11);
    pizzaReine.ajouterIngredient(trucCher);

    assertEquals(1.60, pizzaReine.calculerPrixMinimal(), 0.001);
  }

  /**
   * Vérifie que le type de pizza peut être modifié.
   */
  @Test
  void testSetType() {
    pizzaReine.setType(TypePizza.Vegetarienne);

    assertEquals(TypePizza.Vegetarienne, pizzaReine.getType());
  }
}
