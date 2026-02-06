package pizzas;

/**
 * Exception levée quand il y a un problème avec le compte d'un utilisateur ou
 * sa connexion.
 *
 * @author Eric Cariou
 */
public class NonConnecteException extends Exception {
  
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
  public NonConnecteException(String message) {
    super(message);
  }
  
  /**
   * Constructeur avec message et cause.
   *
   * @param message le message de l'exception
   * @param cause la cause de l'exception
   */
  public NonConnecteException(String message, Throwable cause) {
    super(message, cause);
  }
}
