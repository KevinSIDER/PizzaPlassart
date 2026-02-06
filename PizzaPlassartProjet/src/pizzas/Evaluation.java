package pizzas;

/**
 * Représente l'évaluation d'une pizza par un utilisateur. Cette classe permet
 * de stocker une note, un commentaire optionnel et l'email de l'utilisateur
 * ayant fait l'évaluation. Les notes sont comprises entre 0 et 5 inclus.
 *
 * @author Kevin SIDER
 * @version 1.0
 */
public class Evaluation {
  
  /**
   * Note attribuée a une pizza. La note doit être comprise entre 0 et 5 inclus.
   */
  private int note;
  
  /**
   * Commentaire optionnel associé à l'évaluation. Peut être vide mais ne doit
   * pas être null.
   */
  private String commentaire;
  
  /**
   * L'utilisateur ayant créé l'évaluation. Ne peut pas être null.
   */
  private Client client;
  
  /**
   * Crée une nouvelle évaluation complète avec note, commentaire et client.
   *
   * @param note La note attribuée a la pizza (entre 0 et 5 inclus)
   * @param commentaire Le commentaire associé à l'évaluation (peut être vide)
   * @param client Le client
   * @throws IllegalArgumentException si la note n'est pas dans l'intervalle ou
   *         si le client est null
   */
  public Evaluation(int note, String commentaire, Client client) {
    if (note < 0 || note > 5) {
      throw new IllegalArgumentException(
          "La note doit être comprise entre 0 et 5");
    }
    if (client == null) {
      throw new IllegalArgumentException("Le client ne peut pas être null");
    }
    if (commentaire == null) {
      throw new IllegalArgumentException(
          "Le commentaire ne peut pas être null");
    }
    this.note = note;
    this.commentaire = commentaire;
    this.client = client;
  }
  
  /**
   * Crée une nouvelle évaluation avec une note et un client. Le commentaire
   * sera initialisé comme vide.
   *
   * @param note La note attribuée a la pizza (entre 0 et 5 inclus)
   * @param client Le client
   * @throws IllegalArgumentException si la note n'est pas dans l'intervalle ou
   *         si le client est null
   */
  public Evaluation(int note, Client client) {
    this(note, "", client);
  }
  
  /**
   * Retourne la note attribuée a la pizza.
   *
   * @return la note entre 0 et 5 inclus
   */
  public int getNote() {
    return note;
  }
  
  /**
   * Retourne le commentaire associé à l'évaluation.
   *
   * @return le commentaire (peut être vide mais jamais null)
   */
  public String getCommentaire() {
    return commentaire;
  }
  
  /**
   * Retourne l'auteur de l'évaluation qui a créé l'évaluation.
   *
   * @return l'auteur de l'évaluation
   */
  public Client getAuteur() {
    return client;
  }
  
  /**
   * Retourne une représentation textuelle de l'évaluation.
   *
   * @return la représentation textuelle de l'évaluation
   */
  @Override
  public String toString() {
    String res = "Evaluation de : " + client.getInfoPersonnelle().getPrenom()
        + " " + client.getInfoPersonnelle().getNom() + " : " + note + "/5 "
        + commentaire;
    return res;
  }
}
