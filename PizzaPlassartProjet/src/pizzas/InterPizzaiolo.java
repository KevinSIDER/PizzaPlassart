package pizzas;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface des services utilisés par le pizzaïolo pour concevoir les pizzas
 * avec leurs ingrédients et pour obtenir des statistiques sur les ventes de ses
 * pizzas.
 *
 * @author Eric Cariou
 */
public interface InterPizzaiolo {
  
  /**
   * Création d'un nouvel ingrédient avec son prix. Une fois créé, l'ingrédient
   * est disponible pour faire partie de la composition de pizzas.
   *
   * @param nom le nom de l'ingrédient
   * @param prix le prix de l'ingrédient
   * @return 0 si tout s'est bien passé, -1 si le nom de l'ingrédient n'est pas
   *         valide (chaine vide ou <code>null</code>), -2 s'il existait déjà un
   *         ingrédient du même nom, -3 si le prix est invalide (inférieur ou
   *         égal à 0)
   */
  int creerIngredient(String nom, double prix);
  
  /**
   * Change le prix d'un ingrédient déjà existant.
   *
   * @param nom le nom de l'ingrédient
   * @param prix le nouveau prix de l'ingrédient
   * @return 0 si tout s'est bien passé, -1 si le nom de l'ingrédient n'est pas
   *         valide (chaine vide ou <code>null</code>), -2 si le prix est
   *         invalide (inférieur ou égal à 0), -3 si aucun ingrédient de ce nom
   *         n'existe
   */
  int changerPrixIngredient(String nom, double prix);
  
  /**
   * Précise qu'un ingrédient ne peut pas être utilisé pour un certain type de
   * pizza. Par exemple, du jambon ou du boeuf haché ne peut pas être utilisé
   * dans une pizza végétarienne.
   *
   * @param nomIngredient le nom de l'ingrédient à exclure
   * @param type le type de pizza dont on exclut l'ingrédient
   * @return <code>true</code> si l'ingrédient a bien été ajouté comme exclu
   *         pour le type de pizza, <code>false</code> en cas de problème
   *         (l'ingrédient n'existait pas...) et dans ce cas, aucune exclusion
   *         n'est ajoutée
   */
  boolean interdireIngredient(String nomIngredient, TypePizza type);
  
  /**
   * Crée une nouvelle pizza. Il ne doit pas déjà exister une pizza avec le même
   * nom. Une fois créée, la pizza est visible par les clients et disponible à
   * la vente. Toutes les opérations de cette interface qui prennent en
   * paramètre une pizza ne s'appliquent que sur des pizzas créées par cette
   * méthode.
   *
   * @param nom le nom de la nouvelle pizza
   * @param type le type de la pizza
   * @return la pizza créée ou <code>null</code> en cas de problème (nom
   *         invalide ou pizza du même nom existant déjà)
   */
  Pizza creerPizza(String nom, TypePizza type);
  
  /**
   * Ajoute un ingrédient à une pizza. Ne fait rien si l'ingrédient avait déjà
   * été ajouté à la pizza.
   *
   * @param pizza la pizza à laquelle ajouter l'ingrédient
   * @param nomIngredient le nom de l'ingrédient à ajouter
   * @return 0 si tout s'est bien passé, -1 si la pizza était invalide (valeur
   *         <code>null</code> ou pizza n'existant pas), -2 si le nom de
   *         l'ingredient est invalide (chaine vide, valeur <code>null</code> ou
   *         l'ingrédient n'existe pas), -3 si l'ingredient est interdit pour le
   *         type de la pizza
   */
  int ajouterIngredientPizza(Pizza pizza, String nomIngredient);
  
  /**
   * Supprime un ingrédient d'une pizza.
   *
   * @param pizza la pizza dont on retire l'ingrédient
   * @param nomIngredient le nom de l'ingrédient à retirer
   * @return 0 si tout s'est bien passé, -1 si la pizza était invalide (valeur
   *         <code>null</code> ou pizza n'existant pas), -2 si le nom de
   *         l'ingredient est invalide (chaine vide, valeur <code>null</code> ou
   *         l'ingrédient n'existe pas) , -3 si l'ingredient n'existait pas pour
   *         la pizza
   */
  int retirerIngredientPizza(Pizza pizza, String nomIngredient);
  
  /**
   * Vérifie que des ingrédients d'une pizza ne sont pas interdits pour le type
   * de la pizza. Cette vérification est utile si on a ajouté des interdictions
   * d'ingrédients après avoir créé la pizza avec des ingrédients.
   *
   * @param pizza la pizza dont on veut vérifier les ingrédients
   * @return l'ensemble des ingrédients interdits pour la pizza (l'ensemble est
   *         vide si tous les intégrients sont valides) ou <code>null</code> si
   *         la pizza n'est pas valide
   */
  Set<String> verifierIngredientsPizza(Pizza pizza);
  
  /**
   * Ajoute une photo à une pizza. Remplace la photo précédente si une photo
   * était déjà associée à la pizza.
   *
   * @param pizza la pizza à laquelle on veut ajouter une photo
   * @param file le nom du fichier qui contient l'image
   * @return <code>true</code> si la photo a été ajoutée, <code>false</code> en
   *         cas de problème (pizza invalide, fichier qui ne contient pas une
   *         image...)
   * @throws IOException en cas d'erreur de lecture sur le fichier
   */
  boolean ajouterPhoto(Pizza pizza, String file) throws IOException;
  
  /**
   * Retourne le prix de vente d'une pizza. Si le prix n'a pas été fixé
   * manuellement, retourne le prix minimal basé sur le prix des ingrédients.
   *
   * @param pizza la pizza dont on veut connaitre le prix
   * @return le prix de la pizza ou -1 si la pizza n'était pas valide
   */
  double getPrixPizza(Pizza pizza);
  
  /**
   * Modifie le prix de vente d'une pizza. Le prix doit être supérieur ou égal
   * au prix minimal de la pizza.
   *
   * @param pizza la pizza dont on change le prix
   * @param prix le nouveau prix
   * @return <code>true</code> si le prix a été modifié car correct ou
   *         <code>false</code> si le prix proposé est inférieur au prix minimal
   *         (dans ce cas, le prix n'a pas été modifié) ou que la pizza n'était
   *         pas valide
   */
  boolean setPrixPizza(Pizza pizza, double prix);
  
  /**
   * Calcule le prix minimal d'une pizza en fonction de ses ingrédients (sans
   * modifier le prix courant de la pizza). Le prix minimal d'une pizza est la
   * somme des prix de ses ingrédients augmentée de 40% et arrondie à la dizaine
   * d'€ supérieure.
   *
   * @param pizza la pizza dont on veut calculer le prix minimal
   * @return le prix minimal de la pizza ou -1 si la pizza n'est pas valide
   */
  double calculerPrixMinimalPizza(Pizza pizza);
  
  /**
   * Retourne l'ensemble des pizzas.
   *
   * @return l'ensemble des pizzas (l'ensemble est vide si aucune pizza n'a
   *         encore été créée)
   */
  Set<Pizza> getPizzas();
  
  /**
   * Renvoie l'ensemble des clients qui ont un compte dans la pizzeria.
   *
   * @return l'ensemble des clients (l'ensemble est vide s'il n'y a aucun
   *         client)
   */
  Set<InformationPersonnelle> ensembleClients();
  
  /**
   * Retourne l'ensemble des commandes passées par les clients. Elles sont
   * classées de la plus ancienne à la plus récente.
   *
   * @return l'ensemble ordonné des commandes qui ont été traitées (l'ensemble
   *         est vide si aucune commande n'a été traitée)
   */
  List<Commande> commandesDejaTraitees();
  
  /**
   * Retourne l'ensemble des commandes des clients non encore traitées. Elles
   * sont classées de la plus ancienne à la plus récente. Une fois que ces
   * commandes ont été lues en appelant cette méthode, on les considère comme
   * traitées.
   *
   * @return l'ensemble ordonné des commandes des clients à traiter (l'ensemble
   *         est vide si aucune commande n'est à traiter)
   */
  List<Commande> commandeNonTraitees();
  
  /**
   * Retourne l'ensemble des commandes passées par un certain client et déjà
   * traitées. Elles sont classées de la plus ancienne à la plus récente.
   *
   * @param client le client dont on veut les commandes
   * @return la liste ordonnée des commandes du client (la liste est vide si le
   *         client n'a pas passé de commandes) ou <code>null</code> si le
   *         client n'est pas valide
   */
  List<Commande> commandesTraiteesClient(InformationPersonnelle client);
  
  /**
   * Calcule le bénéfice pour chacune des pizzas en vente. Le bénéfice pour une
   * pizza est la différence entre le prix minimal et le prix de vente de la
   * pizza. Par construction, le bénéfice est supérieur ou égal à 0.
   *
   * @return une map qui associe à chaque pizza son bénéfice unitaire
   */
  Map<Pizza, Double> beneficeParPizza();
  
  /**
   * Calcule le bénéfice d'une commande. Pour cela, on fait la somme des
   * bénéfices de chacune des pizzas de la commande.
   *
   * @param commande la commande dont on veut calculer le bénéfice
   * @return le bénéfice de la commande (ou -1 si la commande n'était pas
   *         valide)
   */
  double beneficeCommandes(Commande commande);
  
  /**
   * Calcule le bénéfice global pour l'ensemble des commandes déjà traitées pour
   * tous les clients.
   *
   * @return le bénéfice global
   */
  double beneficeToutesCommandes();
  
  /**
   * Calcule le nombre total de pizzas commandées par chaque client (en se
   * basant sur les commandes déjà traitées).
   *
   * @return une map qui associe à chaque client le nombre de pizzas qu'il a
   *         commandées
   */
  Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient();
  
  /**
   * Calcule le bénéfice généré par chaque client, à partir des pizzas qu'il a
   * commandées (en se basant sur les commandes déjà traitées).
   *
   * @return une map qui associe à chaque client le bénéfice des pizzas qu'il a
   *         commandées
   */
  Map<InformationPersonnelle, Double> beneficeParClient();
  
  /**
   * Pour une certaine pizza, retourne le nombre de fois où elle a été commandée
   * par les clients (en se basant sur les commandes déjà traitées).
   *
   * @param pizza la pizza dont on cherche le nombre de commandes
   * @return le nombre de fois où la pizza a été commandée (ou -1 si la pizza
   *         n'était pas valide)
   */
  int nombrePizzasCommandees(Pizza pizza);
  
  /**
   * Retourne la liste ordonnée des pizzas selon leur nombre de commandes (en se
   * basant sur les commandes déjà traitées). La première pizza de la liste est
   * la pizza la plus commandée, la dernière de la liste est la pizza la moins
   * commandée.
   *
   * @return la liste ordonnée des pizzas selon le nombre de commandes (la liste
   *         est vide si aucune pizza n'est en vente)
   */
  List<Pizza> classementPizzasParNombreCommandes();
}
