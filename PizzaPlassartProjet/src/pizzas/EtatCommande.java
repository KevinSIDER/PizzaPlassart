package pizzas;

/**
 * Énumération représentant les différents états possibles d'une commande de
 * pizzas. Une commande passe successivement par les états : CREE, VALIDEE, puis
 * TRAITEE.
 *
 * @author Léo Montay
 * @version 1.0
 */
public enum EtatCommande {
  
  /**
   * État initial d'une commande nouvellement créée. La commande a été
   * enregistrée mais n'est pas encore validée.
   */
  CREE,
  
  /**
   * État d'une commande qui a été vérifiée et acceptée. La commande est en
   * cours de préparation.
   */
  VALIDEE,
  
  /**
   * État final d'une commande terminée. La commande a été préparée et livrée au
   * client.
   */
  TRAITEE
}
