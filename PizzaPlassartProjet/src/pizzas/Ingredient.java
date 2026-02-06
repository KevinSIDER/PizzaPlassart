package pizzas;

import java.util.Locale;
import java.util.Objects;

/**
 * Représente un ingrédient entrant dans la composition d'une pizza. Un
 * ingrédient est caractérisé par son nom unique et son prix unitaire.
 *
 * @author Rayan Ladrait
 * @version 1.0
 */
public class Ingredient {
  
  /**
   * Le nom de l'ingrédient.
   */
  private String nom;
  
  /**
   * Le prix de l'ingrédient en euros.
   */
  private double prix;
  
  /**
   * Construit un nouvel ingrédient avec un nom et un prix.
   *
   * @param nom Le nom de l'ingrédient
   * @param prix Le prix de l'ingrédient
   */
  public Ingredient(String nom, double prix) {
    this.nom = nom;
    this.prix = prix;
  }
  
  /**
   * Retourne le nom de l'ingrédient.
   *
   * @return le nom de l'ingrédient
   */
  public String getNom() {
    return nom;
  }
  
  /**
   * Retourne le prix unitaire de l'ingrédient.
   *
   * @return le prix en euros
   */
  public double getPrix() {
    return prix;
  }
  
  /**
   * Modifie le prix de l'ingrédient.
   *
   * @param prix le nouveau prix en euros
   */
  public void setPrix(double prix) {
    this.prix = prix;
  }
  
  /**
   * Vérifie l'égalité entre deux ingrédients. L'égalité est basée uniquement
   * sur le nom de l'ingrédient.
   *
   * @param o l'objet à comparer
   * @return true si les deux ingrédients ont le même nom, false sinon
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ingredient that = (Ingredient) o;
    return Objects.equals(nom, that.nom);
  }
  
  /**
   * Calcule le code de hachage de l'ingrédient. Basé sur le nom pour être
   * cohérent avec equals.
   *
   * @return le hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(nom);
  }
  
  /**
   * Retourne une représentation textuelle de l'ingrédient. Format : "Nom
   * (Prix€)"
   *
   * @return la chaîne de caractères représentant l'ingrédient
   */
  @Override
  public String toString() {
    return nom + " (" + String.format(Locale.FRANCE, "%.2f", prix) + "€)";
  }
}