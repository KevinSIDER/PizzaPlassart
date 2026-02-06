package pizzas;

/**
 * Représente un pizzaiolo, l'artisan responsable de la préparation des pizzas.
 * Un pizzaiolo est identifié par son nom.
 *
 * @author Léo Montay
 * @version 1.0
 * @see GestPizzaiolo
 */
public class Pizzaiolo {
  
  /**
   * Le nom du pizzaiolo.
   */
  private String nom;
  
  /**
   * Constructeur d'un pizzaiolo.
   *
   * @param nom Le nom du pizzaiolo
   */
  public Pizzaiolo(String nom) {
    this.nom = nom;
  }
  
  /**
   * Retourne le nom du pizzaiolo.
   *
   * @return Le nom du pizzaiolo
   */
  public String getNom() {
    return nom;
  }
  
  /**
   * Modifie le nom du pizzaiolo.
   *
   * @param nom Le nouveau nom du pizzaiolo
   */
  public void setNom(String nom) {
    this.nom = nom;
  }
}
