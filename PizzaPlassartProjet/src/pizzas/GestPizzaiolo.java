package pizzas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe de base (Façade) pour la gestion des interactions du pizzaiolo avec la
 * pizzeria. Elle implémente l'interface InterPizzaiolo et fournit les
 * fonctionnalités de gestion des pizzas, ingrédients et commandes.
 *
 * @author Léo Montay
 * @version 1.0
 * @see InterPizzaiolo
 * @see Pizzaiolo
 */
public class GestPizzaiolo implements InterPizzaiolo {
  private List<Pizza> pizzas;
  private List<Ingredient> ingredients;
  private Map<TypePizza, List<Ingredient>> ingredientsInterdits;
  private List<Commande> commandes;
  @SuppressWarnings("unused")
  private Pizzaiolo pizzaiolo;
  
  /**
   * Initialise le gestionnaire pour un pizzaiolo donné et instancie les
   * structures de données vides (pizzas, ingrédients, commandes).
   *
   * @param pizzaiolo le pizzaiolo associé à ce gestionnaire
   */
  public GestPizzaiolo(Pizzaiolo pizzaiolo) {
    this.pizzaiolo = pizzaiolo;
    this.pizzas = new ArrayList<>();
    this.ingredients = new ArrayList<>();
    this.ingredientsInterdits = new HashMap<>();
    this.commandes = new ArrayList<>();
  }
  
  /**
   * Retourne la liste de tous les ingrédients.
   */
  public List<Ingredient> getIngredients() {
    return ingredients;
  }
  
  /**
   * Recherche un ingrédient par son nom.
   */
  private Ingredient rechercherIngredient(String nom) {
    if (nom == null || nom.trim().isEmpty()) {
      return null;
    }
    for (Ingredient ing : ingredients) {
      if (ing.getNom().equals(nom)) {
        return ing;
      }
    }
    return null;
  }
  
  /**
   * Vérifie si un ingrédient donné est interdit pour un type de pizza
   * spécifique.
   *
   * @param type le type de pizza (ex: Végétarienne)
   * @param ing l'ingrédient à vérifier
   * @return true si l'ingrédient est interdit pour ce type, false sinon
   */
  public boolean estIngredientInterdit(TypePizza type, Ingredient ing) {
    List<Ingredient> interdits = ingredientsInterdits.get(type);
    if (interdits != null) {
      return interdits.contains(ing);
    }
    return false;
  }
  
  /**
   * Récupère toutes les commandes de tous les clients.
   */
  private List<Commande> recupererToutesLesCommandes() {
    List<Commande> toutes = new ArrayList<>();
    
    // Utiliser la méthode existante getTousLesClients()
    for (Client client : Compte.getTousLesClients()) {
      // Ajouter toutes les commandes du client (en cours + passées)
      toutes.addAll(client.getCommandesEnCours());
      toutes.addAll(client.getCommandesPassees());
    }
    
    return toutes;
  }
  
  /**
   * Récupère la liste globale de toutes les commandes qui sont dans l'état
   * TRAITEE.
   * 
   * <p>Cette méthode agrège les historiques de commandes de tous les clients
   * enregistrés pour ne retourner que celles qui ont été finalisées.
   * </p>
   *
   * @return une liste contenant toutes les commandes traitées du système
   */
  public List<Commande> getCommandesTraitees() {
    List<Commande> toutes = recupererToutesLesCommandes();
    List<Commande> traitees = new ArrayList<>();
    for (Commande c : toutes) {
      if (c.getEtat() == EtatCommande.TRAITEE) {
        traitees.add(c);
      }
    }
    return traitees;
  }
  
  /**
   * Retourne une liste de commandes filtrée selon un état spécifique.
   *
   * @param etat l'état des commandes recherchées (ex: CREE, VALIDEE, TRAITEE)
   * @return la liste des commandes correspondant à l'état donné
   */
  public List<Commande> getCommandesParEtat(EtatCommande etat) {
    List<Commande> resultat = new ArrayList<>();
    for (Commande c : commandes) {
      if (c.getEtat() == etat) {
        resultat.add(c);
      }
    }
    return resultat;
  }
  
  @Override
  public int creerIngredient(String nom, double prix) {
    if (nom == null || nom.trim().isEmpty()) {
      return -1;
    }
    if (rechercherIngredient(nom) != null) {
      return -2;
    }
    if (prix <= 0) {
      return -3;
    }
    
    Ingredient nouveauIngredient = new Ingredient(nom, prix);
    ingredients.add(nouveauIngredient);
    return 0;
  }
  
  @Override
  public int changerPrixIngredient(String nom, double prix) {
    if (nom == null || nom.trim().isEmpty()) {
      return -1;
    }
    if (prix <= 0) {
      return -2;
    }
    
    Ingredient ingredient = rechercherIngredient(nom);
    if (ingredient == null) {
      return -3;
    }
    
    ingredient.setPrix(prix);
    return 0;
  }
  
  @Override
  public boolean interdireIngredient(String nomIngredient, TypePizza type) {
    Ingredient ingredient = rechercherIngredient(nomIngredient);
    if (ingredient == null) {
      return false;
    }
    
    List<Ingredient> interdits = ingredientsInterdits.get(type);
    if (interdits == null) {
      interdits = new ArrayList<>();
      ingredientsInterdits.put(type, interdits);
    }
    
    // Logique d'interrupteur (Toggle)
    if (interdits.contains(ingredient)) {
      interdits.remove(ingredient);
    } else {
      interdits.add(ingredient);
    }
    return true;
  }
  
  /**
   * Recherche une pizza par son nom.
   */
  private Pizza rechercherPizza(String nom) {
    if (nom == null || nom.trim().isEmpty()) {
      return null;
    }
    for (Pizza p : pizzas) {
      if (p.getNom().equals(nom)) {
        return p;
      }
    }
    return null;
  }
  
  /**
   * Retrouve une commande à partir de son texte affiché dans la ListView.
   */
  public Commande getCommandeByString(String txt) {
    if (txt == null) {
      return null;
    }
    for (Commande c : recupererToutesLesCommandes()) {
      if (c.toString().equals(txt)) {
        return c;
      }
    }
    return null;
  }
  
  /**
   * Retourne tous les clients ayant passé au moins une commande.
   */
  public Set<InformationPersonnelle> getClientsAyantCommande() {
    Set<InformationPersonnelle> cl = new HashSet<>();
    for (Commande c : commandes) {
      cl.add(c.getClient().getInfoPersonnelle());
    }
    return cl;
  }
  
  /**
   * Retrouve un client via son email.
   */
  public InformationPersonnelle getClientByEmail(String email) {
    for (Client client : Compte.getTousLesClients()) {
      if (client.getCompte().getEmail().equals(Compte.normaliserEmail(email))) {
        return client.getInfoPersonnelle();
      }
    }
    return null;
  }
  
  /**
   * Vérifie si une pizza est valide (non null et existe dans la liste).
   */
  private boolean estPizzaValide(Pizza pizza) {
    return pizza != null && pizzas.contains(pizza);
  }
  
  @Override
  public Pizza creerPizza(String nom, TypePizza type) {
    if (nom == null || nom.trim().isEmpty()) {
      return null;
    }
    if (rechercherPizza(nom) != null) {
      return null;
    }
    
    Pizza nouvellePizza = new Pizza(nom, type);
    pizzas.add(nouvellePizza);
    Pizza.ajouterPizzaCatalogue(nouvellePizza);
    return nouvellePizza;
  }
  
  @Override
  public int ajouterIngredientPizza(Pizza pizza, String nomIngredient) {
    if (!estPizzaValide(pizza)) {
      return -1;
    }
    if (nomIngredient == null || nomIngredient.trim().isEmpty()) {
      return -2;
    }
    
    Ingredient ingredient = rechercherIngredient(nomIngredient);
    if (ingredient == null) {
      return -2;
    }
    
    if (estIngredientInterdit(pizza.getType(), ingredient)) {
      return -3;
    }
    
    pizza.ajouterIngredient(ingredient);
    return 0;
  }
  
  @Override
  public int retirerIngredientPizza(Pizza pizza, String nomIngredient) {
    if (!estPizzaValide(pizza)) {
      return -1;
    }
    if (nomIngredient == null || nomIngredient.trim().isEmpty()) {
      return -2;
    }
    
    Ingredient ingredient = rechercherIngredient(nomIngredient);
    if (ingredient == null) {
      return -2;
    }
    
    if (!pizza.getIngredients().contains(ingredient)) {
      return -3;
    }
    
    pizza.retirerIngredient(ingredient);
    return 0;
  }
  
  @Override
  public Set<String> verifierIngredientsPizza(Pizza pizza) {
    if (!estPizzaValide(pizza)) {
      return null;
    }
    
    Set<String> ingredientsInterditsSet = new HashSet<>();
    for (Ingredient ing : pizza.getIngredients()) {
      if (estIngredientInterdit(pizza.getType(), ing)) {
        ingredientsInterditsSet.add(ing.getNom());
      }
    }
    return ingredientsInterditsSet;
  }
  
  @Override
  public boolean ajouterPhoto(Pizza pizza, String file) throws IOException {
    if (!estPizzaValide(pizza)) {
      return false;
    }
    if (file == null || file.trim().isEmpty()) {
      return false;
    }
    
    String extension = file.toLowerCase();
    if (!extension.endsWith(".jpg") && !extension.endsWith(".jpeg")
        && !extension.endsWith(".png") && !extension.endsWith(".gif")) {
      return false;
    }
    
    pizza.setPhoto(file);
    return true;
  }
  
  @Override
  public double getPrixPizza(Pizza pizza) {
    if (!estPizzaValide(pizza)) {
      return -1;
    }
    return pizza.getPrix();
  }
  
  @Override
  public boolean setPrixPizza(Pizza pizza, double prix) {
    if (!estPizzaValide(pizza)) {
      return false;
    }
    double prixMinimal = pizza.calculerPrixMinimal();
    if (prix < prixMinimal) {
      return false;
    }
    pizza.setPrix(prix);
    return true;
  }
  
  @Override
  public double calculerPrixMinimalPizza(Pizza pizza) {
    if (!estPizzaValide(pizza)) {
      return -1;
    }
    return pizza.calculerPrixMinimal();
  }
  
  @Override
  public Set<Pizza> getPizzas() {
    return new java.util.HashSet<>(pizzas);
  }
  
  @Override
  public Set<InformationPersonnelle> ensembleClients() {
    // Les clients sont ceux qui ont des commandes TRAITÉES
    Set<InformationPersonnelle> clients = new HashSet<>();
    for (Commande c : getCommandesTraitees()) {
      clients.add(c.getClient().getInfoPersonnelle());
    }
    return clients;
  }
  
  @Override
  public List<Commande> commandesDejaTraitees() {
    return getCommandesTraitees();
  }
  
  @Override
  public List<Commande> commandeNonTraitees() {
    List<Commande> toutes = recupererToutesLesCommandes();
    List<Commande> nonTraitees = new ArrayList<>();
    for (Commande c : toutes) {
      if (c.getEtat() == EtatCommande.VALIDEE) {
        nonTraitees.add(c);
        c.setEtat(EtatCommande.TRAITEE);
      }
      if (c.getEtat() == EtatCommande.VALIDEE) {
        nonTraitees.add(c);
        // Les marquer comme traitées
        c.setEtat(EtatCommande.TRAITEE);
      }
    }
    return nonTraitees;
  }
  
  @Override
  public List<Commande> commandesTraiteesClient(InformationPersonnelle client) {
    List<Commande> toutes = recupererToutesLesCommandes();
    List<Commande> resultat = new ArrayList<>();
    for (Commande c : toutes) {
      if (c.getEtat() == EtatCommande.TRAITEE
          && c.getClient().getInfoPersonnelle().equals(client)) {
        resultat.add(c);
      }
    }
    return resultat;
  }
  
  @Override
  public Map<Pizza, Double> beneficeParPizza() {
    return Statistique.beneficeParPizza(getCommandesTraitees(), getPizzas());
  }
  
  @Override
  public double beneficeCommandes(Commande commande) {
    return Statistique.calculerBeneficeCommande(commande);
  }
  
  @Override
  public double beneficeToutesCommandes() {
    return Statistique.calculerBeneficeTotal(getCommandesTraitees());
  }
  
  @Override
  public Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient() {
    return Statistique.nombrePizzasParClient(getCommandesTraitees());
  }
  
  @Override
  public Map<InformationPersonnelle, Double> beneficeParClient() {
    return Statistique.beneficeParClient(getCommandesTraitees());
  }
  
  @Override
  public int nombrePizzasCommandees(Pizza pizza) {
    if (!estPizzaValide(pizza)) {
      return -1;
    }
    return Statistique.nombrePizzasCommandees(getCommandesTraitees(), pizza);
  }
  
  @Override
  public List<Pizza> classementPizzasParNombreCommandes() {
    return Statistique.classementPizzas(getCommandesTraitees(), getPizzas());
  }
}
