package pizzas;

/**
 * Définit le type d'une pizza parmi viande, végétarienne ou régionale.
 *
 * @author Eric Cariou
 */
public enum TypePizza {
  
  /**
   * Pizzas classiques à la viande : reine, pepperoni, bolognaise...
   */
  Viande,
  
  /**
   * Pizzas végétariennes : quatre fromages, vegan aux légumes...
   */
  Vegetarienne,
  
  /**
   * Pizzas composées d'ingrédients régionaux : bretonne aux noix de
   * Saint-Jacques, landaise au canard, savoyarde au reblochon...
   */
  Regionale,
}
