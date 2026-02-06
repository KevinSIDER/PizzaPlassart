package io;

import java.io.IOException;

/**
 * Définit les méthodes permettant de sauvegarder les données de l'application
 * dans un fichier.
 *
 * @author Eric Cariou
 */
public interface InterSauvegarde {
  
  /**
   * Sauvegarde toutes les données de l'application dans un fichier.
   *
   * @param nomFichier le fichier dans lequel sauvegarder les données
   * @throws IOException en cas de problème de sauvegarde
   */
  void sauvegarderDonnees(String nomFichier) throws IOException;
  
  /**
   * Charge les données de l'application à partir d'un fichier.
   *
   * @param nomFichier le fichier dans lequel les données ont été sauvegardées
   * @throws IOException en cas de problème de chargement
   */
  void chargerDonnees(String nomFichier) throws IOException;
}
