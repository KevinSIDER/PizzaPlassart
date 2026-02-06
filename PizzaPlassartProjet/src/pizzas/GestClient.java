package pizzas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe de base (Façade) pour la gestion des interactions client avec la
 * pizzeria. Elle implémente l'interface InterClient et ses fonctions qui
 * doivent absolument être redéfinies dans une sous-classe concrète.
 *
 * @author Kevin SIDER
 */
public class GestClient implements InterClient {
  List<Client> clients;
  List<Pizza> pizzas;
  Filtre filtre;
  
  /**
   * Instance de gestionnaire de commande pour effectuer les opérations
   * non-statiques.
   */
  private GestCommande gestCommande;
  
  /**
   * Initialise la liste des clients, des pizzas, le client connecté et le
   * filtre.
   */
  public GestClient() {
    this.clients = new ArrayList<>();
    this.clients = new ArrayList<>();
    this.pizzas = new ArrayList<>();
    this.filtre = new Filtre(new HashSet<>(this.pizzas));
    this.gestCommande = new GestCommande();
  }
  
  
  /**
   * Gestion du compte.
   */
  
  @Override
  public int inscription(String email, String mdp,
      InformationPersonnelle info) {
    int resultat = Compte.inscription(email, mdp, info);
    return resultat;
  }
  
  @Override
  public boolean connexion(String email, String mdp) {
    boolean succes = Compte.connexion(email, mdp);
    if (succes) {
      Client client = Compte.getClientConnecte();
      this.gestCommande.connecter(client);
    }
    return succes;
  }
  
  @Override
  public void deconnexion() throws NonConnecteException {
    boolean aeteDeconnecte = Compte.deconnexion();
    if (!aeteDeconnecte) {
      throw new NonConnecteException("Aucun client n'est connecté.");
    }
    this.gestCommande.deconnecter();
  }
  
  /**
   * Gestion des commandes.
   */
  
  @Override
  public Commande debuterCommande() throws NonConnecteException {
    return GestCommande.debuterCommande();
  }
  
  @Override
  public void ajouterPizza(Pizza pizza, int nombre, Commande cmd)
      throws NonConnecteException, CommandeException {
    this.gestCommande.ajouterPizza(pizza, nombre, cmd);
  }
  
  @Override
  public void validerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    this.gestCommande.validerCommande(cmd);
  }
  
  @Override
  public void annulerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    this.gestCommande.annulerCommande(cmd);
  }
  
  @Override
  public List<Commande> getCommandesEncours() throws NonConnecteException {
    Client client = Compte.getClientConnecte();
    if (client == null) {
      throw new NonConnecteException("Aucun client n'est connecté.");
    }
    return client.getCommandesEnCours();
  }
  
  @Override
  public List<Commande> getCommandePassees() throws NonConnecteException {
    Client client = Compte.getClientConnecte();
    if (client == null) {
      throw new NonConnecteException("Aucun client n'est connecté.");
    }
    return client.getCommandesPassees();
  }
  
  /**
   * Gestion des pizzas et des filtres.
   */
  
  @Override
  public Set<Pizza> getPizzas() {
    return new HashSet<>(Pizza.getCatalogue());
  }
  
  @Override
  public void ajouterFiltre(TypePizza type) {
    this.filtre.ajouterFiltre(type);
  }
  
  @Override
  public void ajouterFiltre(String... ingredients) {
    this.filtre.ajouterFiltre(ingredients);
  }
  
  @Override
  public void ajouterFiltre(double prixMaximum) {
    this.filtre.ajouterFiltre(prixMaximum);
  }
  
  @Override
  public Set<Pizza> selectionPizzaFiltres() {
    Set<Pizza> catalogueAjour = new HashSet<>(Pizza.getCatalogue());
    this.filtre.setPizzas(catalogueAjour);
    return this.filtre.selectionPizzaFiltres();
  }
  
  @Override
  public void supprimerFiltres() {
    this.filtre.supprimerFiltres();
  }
  
  /**
   * Gestion des évaluations.
   */
  
  @Override
  public Set<Evaluation> getEvaluationsPizza(Pizza pizza) {
    if (pizza == null) {
      return new HashSet<>();
    }
    return pizza.getEvaluations();
  }
  
  @Override
  public double getNoteMoyenne(Pizza pizza) {
    if (pizza == null) {
      return -2;
    }
    return pizza.getNoteMoyenne();
  }
  
  @Override
  public boolean ajouterEvaluation(Pizza pizza, int note, String commentaire)
      throws NonConnecteException, CommandeException {
    if (pizza == null) {
      return false;
    }
    return pizza.ajouterEvaluation(note, commentaire);
  }
}
