package pizzas;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe permettant de filtrer un ensemble de pizzas en fonction de différents
 * critères. Les filtres disponible sont : - par le type de pizza - par
 * ingrédients présents dans la pizza - par prix maximum. Les filtres sont
 * cumulables et peuvent être réinitialisés.
 *
 * @author Kevin SIDER
 * @version 1.0
 */
public class Filtre {
  
  /** Ensemble des pizzas sur lesquelles les filtres seront appliqués. */
  private Set<Pizza> pizzas;
  
  /** Ensemble des ingrédients pour filtrer. */
  private final Set<String> ingredientsFiltre = new HashSet<>();
  
  /** Type de pizza à filtrer. */
  private TypePizza typeFiltre;
  
  /** Prix maximum pour filtrer les pizzas. */
  private Double prixMaximum;
  
  /**
   * Initialise le filtre avec l'ensemble des pizzas à filtrer.
   *
   * @param pizzas l'ensemble des pizzas à filtrer
   */
  public Filtre(Set<Pizza> pizzas) {
    this.pizzas = pizzas;
    this.typeFiltre = null;
    this.prixMaximum = null;
  }
  
  /**
   * Ajoute un filtre en fonction du type de la pizza pour ne conserver que les
   * pizzas du type défini.
   *
   * @param type le type de pizza à conserver
   * @throws IllegalArgumentException si le type est null
   */
  public void ajouterFiltre(TypePizza type) {
    if (type == null) {
      throw new IllegalArgumentException("Aucun filtre n'a été sélectionné");
    }
    this.typeFiltre = type;
  }
  
  /**
   * Ajoute un filtre par ingrédients. Les pizzas conservées doivent contenir
   * tous les ingrédients fournis. Les noms sont convertis en minuscules.
   *
   * @param ingredients la liste des ingrédients que doivent contenir les pizzas
   * @throws IllegalArgumentException si le tableau d'ingrédients est null
   */
  public void ajouterFiltre(String... ingredients) {
    if (ingredients == null) {
      throw new IllegalArgumentException("Aucun filtre n'a été sélectionné");
    }
    for (String ing : ingredients) {
      ingredientsFiltre.add(ing.toLowerCase());
    }
  }
  
  /**
   * Ajoute un filre de prix maximum pour ne conserver que les pizzas qui ont un
   * prix inférieur ou égal à ce prix. Le prix doit être supérieur à 0 sinon le
   * filtre n'est pas appliqué.
   *
   * @param prixMaximum le prix maximum des pizzas
   * @throws IllegalArgumentException si le prix est inférieur ou égal à 0
   */
  public void ajouterFiltre(double prixMaximum) {
    if (prixMaximum <= 0) {
      throw new IllegalArgumentException("Le prix maximum doit être positif");
    }
    this.prixMaximum = prixMaximum;
  }
  
  /**
   * Sélectionne les pizzas qui valident tous les filtres définis.
   *
   * @return l'ensemble filtré des pizzas (l'ensemble est vide si aucune pizza
   *         n'existe pour les filtres définis)
   */
  public Set<Pizza> selectionPizzaFiltres() {
    Set<Pizza> res = new HashSet<>();
    
    for (Pizza pizza : pizzas) {
      // Filtre par type
      if (typeFiltre != null && pizza.getType() != typeFiltre) {
        continue;
      }
      // Filtre par ingrédients
      if (!ingredientsFiltre.isEmpty()) {
        Set<String> nomsIngredientsPizza = new HashSet<>();
        for (Ingredient ingredient : pizza.getIngredients()) {
          nomsIngredientsPizza.add(ingredient.getNom().toLowerCase());
        }
        if (!nomsIngredientsPizza.containsAll(ingredientsFiltre)) {
          continue;
        }
      }
      // Filtre par prix
      if (prixMaximum != null && pizza.getPrix() > prixMaximum) {
        continue;
      }
      
      // Pizza validée par tous les filtres
      res.add(pizza);
    }
    
    return res;
  }
  
  /**
   * Supprime tous les filtres qui ont été définis.
   */
  public void supprimerFiltres() {
    this.typeFiltre = null;
    this.prixMaximum = null;
    this.ingredientsFiltre.clear();
  }
  
  /**
   * Met à jour la liste des pizzas à filtrer.
   */
  public void setPizzas(java.util.Set<Pizza> pizzas) {
    this.pizzas = pizzas;
  }
}
