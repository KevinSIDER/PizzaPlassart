package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description d'un client de la pizzeria : un compte et la liste de ses
 * commandes.
 *
 * @author Yaouanc kevin
 */
public final class Client implements Serializable {
  
  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * Le compte associé à ce client.
   */
  private final Compte compte;
  
  /**
   * La liste de toutes les commandes passées par ce client.
   */
  private final List<Commande> commandes;
  
  private final GestCommande gestCommande;
  
  /**
   * Compteur statique pour générer des IDs uniques de commande.
   */
  private static int compteurCommande = 1;
  
  /**
   * Crée un client à partir de son compte.
   *
   * @param compte le compte du client
   * @throws IllegalArgumentException si le compte est {@code null}
   */
  public Client(Compte compte) {
    if (compte == null) {
      throw new IllegalArgumentException("le compte null");
    }
    this.compte = compte;
    this.commandes = new ArrayList<>();
    this.gestCommande = new GestCommande();
  }
  
  /**
   * Renvoie le compte du client.
   *
   * @return le compte
   */
  public Compte getCompte() {
    return compte;
  }
  
  // ✅ MODIF AJOUTÉE (rien d'autre n'a changé)
  public InformationPersonnelle getInfoPersonnelle() {
    return compte.getInfoPersonnelle();
  }
  
  /**
   * Renvoie la liste de toutes les commandes du client.
   *
   * @param idCmd l'identifiant unique de la commande recherchée
   * @return une vue non modifiable de la liste des commandes
   */
  public Commande getCommandes(int idCmd) {
    for (Commande c : commandes) {
      if (c.getIdCommande() == idCmd) {
        return c;
      }
    }
    return null;
  }
  
  /**
   * Ajoute une commande à la liste des commandes du client.
   *
   * @param commande la commande à ajouter
   */
  public void ajouterCommande(Commande commande) {
    if (commande != null) {
      commandes.add(commande);
    }
  }
  
  /**
   * Retire une commande à la liste des commandes du client.
   *
   * @param commande la commande à retirer
   */
  public void retirerCommande(Commande commande) {
    commandes.remove(commande);
  }
  
  /**
   * Permet au client de débuter la commande.
   *
   * @return cmd
   * @throws NonConnecteException problème de connexion
   */
  public Commande debuterCommande() throws NonConnecteException {
    return this.nouvelleCommande();
  }
  
  
  
  /**
   * Permet au client de valider la commande.
   *
   * @param cmd la commande à valider (ne doit pas être {@code null})
   * @throws NonConnecteException si le client n'est pas connecté au système
   * @throws CommandeException si la commande ne peut pas être validée
   * @throws IllegalArgumentException si la commande passée en paramètre est
   *         {@code null}
   */
  public void validerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    if (cmd == null) {
      throw new IllegalArgumentException("Commande invalide.");
    }
    gestCommande.validerCommande(cmd);
  }
  
  /**
   * Renvoie la liste des commandes déjà traitées de ce client.
   *
   * @return la liste des commandes traitées
   */
  public List<Commande> getCommandesPassees() {
    return GestCommande.commandesDejaTraitees(commandes);
  }
  
  /**
   * Renvoie la liste des commandes encore non traitées de ce client.
   *
   * @return la liste des commandes non traitées
   */
  public List<Commande> getCommandesEnCours() {
    return GestCommande.commandesNonTraitees(commandes);
  }
  
  public GestCommande getGestionnaireCommande() {
    return gestCommande;
  }
  
  /**
   * Indique si ce client peut évaluer une pizza donnée. Un client peut évaluer
   * une pizza s'il l'a déjà commandée dans au moins une commande qui a été
   * traitée.
   *
   * @param pizza la pizza à évaluer
   * @return {@code true} si le client peut évaluer cette pizza
   */
  public boolean peutEvaluerPizza(Pizza pizza) {
    if (pizza == null) {
      return false;
    }
    for (Commande commande : getCommandesPassees()) {
      if (commande.getPizzas().contains(pizza)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Crée une nouvelle commande pour ce client.
   *
   * @return la nouvelle commande créée
   */
  public Commande nouvelleCommande() {
    Commande cmd = new Commande(compteurCommande++, this);
    this.ajouterCommande(cmd);
    return cmd;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(compte);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Client other = (Client) obj;
    return Objects.equals(compte, other.compte);
  }
  
  @Override
  public String toString() {
    return "Client(" + compte + ")";
  }
}
