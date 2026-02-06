package ui;

import io.GestionSauvegarde;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pizzas.Client;
import pizzas.Commande;
import pizzas.Compte;
import pizzas.GestPizzaiolo;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.Pizzaiolo;
import pizzas.TypePizza;

/**
 * Controleur JavaFX de la fenêtre du pizzaïolo.
 *
 * @author Eric Cariou
 */
public class PizzaioloControleur {
  
  private GestPizzaiolo gestPizzaiolo;
  private Pizza pizzaSelectionnee;
  
  @FXML
  private ChoiceBox<String> choiceBoxTypeIngredient;
  @FXML
  private ChoiceBox<String> choiceBoxTypePizza;
  @FXML
  private ComboBox<String> comboBoxClients;
  @FXML
  private TextField entreeBeneficeClient;
  @FXML
  private TextField entreeBeneficeCommande;
  @FXML
  private TextField entreeBeneficeTotalCommandes;
  @FXML
  private TextField entreeBeneficeTotalPizza;
  @FXML
  private TextField entreeBeneficeUnitairePizza;
  @FXML
  private TextField entreeNbCommandesPizza;
  @FXML
  private TextField entreeNbPizzasClient;
  @FXML
  private TextField entreeNomIngredient;
  @FXML
  private TextField entreeNomPizza;
  @FXML
  private TextField entreeNombreTotalCommandes;
  @FXML
  private TextField entreePhotoPizza;
  @FXML
  private TextField entreePrixIngredient;
  @FXML
  private TextField entreePrixMinimalPizza;
  @FXML
  private TextField entreePrixVentePizza;
  @FXML
  private Label labelListeCommandes;
  @FXML
  private Label labelListeIngredients;
  @FXML
  private Label labelListePizzas;
  @FXML
  private ListView<String> listeCommandes;
  @FXML
  private ListView<String> listeIngredients;
  @FXML
  private ListView<String> listePizzas;
  
  @FXML
  void actionBoutonCreerIngredient(ActionEvent event) {
    String nom = entreeNomIngredient.getText();
    // On tolère la virgule à la saisie en la convertissant pour le calcul
    String prixStr = entreePrixIngredient.getText().replace(',', '.');
    
    if (nom == null || nom.trim().isEmpty()) {
      afficherAlerte("Erreur", "Veuillez saisir un nom d'ingrédient",
          Alert.AlertType.ERROR);
      return;
    }
    
    double prix;
    try {
      prix = Double.parseDouble(prixStr);
    } catch (NumberFormatException e) {
      afficherAlerte("Erreur", "Le prix doit être un nombre valide",
          Alert.AlertType.ERROR);
      return;
    }
    
    int resultat = gestPizzaiolo.creerIngredient(nom, prix);
    
    switch (resultat) {
      case 0:
        afficherAlerte("Succès", "Ingrédient créé",
            Alert.AlertType.INFORMATION);
        actualiserListeIngredients();
        entreeNomIngredient.clear();
        entreePrixIngredient.clear();
        break;
      case -1:
        afficherAlerte("Erreur", "Nom invalide", Alert.AlertType.ERROR);
        break;
      case -2:
        afficherAlerte("Erreur", "Existe déjà", Alert.AlertType.ERROR);
        break;
      case -3:
        afficherAlerte("Erreur", "Prix > 0 requis", Alert.AlertType.ERROR);
        break;
      default:
        break;
    }
  }
  
  @FXML
  void actionBoutonModifierPrixIngredient(ActionEvent event) {
    String nomIngredient =
        listeIngredients.getSelectionModel().getSelectedItem();
    if (nomIngredient == null) {
      afficherAlerte("Erreur", "Sélectionnez un ingrédient",
          Alert.AlertType.WARNING);
      return;
    }
    
    String nom = nomIngredient.split(" \\(")[0];
    String prixStr = entreePrixIngredient.getText().replace(',', '.');
    
    double prix;
    try {
      prix = Double.parseDouble(prixStr);
    } catch (NumberFormatException e) {
      afficherAlerte("Erreur", "Prix invalide", Alert.AlertType.ERROR);
      return;
    }
    
    int resultat = gestPizzaiolo.changerPrixIngredient(nom, prix);
    
    switch (resultat) {
      case 0:
        afficherAlerte("Succès", "Prix modifié", Alert.AlertType.INFORMATION);
        actualiserListeIngredients();
        entreePrixIngredient.clear();
        break;
      case -1:
        afficherAlerte("Erreur", "Nom invalide", Alert.AlertType.ERROR);
        break;
      case -2:
        afficherAlerte("Erreur", "Prix > 0 requis", Alert.AlertType.ERROR);
        break;
      case -3:
        afficherAlerte("Erreur", "Ingrédient inconnu", Alert.AlertType.ERROR);
        break;
      default:
        break;
    }
  }
  
  @FXML
  void actionBoutonInterdireIngredient(ActionEvent event) {
    String nomIngredient =
        listeIngredients.getSelectionModel().getSelectedItem();
    if (nomIngredient == null) {
      afficherAlerte("Erreur", "Sélectionnez un ingrédient",
          Alert.AlertType.WARNING);
      return;
    }
    
    String typeSelectionne = choiceBoxTypeIngredient.getValue();
    if (typeSelectionne == null) {
      afficherAlerte("Erreur", "Sélectionnez un type", Alert.AlertType.WARNING);
      return;
    }
    
    String nom = nomIngredient.split(" \\(")[0];
    TypePizza type = TypePizza.valueOf(typeSelectionne);
    
    boolean resultat = gestPizzaiolo.interdireIngredient(nom, type);
    
    if (resultat) {
      Ingredient ing = null;
      for (Ingredient i : gestPizzaiolo.getIngredients()) {
        if (i.getNom().equals(nom)) {
          ing = i;
          break;
        }
      }
      if (gestPizzaiolo.estIngredientInterdit(type, ing)) {
        afficherAlerte("Succès",
            nom + " est maintenant INTERDIT pour les " + type,
            Alert.AlertType.INFORMATION);
      } else {
        afficherAlerte("Succès",
            nom + " est maintenant AUTORISÉ pour les " + type,
            Alert.AlertType.INFORMATION);
      }
    } else {
      afficherAlerte("Erreur", "Erreur technique", Alert.AlertType.ERROR);
    }
  }
  
  @FXML
  void actionBoutonAfficherTousIngredients(ActionEvent event) {
    actualiserListeIngredients();
  }
  
  @FXML
  void actionListeSelectionIngredient(MouseEvent event) {
    String ingredientSelectionne =
        listeIngredients.getSelectionModel().getSelectedItem();
    if (ingredientSelectionne != null) {
      String[] parties = ingredientSelectionne.split(" \\(");
      String nom = parties[0];
      if (parties.length > 1) {
        String prixStr = parties[1].replace("€)", "").replace(" ", "");
        // L'affichage direct du prix récupéré de la liste (qui est déjà en
        // virgule grâce au toString)
        entreeNomIngredient.setText(nom);
        entreePrixIngredient.setText(prixStr);
      }
    }
  }
  
  private void actualiserListeIngredients() {
    listeIngredients.getItems().clear();
    for (Ingredient ing : gestPizzaiolo.getIngredients()) {
      listeIngredients.getItems().add(ing.toString());
    }
    labelListeIngredients.setText("Liste des ingrédients ("
        + gestPizzaiolo.getIngredients().size() + ")");
  }
  
  private void afficherAlerte(String titre, String message,
      Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(titre);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  
  @FXML
  void actionBoutonCreerPizza(ActionEvent event) {
    String nom = entreeNomPizza.getText();
    String typeSelectionne = choiceBoxTypePizza.getValue();
    
    if (nom == null || nom.trim().isEmpty()) {
      afficherAlerte("Erreur", "Nom invalide", Alert.AlertType.ERROR);
      return;
    }
    if (typeSelectionne == null) {
      afficherAlerte("Erreur", "Type manquant", Alert.AlertType.WARNING);
      return;
    }
    
    TypePizza type = TypePizza.valueOf(typeSelectionne);
    Pizza nouvellePizza = gestPizzaiolo.creerPizza(nom, type);
    
    if (nouvellePizza != null) {
      afficherAlerte("Succès", "Pizza créée", Alert.AlertType.INFORMATION);
      actualiserListePizzas();
      entreeNomPizza.clear();
    } else {
      afficherAlerte("Erreur", "Impossible de créer (nom existe déjà ?)",
          Alert.AlertType.ERROR);
    }
  }
  
  @FXML
  void actionBoutonAjouterIngredientPizza(ActionEvent event) {
    if (pizzaSelectionnee == null
        || listeIngredients.getSelectionModel().getSelectedItem() == null) {
      return;
    }
    
    String nom =
        listeIngredients.getSelectionModel().getSelectedItem().split(" \\(")[0];
    int resultat = gestPizzaiolo.ajouterIngredientPizza(pizzaSelectionnee, nom);
    
    switch (resultat) {
      case 0:
        actualiserDetailsPizza(pizzaSelectionnee);
        break;
      case -3:
        afficherAlerte("Erreur", "Ingrédient INTERDIT pour ce type",
            Alert.AlertType.ERROR);
        break;
      default:
        afficherAlerte("Erreur", "Erreur ajout (" + resultat + ")",
            Alert.AlertType.ERROR);
    }
  }
  
  @FXML
  void actionBoutonSupprimerIngredientPizza(ActionEvent event) {
    if (pizzaSelectionnee == null
        || listeIngredients.getSelectionModel().getSelectedItem() == null) {
      return;
    }
    
    String nom =
        listeIngredients.getSelectionModel().getSelectedItem().split(" \\(")[0];
    int resultat = gestPizzaiolo.retirerIngredientPizza(pizzaSelectionnee, nom);
    
    if (resultat == 0) {
      actualiserDetailsPizza(pizzaSelectionnee);
    } else {
      afficherAlerte("Erreur", "Erreur suppression", Alert.AlertType.ERROR);
    }
  }
  
  @FXML
  void actionBoutonVerifierValiditeIngredientsPizza(ActionEvent event) {
    if (pizzaSelectionnee == null) {
      afficherAlerte("Erreur", "Veuillez sélectionner une pizza",
          Alert.AlertType.WARNING);
      return;
    }
    
    java.util.Set<String> ingredientsInterdits =
        gestPizzaiolo.verifierIngredientsPizza(pizzaSelectionnee);
    
    if (ingredientsInterdits == null) {
      afficherAlerte("Erreur", "Pizza invalide (non trouvée)",
          Alert.AlertType.ERROR);
      
    } else if (ingredientsInterdits.isEmpty()) {
      // CAS 1 : Tout est valide
      afficherAlerte("Validation",
          "Tous les ingrédients sont valides pour cette pizza",
          Alert.AlertType.INFORMATION);

      actualiserListeIngredients();
      
    } else {
      // CAS 2 : Il y a des ingrédients interdits
      // ON VIDE LA LISTE pour n'afficher QUE les interdits
      listeIngredients.getItems().clear();
      
      for (String nomIng : ingredientsInterdits) {
        // On cherche l'objet ingrédient complet pour avoir son prix et son
        for (Ingredient ing : gestPizzaiolo.getIngredients()) {
          if (ing.getNom().equals(nomIng)) {
            listeIngredients.getItems().add(ing.toString());
            break;
          }
        }
      }
      
      labelListeIngredients.setText(
          "Ingrédients INVALIDES (" + ingredientsInterdits.size() + ")");
      afficherAlerte("Attention", "Cette pizza contient "
          + ingredientsInterdits.size() + " ingrédient(s) interdit(s) !",
          Alert.AlertType.WARNING);
    }
  }
  
  @FXML
  void actionBoutonModifierPrixPizza(ActionEvent event) {
    if (pizzaSelectionnee == null) {
      return;
    }
    
    String prixStr = entreePrixVentePizza.getText().replace(',', '.');
    
    try {
      double prix = Double.parseDouble(prixStr);
      boolean resultat = gestPizzaiolo.setPrixPizza(pizzaSelectionnee, prix);
      
      if (resultat) {
        afficherAlerte("Succès", "Prix modifié", Alert.AlertType.INFORMATION);
        actualiserDetailsPizza(pizzaSelectionnee);
        actualiserListePizzas();
      } else {
        double min = gestPizzaiolo.calculerPrixMinimalPizza(pizzaSelectionnee);
        afficherAlerte(
            "Erreur", "Prix min requis : "
                + String.format(Locale.FRANCE, "%.2f", min) + "€",
            Alert.AlertType.ERROR);
      }
    } catch (NumberFormatException e) {
      afficherAlerte("Erreur", "Prix invalide", Alert.AlertType.ERROR);
    }
  }
  
  @FXML
  void actionBoutonParcourirPhotoPizza(ActionEvent event) {
    if (pizzaSelectionnee == null) {
      return;
    }
    javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
    fileChooser.getExtensionFilters()
        .add(new javafx.stage.FileChooser.ExtensionFilter("Images", "*.jpg",
            "*.png"));
    java.io.File fichier =
        fileChooser.showOpenDialog(entreePhotoPizza.getScene().getWindow());
    
    if (fichier != null) {
      try {
        if (gestPizzaiolo.ajouterPhoto(pizzaSelectionnee,
            fichier.getAbsolutePath())) {
          entreePhotoPizza.setText(fichier.getAbsolutePath());
          afficherAlerte("Succès", "Photo ajoutée",
              Alert.AlertType.INFORMATION);
        }
      } catch (Exception e) {
        afficherAlerte("Erreur", "Erreur fichier", Alert.AlertType.ERROR);
      }
    }
  }
  
  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {
    actualiserListePizzas();
  }
  
  @FXML
  void actionBoutonAfficherListeTrieePizzas(ActionEvent event) {
    List<Pizza> triees = gestPizzaiolo.classementPizzasParNombreCommandes();
    listePizzas.getItems().clear();
    for (Pizza p : triees) {
      int nb = gestPizzaiolo.nombrePizzasCommandees(p);
      listePizzas.getItems().add(p.toString() + " [" + nb + " commandes]");
    }
    labelListePizzas.setText("Pizzas (Triées par popularité)");
  }
  
  @FXML
  void actionListeSelectionPizza(MouseEvent event) {
    String selected = listePizzas.getSelectionModel().getSelectedItem();
    if (selected != null) {
      String nom = selected.split(" \\(")[0];
      for (Pizza p : gestPizzaiolo.getPizzas()) {
        if (p.getNom().equals(nom)) {
          pizzaSelectionnee = p;
          actualiserDetailsPizza(p);
          break;
        }
      }
    }
  }
  
  private void actualiserListePizzas() {
    listePizzas.getItems().clear();
    for (Pizza p : gestPizzaiolo.getPizzas()) {
      listePizzas.getItems().add(p.toString());
    }
    labelListePizzas
        .setText("Liste des pizzas (" + gestPizzaiolo.getPizzas().size() + ")");
  }
  
  private void actualiserDetailsPizza(Pizza pizza) {
    if (pizza == null) {
      return;
    }
    entreeNomPizza.setText(pizza.getNom());
    choiceBoxTypePizza.setValue(pizza.getType().toString());
    
    double min = gestPizzaiolo.calculerPrixMinimalPizza(pizza);
    double vente = gestPizzaiolo.getPrixPizza(pizza);
    
    // FORMAT FRANCE : Virgule
    entreePrixMinimalPizza.setText(String.format(Locale.FRANCE, "%.2f", min));
    entreePrixVentePizza.setText(String.format(Locale.FRANCE, "%.2f", vente));
    entreeBeneficeUnitairePizza
        .setText(String.format(Locale.FRANCE, "%.2f", vente - min));
    
    entreePhotoPizza.setText(pizza.getPhoto() != null ? pizza.getPhoto() : "");
    
    listeIngredients.getItems().clear();
    for (Ingredient i : pizza.getIngredients()) {
      listeIngredients.getItems().add(i.toString());
    }
    labelListeIngredients
        .setText("Ingrédients (" + pizza.getIngredients().size() + ")");
    
    entreeNbCommandesPizza
        .setText(String.valueOf(gestPizzaiolo.nombrePizzasCommandees(pizza)));
    Map<Pizza, Double> benefs = gestPizzaiolo.beneficeParPizza();
    entreeBeneficeTotalPizza.setText(
        String.format(Locale.FRANCE, "%.2f", benefs.getOrDefault(pizza, 0.0)));
  }
  
  @FXML
  void actionBoutonCommandesDejaTraitees(ActionEvent event) {
    afficherListeCommandes(gestPizzaiolo.getCommandesTraitees(),
        "Commandes traitées");
  }
  
  @FXML
  void actionBoutonCommandesNonTraitees(ActionEvent event) {
    afficherListeCommandes(gestPizzaiolo.commandeNonTraitees(),
        "Non traitées (validées)");
  }
  
  @FXML
  void actionBoutonCommandesTraiteesClient(ActionEvent event) {
    if (comboBoxClients.getItems().isEmpty()) {
      for (Client c : Compte.getTousLesClients()) {
        comboBoxClients.getItems().add(c.getCompte().getEmail());
      }
    }
    String email = comboBoxClients.getValue();
    if (email == null) {
      return;
    }
    
    InformationPersonnelle info = gestPizzaiolo.getClientByEmail(email);
    if (info != null) {
      afficherListeCommandes(gestPizzaiolo.commandesTraiteesClient(info),
          "Commandes de " + email);
    }
  }
  
  private void afficherListeCommandes(List<Commande> cmds, String titre) {
    listeCommandes.getItems().clear();
    for (Commande c : cmds) {
      listeCommandes.getItems().add(c.toString());
    }
    labelListeCommandes.setText(titre + " (" + cmds.size() + ")");
  }
  
  @FXML
  void actionListeSelectionCommande(MouseEvent event) {
    String cmdStr = listeCommandes.getSelectionModel().getSelectedItem();
    if (cmdStr == null) {
      return;
    }
    
    Commande c = gestPizzaiolo.getCommandeByString(cmdStr);
    
    entreeNombreTotalCommandes
        .setText(String.valueOf(gestPizzaiolo.commandesDejaTraitees().size()));
    try {
      entreeBeneficeTotalCommandes.setText(String.format(Locale.FRANCE, "%.2f",
          gestPizzaiolo.beneficeToutesCommandes()));
    } catch (Exception e) {
      entreeBeneficeTotalCommandes.setText("0,00");
    }
    
    if (c != null) {
      try {
        entreeBeneficeCommande.setText(String.format(Locale.FRANCE, "%.2f",
            gestPizzaiolo.beneficeCommandes(c)));
      } catch (Exception e) {
        entreeBeneficeCommande.setText("0,00");
      }
      
      Client client = c.getClient();
      if (client != null) {
        InformationPersonnelle info = client.getInfoPersonnelle();
        Integer nb = gestPizzaiolo.nombrePizzasCommandeesParClient().get(info);
        entreeNbPizzasClient.setText(String.valueOf(nb != null ? nb : 0));
        
        Double benef = gestPizzaiolo.beneficeParClient().get(info);
        entreeBeneficeClient.setText(
            String.format(Locale.FRANCE, "%.2f", benef != null ? benef : 0.0));
      }
    } else {
      entreeBeneficeCommande.setText("");
      entreeNbPizzasClient.setText("");
      entreeBeneficeClient.setText("");
    }
  }
  
  @FXML
  void actionSelectionClient(ActionEvent event) {
    String email = comboBoxClients.getValue();
    if (email == null) {
      return;
    }
    InformationPersonnelle info = gestPizzaiolo.getClientByEmail(email);
    if (info == null) {
      return;
    }
    
    afficherListeCommandes(gestPizzaiolo.commandesTraiteesClient(info),
        "Commandes de " + email);
    
    Integer nb = gestPizzaiolo.nombrePizzasCommandeesParClient().get(info);
    entreeNbPizzasClient.setText(String.valueOf(nb != null ? nb : 0));
    
    Double benef = gestPizzaiolo.beneficeParClient().get(info);
    entreeBeneficeClient.setText(
        String.format(Locale.FRANCE, "%.2f", benef != null ? benef : 0.0));
  }
  
  @FXML
  void actionMenuApropos(ActionEvent e) {
    afficherAlerte("A propos",
        "Application de gestion de Pizzeria \n"
            + "Développée dans le cadre du projet Java. \n" + "Version 1.0",
        Alert.AlertType.INFORMATION);
  }
  
  @FXML
  void actionMenuQuitter(ActionEvent e) {
    System.exit(0);
  }
  
  @FXML
  void actionMenuSauvegarder(ActionEvent e) {
    try {
      new GestionSauvegarde(gestPizzaiolo)
          .sauvegarderDonnees("donnees_pizzeria.txt");
      afficherAlerte("Sauvegarde", "Votre fichier à bien été sauvegarder.",
          Alert.AlertType.INFORMATION);
    } catch (Exception ex) {
      afficherAlerte("Erreur", ex.getMessage(), Alert.AlertType.ERROR);
    }
  }
  
  @FXML
  void actionMenuCharger(ActionEvent e) {
    try {
      new GestionSauvegarde(gestPizzaiolo)
          .chargerDonnees("donnees_pizzeria.txt");
      actualiserListeIngredients();
      actualiserListePizzas();
      comboBoxClients.getItems().clear();
      for (Client c : Compte.getTousLesClients()) {
        comboBoxClients.getItems().add(c.getCompte().getEmail());
      }
      afficherAlerte("Chargement", "Votre fichier a bien été charger.",
          Alert.AlertType.INFORMATION);
    } catch (Exception ex) {
      afficherAlerte("Erreur", ex.getMessage(), Alert.AlertType.ERROR);
    }
  }
  
  @FXML
  void initialize() {
    gestPizzaiolo = new GestPizzaiolo(new Pizzaiolo("Mario"));
    choiceBoxTypeIngredient.getItems().addAll("Viande", "Vegetarienne",
        "Regionale");
    choiceBoxTypePizza.getItems().addAll("Viande", "Vegetarienne", "Regionale");
    
    choiceBoxTypePizza.getSelectionModel().selectedItemProperty()
        .addListener((o, old, newVal) -> {
          if (pizzaSelectionnee != null && newVal != null) {
            try {
              pizzaSelectionnee.setType(TypePizza.valueOf(newVal));
            } catch (Exception e) {
              System.err.println("Type de pizza invalide : " + newVal);
            }
          }
        });
    
    actualiserListeIngredients();
    actualiserListePizzas();
    for (Client c : Compte.getTousLesClients()) {
      comboBoxClients.getItems().add(c.getCompte().getEmail());
    }
    System.out.println("Pizzaiolo prêt !");
  }
}
