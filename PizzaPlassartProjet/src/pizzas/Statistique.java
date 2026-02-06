package pizzas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Classe utilitaire pour le calcul des statistiques de la pizzeria.
 *
 * @author Rayan Ladrait
 * @version 1.0
 */

public class Statistique {
  
  /**
   * Calcule le bénéfice unitaire d'une pizza (Prix de vente - Prix minimal).
   */
  public static double calculerBeneficePizza(Pizza pizza) {
    if (pizza == null) {
      return 0.0;
    }
    return pizza.getPrix() - pizza.calculerPrixMinimal();
  }
  
  /**
   * Calcule le bénéfice total d'une commande.
   */
  public static double calculerBeneficeCommande(Commande commande) {
    if (commande == null) {
      return 0.0;
    }
    double total = 0.0;
    for (Pizza p : commande.getPizzas()) {
      total += calculerBeneficePizza(p);
    }
    return total;
  }
  
  /**
   * Calcule le bénéfice total sur une liste de commandes.
   */
  public static double calculerBeneficeTotal(List<Commande> commandes) {
    if (commandes == null) {
      return 0.0;
    }
    double total = 0.0;
    for (Commande c : commandes) {
      total += calculerBeneficeCommande(c);
    }
    return total;
  }
  
  /**
   * Calcule le bénéfice généré par chaque pizza (Total des bénéfices apportés
   * par cette pizza).
   */
  public static Map<Pizza, Double> beneficeParPizza(List<Commande> commandes,
      Set<Pizza> catalogue) {
    Map<Pizza, Double> result = new HashMap<>();
    // Initialisation à 0 pour toutes les pizzas du catalogue
    for (Pizza p : catalogue) {
      result.put(p, 0.0);
    }
    
    for (Commande c : commandes) {
      for (Pizza p : c.getPizzas()) {
        double benef = calculerBeneficePizza(p);
        // On ajoute au total existant
        result.put(p, result.getOrDefault(p, 0.0) + benef);
      }
    }
    return result;
  }
  
  /**
   * Compte le nombre de pizzas commandées par chaque client.
   */
  public static Map<InformationPersonnelle, Integer> nombrePizzasParClient(
      List<Commande> commandes) {
    Map<InformationPersonnelle, Integer> result = new HashMap<>();
    for (Commande c : commandes) {
      InformationPersonnelle clientInfo = c.getClient().getInfoPersonnelle();
      int nbPizzas = c.getPizzas().size();
      result.put(clientInfo, result.getOrDefault(clientInfo, 0) + nbPizzas);
    }
    return result;
  }
  
  /**
   * Calcule le bénéfice total rapporté par chaque client.
   */
  public static Map<InformationPersonnelle, Double> beneficeParClient(
      List<Commande> commandes) {
    Map<InformationPersonnelle, Double> result = new HashMap<>();
    for (Commande c : commandes) {
      InformationPersonnelle clientInfo = c.getClient().getInfoPersonnelle();
      double benef = calculerBeneficeCommande(c);
      result.put(clientInfo, result.getOrDefault(clientInfo, 0.0) + benef);
    }
    return result;
  }
  
  /**
   * Compte combien de fois une pizza spécifique a été commandée.
   */
  public static int nombrePizzasCommandees(List<Commande> commandes,
      Pizza pizza) {
    int count = 0;
    for (Commande c : commandes) {
      for (Pizza p : c.getPizzas()) {
        if (p.equals(pizza)) {
          count++;
        }
      }
    }
    return count;
  }
  
  /**
   * Trie les pizzas de la plus commandée à la moins commandée.
   */
  public static List<Pizza> classementPizzas(List<Commande> commandes,
      Set<Pizza> catalogue) {
    Map<Pizza, Integer> counts = new HashMap<>();
    // Initialisation
    for (Pizza p : catalogue) {
      counts.put(p, 0);
    }
    
    // Comptage
    for (Commande c : commandes) {
      for (Pizza p : c.getPizzas()) {
        counts.put(p, counts.getOrDefault(p, 0) + 1);
      }
    }
    
    // Tri décroissant sur les valeurs
    return counts.entrySet().stream()
        .sorted(Map.Entry.<Pizza, Integer>comparingByValue().reversed())
        .map(Map.Entry::getKey).collect(Collectors.toList());
  }
}
