package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import pizzas.Client;
import pizzas.Commande;
import pizzas.Compte;
import pizzas.EtatCommande;
import pizzas.Evaluation;
import pizzas.GestPizzaiolo;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Classe gérant la persistance des données de l'application.
 * 
 * <p>Elle permet de sauvegarder et charger les ingrédients, les pizzas, les
 * clients, les interdictions, les commandes et les évaluations.
 *
 * @author Kevin SIDER
 * @version 1.1
 */
public class GestionSauvegarde implements InterSauvegarde {
  
  /**
   * Le gestionnaire métier.
   */
  private GestPizzaiolo gestionnaire;
  
  /**
   * Construit une instance de gestion de sauvegarde liée à un gestionnaire de
   * pizzaiolo.
   *
   * @param gestionnaire le gestionnaire principal de l'application
   *        (GestPizzaiolo)
   */  
  public GestionSauvegarde(GestPizzaiolo gestionnaire) {
    this.gestionnaire = gestionnaire;
  }
  
  @Override
  public void sauvegarderDonnees(String nomFichier) throws IOException {
    try (PrintWriter writer =
        new PrintWriter(new BufferedWriter(new FileWriter(nomFichier)))) {
      
      // 1. INGRÉDIENTS
      for (Ingredient ing : gestionnaire.getIngredients()) {
        writer.println("INGREDIENT;" + ing.getNom() + ";" + ing.getPrix());
      }
      
      // 2. PIZZAS
      for (Pizza p : gestionnaire.getPizzas()) {
        StringBuilder ligne = new StringBuilder();
        ligne.append("PIZZA;").append(p.getNom()).append(";")
            .append(p.getType()).append(";").append(p.getPrix()).append(";")
            .append(p.getPhoto() != null ? p.getPhoto() : "null");
        for (Ingredient ing : p.getIngredients()) {
          ligne.append(";").append(ing.getNom());
        }
        writer.println(ligne.toString());
      }
      
      // 3. CLIENTS
      for (Client c : Compte.getTousLesClients()) {
        Compte compte = c.getCompte();
        writer.println(
            "CLIENT;" + compte.getEmail() + ";" + compte.getMotDePasse() + ";"
                + compte.getNom() + ";" + compte.getPrenom() + ";"
                + compte.getAdresse() + ";" + compte.getAge());
      }
      
      // 4. INTERDICTIONS
      for (TypePizza type : TypePizza.values()) {
        for (Ingredient ing : gestionnaire.getIngredients()) {
          if (gestionnaire.estIngredientInterdit(type, ing)) {
            writer.println("INTERDICTION;" + ing.getNom() + ";" + type);
          }
        }
      }
      
      // 5. COMMANDES
      // Format : COMMANDE;emailClient;Etat;NomPizza1;NomPizza2...
      for (Commande c : gestionnaire.commandesDejaTraitees()) {
        // Sauvegarde les commandes traitées/validées
        StringBuilder ligne = new StringBuilder();
        ligne.append("COMMANDE;").append(c.getClient().getCompte().getEmail())
            .append(";").append(c.getEtat());
        
        for (Pizza p : c.getPizzas()) {
          ligne.append(";").append(p.getNom());
        }
        writer.println(ligne.toString());
      }
      
      // 6. EVALUATIONS
      // Format : EVALUATION;NomPizza;EmailAuteur;Note;Commentaire
      for (Pizza p : gestionnaire.getPizzas()) {
        for (Evaluation e : p.getEvaluations()) {
          writer.println("EVALUATION;" + p.getNom() + ";"
              + e.getAuteur().getCompte().getEmail() + ";" + e.getNote() + ";"
              + e.getCommentaire());
        }
      }
    }
  }
  
  @Override
  public void chargerDonnees(String nomFichier) throws IOException {
    try (BufferedReader reader =
        new BufferedReader(new FileReader(nomFichier))) {
      
      Compte.resetMemoire();
      
      String ligne;
      while ((ligne = reader.readLine()) != null) {
        String[] parts = ligne.split(";");
        if (parts.length < 2) {
          continue;
        }
        
        String typeDonnee = parts[0];
        
        if (typeDonnee.equals("INGREDIENT")) {
          if (parts.length >= 3) {
            gestionnaire.creerIngredient(parts[1],
                Double.parseDouble(parts[2]));
          }
          
        } else if (typeDonnee.equals("PIZZA")) {
          if (parts.length >= 5) {
            String nom = parts[1];
            TypePizza type = TypePizza.valueOf(parts[2]);
            double prix = Double.parseDouble(parts[3]);
            String photo = parts[4];
            
            Pizza p = gestionnaire.creerPizza(nom, type);
            if (p != null) {
              for (int i = 5; i < parts.length; i++) {
                gestionnaire.ajouterIngredientPizza(p, parts[i]);
              }
              gestionnaire.setPrixPizza(p, prix);
              if (!photo.equals("null")) {
                gestionnaire.ajouterPhoto(p, photo);
              }
            }
          }
          
        } else if (typeDonnee.equals("CLIENT")) {
          if (parts.length >= 7) {
            InformationPersonnelle info = new InformationPersonnelle(parts[3],
                parts[4], parts[5], Integer.parseInt(parts[6]));
            Compte.inscription(parts[1], parts[2], info);
          }
          
        } else if (typeDonnee.equals("INTERDICTION")) {
          if (parts.length >= 3) {
            gestionnaire.interdireIngredient(parts[1],
                TypePizza.valueOf(parts[2]));
          }
          
        } else if (typeDonnee.equals("COMMANDE")) {
          // Format : COMMANDE;email;etat;pizza1;pizza2...
          if (parts.length >= 4) {
            String email = parts[1];
            EtatCommande etat = EtatCommande.valueOf(parts[2]);
            
            // Retrouver le client
            Client client = null;
            for (Client c : Compte.getTousLesClients()) {
              if (c.getCompte().getEmail().equalsIgnoreCase(email)) {
                client = c;
                break;
              }
            }
            
            if (client != null) {
              Commande cmd = client.nouvelleCommande();
              // Ajout des pizzas
              for (int i = 3; i < parts.length; i++) {
                // On utilise la méthode statique de Pizza car le gestionnaire
                // ne retourne pas facilement l'objet Pizza
                Pizza p = Pizza.getPizzaParNom(parts[i]);
                if (p != null) {
                  cmd.ajouterPizza(p);
                }
              }
              // On force l'état (nécessaire pour contourner les règles de
              // transition si on veut charger directement en TRAITEE)
              cmd.setEtat(etat);
            }
          }
          
        } else if (typeDonnee.equals("EVALUATION")) {
          // Format : EVALUATION;nomPizza;email;note;commentaire
          if (parts.length >= 5) {
            String nomPizza = parts[1];
            String email = parts[2];
            int note = Integer.parseInt(parts[3]);
            String commentaire = parts[4];
            
            Pizza p = Pizza.getPizzaParNom(nomPizza);
            Client client = null;
            for (Client c : Compte.getTousLesClients()) {
              if (c.getCompte().getEmail().equalsIgnoreCase(email)) {
                client = c;
                break;
              }
            }
            
            if (p != null && client != null) {
              // On ajoute directement l'évaluation à la liste de la pizza
              // On ne passe pas par p.ajouterEvaluation() car cette méthode
              // exige que le client soit connecté
              Evaluation eval = new Evaluation(note, commentaire, client);
              p.getEvaluations().add(eval);
            }
          }
        }
      }
    } catch (Exception e) {
      throw new IOException("Erreur lecture fichier : " + e.getMessage(), e);
    }
  }
}
