package pizzas;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une commande réalisée par un client. Une commande contient une
 * liste de pizzas, un client associé, un état (créée, validée, traitée) et un
 * prix total calculé en fonction des pizzas ajoutées.
 *
 * @author leo.montay
 * @version 1.0
 */
public class Commande {
  
  /**
   * Identifiant unique de la commande.
   */
  private int idCommande;
  
  /**
   * Liste des pizzas composant la commande.
   */
  private List<Pizza> pizzas;
  
  /**
   * Client ayant passé la commande.
   */
  private Client client;
  
  /**
   * État actuel de la commande.
   */
  private EtatCommande etat;
  
  /**
   * Prix total de la commande.
   */
  private double prixTotal;
  
  /**
   * Construit une commande avec un identifiant et un client. L'état de la
   * commande est initialisé à {@code EtatCommande.Cree}.
   *
   * @param idCommande identifiant unique de la commande
   * @param client client ayant passé la commande
   */
  public Commande(int idCommande, Client client) {
    this.idCommande = idCommande;
    this.client = client;
    this.pizzas = new ArrayList<>();
    this.etat = EtatCommande.CREE;
    this.prixTotal = 0.0;
  }
  
  /**
   * Renvoie l'identifiant de la commande.
   *
   * @return identifiant unique
   */
  public int getIdCommande() {
    return idCommande;
  }
  
  /**
   * Renvoie le client ayant passé la commande.
   *
   * @return client associé
   */
  public Client getClient() {
    return client;
  }
  
  /**
   * Renvoie l'état actuel de la commande.
   *
   * @return état de la commande
   */
  public EtatCommande getEtat() {
    return etat;
  }
  
  /**
   * Modifie l'état de la commande.
   *
   * @param etat nouvel état
   */
  public void setEtat(EtatCommande etat) {
    this.etat = etat;
  }
  
  /**
   * Renvoie la liste des pizzas contenues dans la commande.
   *
   * @return liste des pizzas
   */
  public List<Pizza> getPizzas() {
    return pizzas;
  }
  
  /**
   * Renvoie le prix total de la commande.
   *
   * @return prix total
   */
  public double getPrixTotal() {
    return prixTotal;
  }
  
  /**
   * Valide la commande.
   *
   * @throws CommandeException si la commande ne peut pas être validée
   */
  public void valider() throws CommandeException {
    if (etat != EtatCommande.CREE) {
      throw new CommandeException("La commande ne peut pas être validée.");
    }
    etat = EtatCommande.VALIDEE;
  }
  
  
  /**
   * Ajoute une pizza à la commande et met à jour le prix total.
   *
   * @param pizza pizza à ajouter
   */
  public void ajouterPizza(Pizza pizza) {
    if (pizza != null) {
      pizzas.add(pizza);
      prixTotal += pizza.getPrix(); // ← Vérifiez que cette ligne existe
    }
  }
  
  /**
   * Retire une pizza de la commande si elle est présente, et met à jour le prix
   * total.
   *
   * @param pizza pizza à retirer
   */
  public void retirerPizza(Pizza pizza) {
    if (pizzas.remove(pizza)) {
      prixTotal -= pizza.getPrix();
    }
  }
  
  /**
   * Recalcule intégralement le prix total des pizzas de la commande. Utile en
   * cas de modifications multiples.
   */
  public void calculerPrixTotal() {
    prixTotal = 0.0;
    for (Pizza p : pizzas) {
      prixTotal += p.getPrix();
    }
  }
  
  @Override
  public String toString() {
    return "Commande #" + idCommande + " - " + client.getCompte().getEmail()
        + " - " + String.format("%.2f", prixTotal) + "€" + " - " + etat;
  }
}
