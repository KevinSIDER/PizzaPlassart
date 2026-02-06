package pizzas;

/**
 * Exception levée quand il y a un problème avec la commande d'un client.
 *
 * @author Eric Cariou
 */
public class CommandeException extends Exception {
  
  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = -2876441299971092712L;
  
  // A COMPLETER SI BESOIN
  
  /**
   * Constructeur avec message.
   *
   * @param message le message de l'exception
   */
  public CommandeException(String message) {
    super(message); // transmet le message à la classe Exception
  }
  
  /**
   * Constructeur avec message et cause.
   *
   * @param message le message de l'exception
   * @param cause la cause de l'exception
   */
  public CommandeException(String message, Throwable cause) {
    super(message, cause);
  }
}
