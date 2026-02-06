package pizzas;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère les commandes réalisées par un client connecté. Elle vérifie
 * systématiquement que le client est connecté et que l'opération demandée est
 * autorisée.
 *
 * @author Léo Montay
 * @version 1.0
 */
public class GestCommande {
  
  /**
   * Représente le client actuellement connecté.
   */
  private static Client clientConnecte;
  
  
  /**
   * Liste des commandes associées au client ou au système.
   */
  public static List<Commande> commandes;
  
  /**
   * Compteur utilisé pour générer des identifiants uniques pour les commandes
   * ou autres entités. Sa valeur initiale est 1.
   */
  @SuppressWarnings("unused")
  private static int compteurId = 1;
  
  /**
   * Construit un gestionnaire de commandes.
   */
  public GestCommande() {
    commandes = new ArrayList<>();
  }
  
  /**
   * Connecte un client au système.
   *
   * @param c client à connecter
   */
  public void connecter(Client c) {
    GestCommande.clientConnecte = c;
  }
  
  /**
   * Déconnecte le client actuellement connecté.
   */
  public void deconnecter() {
    GestCommande.clientConnecte = null;
  }
  
  /**
   * Vérifie qu'un client est bien connecté.
   *
   * @throws NonConnecteException si aucun client n'est connecté
   */
  private static void checkClientConnecte() throws NonConnecteException {
    if (clientConnecte == null) {
      throw new NonConnecteException("Aucun client connecté.");
    }
  }
  
  /**
   * Crée une nouvelle commande pour le client actuellement connecté.
   *
   * @return la nouvelle commande
   * @throws NonConnecteException si aucun client n'est connecté
   */
  public static Commande debuterCommande() throws NonConnecteException {
    Client client = Compte.getClientConnecte();
    if (client == null) {
      throw new NonConnecteException("Aucun client connecté");
    }
    
    Commande nouvelleCommande = client.nouvelleCommande();
    return nouvelleCommande;
  }
  
  /**
   * Ajoute une pizza à une commande existante.
   *
   * @param pizza la pizza à ajouter
   * @param nombre le nombre de pizzas
   * @param cmd la commande concernée
   * @throws NonConnecteException si aucun client n'est connecté
   * @throws CommandeException si la commande ne peut pas être modifiée
   */
  public void ajouterPizza(Pizza pizza, int nombre, Commande cmd)
      throws NonConnecteException, CommandeException {
    
    checkClientConnecte();
    
    if (cmd == null) {
      throw new CommandeException("Commande inexistante.");
    }
    
    if (cmd.getClient() != clientConnecte) {
      throw new CommandeException(
          "Cette commande appartient à un autre client.");
    }
    
    if (cmd.getEtat() != EtatCommande.CREE) {
      throw new CommandeException("La commande n'est plus modifiable.");
    }
    
    if (nombre <= 0) {
      throw new CommandeException("Le nombre de pizzas doit être positif.");
    }
    
    for (int i = 0; i < nombre; i++) {
      cmd.ajouterPizza(pizza);
    }
  }
  
  /**
   * Valide une commande en cours.
   *
   * @param cmd commande à valider
   * @throws NonConnecteException si aucun client n'est connecté
   * @throws CommandeException si la commande ne peut pas être validée
   */
  public void validerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    
    checkClientConnecte();
    
    if (cmd.getClient() != clientConnecte) {
      throw new CommandeException(
          "Cette commande n'appartient pas au client connecté.");
    }
    
    cmd.valider();
  }
  
  /**
   * Annule une commande en cours.
   *
   * @param cmd commande à annuler
   * @throws NonConnecteException si aucun client n'est connecté
   * @throws CommandeException si l'annulation n'est pas possible
   */
  public void annulerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    
    checkClientConnecte();
    if (cmd == null) {
      throw new CommandeException("Commande null.");
    }
    
    if (cmd.getClient() != clientConnecte) {
      throw new CommandeException(
          "Cette commande n'appartient pas au client connecté.");
    }
    
    if (cmd.getEtat() == EtatCommande.TRAITEE) {
      throw new CommandeException(
          "Impossible d'annuler une commande déjà traitée.");
    }
    clientConnecte.retirerCommande(cmd);
    commandes.remove(cmd);
  }
  
  /**
   * Retourne la liste des commandes déjà traitées.
   *
   * @param commandes Liste de toutes les commandes
   * @return Liste des commandes traitées
   */
  public static List<Commande> commandesDejaTraitees(List<Commande> commandes) {
    List<Commande> resultat = new ArrayList<>();
    for (Commande c : commandes) {
      if (c.getEtat() == EtatCommande.VALIDEE
          || c.getEtat() == EtatCommande.TRAITEE) {
        resultat.add(c);
      }
    }
    return resultat;
  }
  
  /**
   * Retourne la liste des commandes non traitées. Note : Cette méthode devrait
   * être statique et accéder à une liste globale de commandes.
   *
   * @param commandes Liste de toutes les commandes
   * @return Liste des commandes non traitées
   */
  public static List<Commande> commandesNonTraitees(List<Commande> commandes) {
    List<Commande> resultat = new ArrayList<>();
    for (Commande c : commandes) {
      if (c.getEtat() == EtatCommande.CREE) { // Seulement CREE
        resultat.add(c);
      }
    }
    return resultat;
  }
  
  /**
   * Retourne la liste des commandes traitées pour un client donné. Note : Cette
   * méthode devrait être statique et accéder à une liste globale de commandes.
   *
   * @param commandes Liste de toutes les commandes
   * @param client Informations personnelles du client recherché
   * @return Liste des commandes traitées du client
   */
  public static List<Commande> commandesTraiteesClient(List<Commande> commandes,
      InformationPersonnelle client) {
    List<Commande> resultat = new ArrayList<>();
    for (Commande c : commandes) {
      if (c.getEtat() == EtatCommande.TRAITEE
          && c.getClient().getInfoPersonnelle().equals(client)) {
        resultat.add(c);
      }
    }
    return resultat;
  }
}
