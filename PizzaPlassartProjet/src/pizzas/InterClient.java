package pizzas;

import java.util.List;
import java.util.Set;

/**
 * Interface des services utilisés par un client pour commander des pizzas et
 * évaluer des pizzas.
 *
 * @author Eric Cariou
 */
public interface InterClient {
  
  /**
   * Inscription d'un client. L'email choisi ne doit pas déjà exister parmi les
   * clients déjà inscrits.
   *
   * @param email l'adresse email (unique) du client
   * @param mdp le mot de passe du client (ne pas doit pas être vide ou
   *        <code>null</code>)
   * @param info les informations personnelles sur le client
   * @return un code précisant le résultat de l'inscription : 0 si l'inscription
   *         s'est bien déroulée, -1 si l'email est déjà utilisé, -2 si l'email
   *         ou le mot de passe est vide, -3 si les informations personnelles ne
   *         sont pas bien précisées, -4 si l'adresse email n'est pas bien
   *         formée
   */
  int inscription(String email, String mdp, InformationPersonnelle info);
  
  /**
   * Connexion d'un client. Une fois connecté, le client pourra passer des
   * commandes et accéder à ses anciennes commandes.
   *
   * @param email le pseudo du client
   * @param mdp le mot de passe du client
   * @return <code>true</code> si la connexion s'est bien déroulée,
   *         <code>false</code> en cas de couple pseudo/mot de passe invalide
   */
  boolean connexion(String email, String mdp);
  
  /**
   * Déconnecte le client actuellement connecté.
   *
   * @throws NonConnecteException si aucun client n'est connecté.
   */
  void deconnexion() throws NonConnecteException;
  
  /**
   * Crée une nouvelle commande pour le client actuellement connecté. On
   * ajoutera ensuite des pizzas à la commande avant de la valider.
   *
   * @return la commande qui vient d'être créée
   * @throws NonConnecteException si aucun client n'est connecté
   */
  Commande debuterCommande() throws NonConnecteException;
  
  /**
   * Ajoute une certain pizza à une commande.
   *
   * @param pizza la pizza que l'on commande
   * @param nombre le nombre de cette pizza que l'on commande
   * @param cmd la commande en cours à laquelle on ajoute la ou les pizzas
   * @throws NonConnecteException si aucun client n'est connecté
   * @throws CommandeException en cas de problème avec l'ajout des pizzas à la
   *         commande : la commande n'est pas une commande en cours, la commande
   *         n'a pas été créée par le client connecté, la pizza n'existe pas...
   */
  void ajouterPizza(Pizza pizza, int nombre, Commande cmd)
      throws NonConnecteException, CommandeException;
  
  /**
   * Valide une commande en cours. Une fois cela fait, la commande est visible
   * par le pizzaïlo et le client ne peut plus la modifier. La commande
   * apparaîtra dans la liste des commandes passées du client.
   *
   * @param cmd la commande à valider
   * @throws NonConnecteException si aucun client n'est connecté
   * @throws CommandeException en cas de problème avec la validation de la
   *         commande : la commande n'est pas une commande en cours, la commande
   *         n'a pas été créée par le client connecté...
   */
  void validerCommande(Commande cmd)
      throws NonConnecteException, CommandeException;
  
  /**
   * Annule une commande en cours. Une fois cela fait, la commande n'existe
   * plus.
   *
   * @param cmd la commande à annuler
   * @throws NonConnecteException si aucun client n'est connecté
   * @throws CommandeException en cas de problème avec l'annulation de la
   *         commande : la commande n'est pas une commande en cours, la commande
   *         n'a pas été créée par le client connecté...
   */
  void annulerCommande(Commande cmd)
      throws NonConnecteException, CommandeException;
  
  /**
   * Renvoie la liste des commandes actuellement en cours (créés mais non encore
   * validées) pour le client connecté.
   *
   * @return les commandes en cours du client connecté, ordonnées par création
   *         (la plus ancienne en premier, la plus récente en dernier)
   * @throws NonConnecteException si aucun client n'est connecté
   */
  List<Commande> getCommandesEncours() throws NonConnecteException;
  
  /**
   * Renvoie la liste des commandes passées (créées et validées mais pas
   * nécessairement toutes traitées par le pizzaïlo) par le client connecté.
   *
   * @return les commandes passées du client connecté, ordonnées par création
   *         (la plus ancienne en premier, la plus récente en dernier)
   * @throws NonConnecteException si aucun client n'est connecté
   */
  List<Commande> getCommandePassees() throws NonConnecteException;
  
  /**
   * Renvoie l'ensemble des pizzas en vente.
   *
   * @return l'ensemble des pizzas (l'ensemble est vide si aucune pizza
   *         n'existe)
   */
  Set<Pizza> getPizzas();
  
  /**
   * Ajoute un filtre en fonction du type de la pizza pour ne conserver que les
   * pizzas du type défini.
   *
   * @param type le type de pizza à conserver
   */
  void ajouterFiltre(TypePizza type);
  
  /**
   * Ajoute un filtre sur les ingrédients. Les pizzas conservées contiennent
   * tous les ingrédients de la liste. Si un ingrédient n'est pas valide, il est
   * ignoré.
   *
   * @param ingredients la liste des ingrédients que doivent contenir les pizzas
   */
  void ajouterFiltre(String... ingredients);
  
  /**
   * Ajoute un filre de prix maximum pour ne conserver que les pizzas qui ont un
   * prix inférieur ou égal à ce prix. Le prix doit être supérieur à 0 sinon le
   * filtre n'est pas appliqué.
   *
   * @param prixMaximum le prix maximum des pizzas
   */
  void ajouterFiltre(double prixMaximum);
  
  /**
   * Sélectionne les pizzas qui valident tous les filtres définis.
   *
   * @return l'ensemble filtré des pizzas (l'ensemble est vide si aucune pizza
   *         n'existe pour les filtres définis)
   */
  Set<Pizza> selectionPizzaFiltres();
  
  /**
   * Supprime tous les filtres qui ont été définis.
   */
  void supprimerFiltres();
  
  /**
   * Retourne l'ensemble des évaluations d'une pizza.
   *
   * @param pizza la pizza dont on veut les évaluations
   * @return l'ensemble des évaluations pour la pizza (ensemble vide si aucune
   *         évaluation n'a été faite pour la pizza) ou <code>null</code> si la
   *         pizza n'est pas valide
   */
  Set<Evaluation> getEvaluationsPizza(Pizza pizza);
  
  /**
   * Retourne la note moyenne des évaluations de la pizza.
   *
   * @param pizza la pizza dont on veut la note moyenne
   * @return la moyenne des évalutions (valeur entre 0 et 5), -1 si aucune
   *         évaluation n'existe, -2 si la pizza n'est pas valide
   */
  double getNoteMoyenne(Pizza pizza);
  
  /**
   * Ajoute une évaluation à une pizza de la part du client connecté.
   * L'évaluation consiste à mettre une note (une valeur entière entre 0 et 5)
   * avec éventuellement un commentaire textuel. Le client connecté doit avoir
   * déjà commandé la pizza (dans une commande validée) pour pouvoir faire un
   * commentaire sur la pizza. Si le client avait déjà évalué la pizza, la
   * nouvelle évaluation est ignorée.
   *
   * @param pizza la pizza que le client évalue
   * @param note la note entre 0 et 5
   * @param commentaire le commentaire ou <code>null</code> si aucun commentaire
   *        n'est ajouté
   * @return <code>true</code> si l'évaluation a été ajoutée à la pizza,
   *         <code>false</code> si l'évaluation n'a pas été ajoutée (pizza non
   *         valide, évaluation déjà faite pour cette pizza, note en dehors de
   *         l'intervalle...)
   * @throws NonConnecteException si aucun client n'est connecté
   * @throws CommandeException si le client n'avait jamais commandé cette pizza
   */
  boolean ajouterEvaluation(Pizza pizza, int note, String commentaire)
      throws NonConnecteException, CommandeException;
  
}
