package pizzas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Représente une pizza définie par un nom, un type, une liste d'ingrédients et
 * un prix de vente. Cette classe gère également le calcul du prix minimal en
 * fonction des ingrédients.
 *
 * @author Rayan Ladrait
 * @version 1.0
 */
public class Pizza {
  
  /**
   * Le nom de la pizza (doit être unique).
   */
  private String nom;
  
  /**
   * Le type de la pizza (Viande, Végétarienne ou Régionale).
   */
  private TypePizza type;
  
  /**
   * La liste des ingrédients composant la pizza.
   */
  private List<Ingredient> ingredients;
  
  /**
   * Le prix de vente fixé pour la pizza. Si null, le prix minimal calculé
   * s'applique par défaut.
   */
  private Double prixVente;
  
  /**
   * Le chemin ou l'URL de la photo de la pizza.
   */
  private String photo;
  
  /**
   * L'ensemble des évaluations laissées par les clients pour cette pizza.
   */
  private Set<Evaluation> evaluations;
  
  /**
   * Créer une liste de pizza "Catalogue".
   */
  private static final List<Pizza> catalogue = new ArrayList<>();
  
  /**
   * Construit une nouvelle pizza avec un nom et un type. La liste des
   * ingrédients est initialisée vide et aucun prix manuel n'est fixé.
   *
   * @param nom Le nom de la pizza
   * @param type Le type de la pizza
   */
  public Pizza(String nom, TypePizza type) {
    this.nom = nom;
    this.type = type;
    this.ingredients = new ArrayList<>();
    this.prixVente = null;
    this.photo = null;
    this.evaluations = new HashSet<>();
  }
  
  /**
   * Retourne le nom de la pizza.
   *
   * @return le nom
   */
  public String getNom() {
    return nom;
  }
  
  /**
   * Retourne le type de la pizza.
   *
   * @return le type de pizza
   */
  public TypePizza getType() {
    return type;
  }
  
  /**
   * Retourne la liste des ingrédients de la pizza.
   *
   * @return la liste des ingrédients
   */
  public List<Ingredient> getIngredients() {
    return ingredients;
  }
  
  /**
   * Ajoute un ingrédient à la composition de la pizza. Vérifie que l'ingrédient
   * n'est pas null et qu'il n'est pas déjà présent dans la pizza (basé sur
   * l'égalité des ingrédients).
   *
   * @param ingredient l'ingrédient à ajouter
   */
  public void ajouterIngredient(Ingredient ingredient) {
    if (ingredient == null) {
      return;
    }
    // Utilise la méthode equals() de Ingredient pour vérifier si on l'a déjà
    if (!this.ingredients.contains(ingredient)) {
      this.ingredients.add(ingredient);
    }
  }
  
  /**
   * Retire un ingrédient de la composition de la pizza. Vérifie que
   * l'ingrédient n'est pas null avant de tenter la suppression.
   *
   * @param ingredient l'ingrédient à retirer
   */
  public void retirerIngredient(Ingredient ingredient) {
    if (ingredient == null) {
      return;
    }
    this.ingredients.remove(ingredient);
  }
  
  /**
   * Retourne le chemin vers la photo de la pizza.
   *
   * @return le chemin de la photo ou null si aucune photo
   */
  public String getPhoto() {
    return photo;
  }
  
  /**
   * Modifie la photo associée à la pizza.
   *
   * @param photo le nouveau chemin de la photo
   */
  public void setPhoto(String photo) {
    this.photo = photo;
  }
  
  /**
   * Retourne le prix de vente actuel de la pizza. Si un prix manuel a été
   * défini, c'est celui-ci qui est retourné. Sinon, le prix minimal calculé est
   * retourné.
   *
   * @return le prix de la pizza
   */
  public double getPrix() {
    if (prixVente != null) {
      return prixVente;
    }
    return calculerPrixMinimal();
  }
  
  /**
   * Définit un prix de vente manuel pour la pizza.
   *
   * @param prix le nouveau prix de vente
   */
  public void setPrix(double prix) {
    this.prixVente = prix;
  }
  
  /**
   * Calcule le prix minimal de la pizza en fonction de ses ingrédients. La
   * formule est la somme des prix des ingrédients + 40%, arrondi à la dizaine
   * de centimes supérieure.
   *
   * @return le prix minimal calculé
   */
  public double calculerPrixMinimal() {
    double total = 0;
    for (Ingredient i : ingredients) {
      total += i.getPrix();
    }
    // Ajout de la marge de 40%
    double avecMarge = total * 1.4;
    
    // Arrondi à la dizaine de centimes supérieure
    // Math.ceil permet d'arrondir à l'entier supérieur.
    // En multipliant par 10 avant et divisant par 10 après, on arrondit à la
    // décimale.
    return Math.ceil(avecMarge * 10.0) / 10.0;
  }
  
  /**
   * Retourne l'ensemble des évaluations de la pizza. (Correspond à la
   * fonctionnalité attendue par getEvaluationPizza) * @return l'ensemble des
   * évaluations
   */
  public Set<Evaluation> getEvaluations() {
    return evaluations;
  }
  
  /**
   * Alias pour getEvaluations() si nécessaire pour respecter strictement la
   * demande.
   */
  public Set<Evaluation> getEvaluationPizza() {
    return getEvaluations();
  }
  
  /**
   * Calcule la note moyenne des évaluations de la pizza. * @return la moyenne
   * des notes, ou -1 si aucune évaluation n'existe.
   */
  public double getNoteMoyenne() {
    if (evaluations.isEmpty()) {
      return -1;
    }
    double somme = 0;
    for (Evaluation e : evaluations) {
      somme += e.getNote();
    }
    return somme / evaluations.size();
  }
  
  /** Ajoute une évaluation à la pizza de la part du client connecté. */
  public boolean ajouterEvaluation(int note, String commentaire)
      throws NonConnecteException, CommandeException {
    // Récupération du client connecté via la méthode statique de Compte
    Client client = Compte.getClientConnecte();
    if (client == null) {
      throw new NonConnecteException("Aucun client n'est connecté.");
    }
    
    // Vérification que le client a bien commandé cette pizza
    if (!client.peutEvaluerPizza(this)) {
      throw new CommandeException(
          "Vous ne pouvez pas évaluer une pizza que vous n'avez pas commandée et reçue.");
    }
    
    // Vérification si le client a déjà évalué cette pizza
    for (Evaluation e : evaluations) {
      if (e.getAuteur().equals(client)) {
        return false; // Le client a déjà évalué cette pizza
      }
    }
    
    try {
      // Création de l'évaluation (gère le cas commentaire null via le
      // constructeur approprié)
      Evaluation eval;
      if (commentaire == null) {
        eval = new Evaluation(note, client);
      } else {
        eval = new Evaluation(note, commentaire, client);
      }
      
      return this.evaluations.add(eval);
      
    } catch (IllegalArgumentException e) {
      // Note invalide ou autre erreur d'argument
      return false;
    }
  }
  
  /**
   * Ajoute une pizza au catalogue global de l'application. La pizza n'est
   * ajoutée que si elle n'est pas nulle et qu'elle n'est pas déjà présente dans
   * la liste.
   *
   * @param p La pizza à ajouter au catalogue
   */
  public static void ajouterPizzaCatalogue(Pizza p) {
    if (p != null && !catalogue.contains(p)) {
      catalogue.add(p);
    }
  }
  
  /**
   * Recherche et récupère une pizza dans le catalogue à partir de son nom. La
   * recherche n'est pas sensible à la casse (majuscules/minuscules).
   *
   * @param nom Le nom de la pizza recherchée
   * @return La pizza correspondante si elle est trouvée, sinon null
   */
  public static Pizza getPizzaParNom(String nom) {
    for (Pizza p : catalogue) {
      if (p.getNom().equalsIgnoreCase(nom)) {
        return p;
      }
    }
    return null;
  }
  
  public static List<Pizza> getCatalogue() {
    return new ArrayList<>(catalogue);
  }
  
  /**
   * Modifie le type de la pizza.
   *
   * @param type le nouveau type
   */
  public void setType(TypePizza type) {
    this.type = type;
  }
  
  
  /**
   * Vérifie l'égalité entre deux pizzas. L'égalité est basée uniquement sur le
   * nom de la pizza.
   *
   * @param o l'objet à comparer
   * @return true si les deux pizzas ont le même nom, false sinon
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pizza pizza = (Pizza) o;
    return Objects.equals(nom, pizza.nom);
  }
  
  /**
   * Calcule le code de hachage de la pizza. Basé sur le nom pour être cohérent
   * avec equals.
   *
   * @return le hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(nom);
  }
  
  /**
   * Retourne une représentation textuelle de la pizza. Format : "Nom (Type)"
   *
   * @return la chaîne de caractères représentant la pizza
   */
  @Override
  public String toString() {
    return nom + " (" + type + ")";
  }
}
