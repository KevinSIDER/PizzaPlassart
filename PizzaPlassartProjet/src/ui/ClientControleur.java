package ui;

import java.util.Set;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import pizzas.Client;
import pizzas.Commande;
import pizzas.CommandeException;
import pizzas.Compte;
import pizzas.Evaluation;
import pizzas.GestClient;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.InterClient;
import pizzas.NonConnecteException;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Controleur JavaFX de la fenêtre du client.
 *
 * @author Eric Cariou
 */
public class ClientControleur {
  
  private static final int AGE_MIN = 15;
  private static final int AGE_MAX = 120;
  private static final String MSG_MAIL_INVALIDE = "format mail non valide";
  
  private InterClient model;
  
  /**
   * Initialise le modèle métier du contrôleur avec une instance de GestClient.
   */
  public ClientControleur() {
    this.model = new GestClient();
  }
  
  private void afficherCommandesEnCours() {
    listeCommandes.getItems().clear();
    try {
      for (Commande c : model.getCommandesEncours()) {
        listeCommandes.getItems().add("Commande " + c.getIdCommande());
      }
      labelListeCommandes.setText("Commandes en cours");
    } catch (NonConnecteException e) {
      popupErreur(e.getMessage());
    }
  }
  
  @FXML
  private ChoiceBox<String> choiceBoxFiltreType;
  
  @FXML
  private TextField entreeAdresseClient;
  
  @FXML
  private TextField entreeAgeClient;
  
  @FXML
  private TextField entreeAuteurEvaluation;
  
  @FXML
  private TextField entreeEmailClient;
  
  @FXML
  private TextField entreeEvaluationMoyenneEvaluations;
  
  @FXML
  private TextField entreeFiltreContientIngredient;
  
  @FXML
  private TextField entreeFiltrePrixMax;
  
  @FXML
  private PasswordField entreeMotDePasseClient;
  
  @FXML
  private TextField entreeNomClient;
  
  @FXML
  private TextField entreeNomPizza;
  
  @FXML
  private TextField entreeNomPizzaEvaluee;
  
  @FXML
  private TextField entreeNoteMoyennePizza;
  
  @FXML
  private TextField entreePrenomClient;
  
  @FXML
  private TextField entreePrixPizza;
  
  @FXML
  private TextField entreeTypePizza;
  
  @FXML
  private Label labelListeCommandes;
  
  @FXML
  private Label labelListePizzas;
  
  @FXML
  private ListView<String> listeCommandes;
  
  @FXML
  private ListView<String> listeEvaluations;
  
  @FXML
  private ListView<String> listeIngredients;
  
  @FXML
  private ChoiceBox<Integer> choiceBoxNoteEvaluation;
  
  @FXML
  private ListView<String> listePizzas;
  
  @FXML
  private StackPane panePhotoPizza;
  
  @FXML
  private TextArea texteCommentaireEvaluation;
  
  private void popupInfo(String msg) {
    Alert a = new Alert(AlertType.INFORMATION);
    a.setHeaderText(null);
    a.setContentText(msg);
    a.showAndWait();
  }
  
  private void popupErreur(String msg) {
    Alert a = new Alert(AlertType.ERROR);
    a.setHeaderText(null);
    a.setContentText(msg);
    a.showAndWait();
  }
  
  private void setChampInvalide(TextField champ) {
    champ.setStyle("-fx-border-color: red; -fx-border-width: 2;");
  }
  
  private void setChampValide(TextField champ) {
    champ.setStyle("");
  }
  
  private boolean estNomOuPrenomValide(String s) {
    if (s == null) {
      return false;
    }
    String v = s.trim();
    if (v.isEmpty()) {
      return false;
    }
    // Lettres (accents OK) + espace + ' + -
    return v.matches("[\\p{L}][\\p{L} '\\-]*");
  }
  
  private boolean verifierEmailChamp(boolean afficherPopup) {
    String email = entreeEmailClient.getText();
    boolean ok = Compte.estEmailValide(email);
    if (!ok && afficherPopup) {
      popupErreur(MSG_MAIL_INVALIDE);
      setChampInvalide(entreeEmailClient);
      entreeEmailClient.requestFocus();
    } else if (ok) {
      setChampValide(entreeEmailClient);
    }
    return ok;
  }
  
  private int lireAgeOuZeroVerifie() {
    String txt = entreeAgeClient.getText();
    if (txt == null || txt.isBlank()) {
      return 0; // age non défini
    }
    
    int age = Integer.parseInt(txt.trim());
    if (age < AGE_MIN || age > AGE_MAX) {
      throw new IllegalArgumentException(
          "Âge invalide (entre " + AGE_MIN + " et " + AGE_MAX + ")");
    }
    return age;
  }
  
  /**
   * Méthode utilitaire pour rafraîchir les champs infos. Note : InterClient ne
   * permet pas de récupérer les infos, on garde l'appel statique ici.
   */
  private void rafraichirInfosClient() {
    Client client = Compte.getClientConnecte();
    if (client != null) {
      Compte compte = client.getCompte();
      entreeNomClient.setText(compte.getNom());
      entreePrenomClient.setText(compte.getPrenom());
      entreeAdresseClient.setText(compte.getAdresse());
      entreeAgeClient.setText(String.valueOf(compte.getAge()));
      entreeAuteurEvaluation.setText(compte.getPrenom());
    }
  }
  
  /**
   * Méthode utilitaire pour mettre à jour l'affichage de la liste des pizzas.
   */
  private void majCatalogue(Set<Pizza> pizzas) {
    listePizzas.getItems().clear();
    if (pizzas != null) {
      for (Pizza p : pizzas) {
        listePizzas.getItems().add(p.getNom());
      }
      labelListePizzas.setText("Pizzas (" + pizzas.size() + ")");
    } else {
      labelListePizzas.setText("Pizzas (0)");
    }
  }
  
  @FXML
  void actionBoutonInscription(ActionEvent event) {
    
    // reset styles
    setChampValide(entreeNomClient);
    setChampValide(entreePrenomClient);
    setChampValide(entreeEmailClient);
    setChampValide(entreeAgeClient);
    
    // vérif nom/prénom UNIQUEMENT ici (sans effacer)
    if (!estNomOuPrenomValide(entreeNomClient.getText())) {
      popupErreur("Nom invalide : pas de chiffres ni caractères spéciaux");
      setChampInvalide(entreeNomClient);
      entreeNomClient.requestFocus();
      return;
    }
    if (!estNomOuPrenomValide(entreePrenomClient.getText())) {
      popupErreur("Prénom invalide : pas de chiffres ni caractères spéciaux");
      setChampInvalide(entreePrenomClient);
      entreePrenomClient.requestFocus();
      return;
    }
    
    // email
    if (!verifierEmailChamp(true)) {
      return;
    }
    
    // âge 15..120 si renseigné
    int age;
    try {
      age = lireAgeOuZeroVerifie();
      setChampValide(entreeAgeClient);
    } catch (NumberFormatException e) {
      popupErreur("Inscription impossible : âge invalide (mets un nombre).");
      setChampInvalide(entreeAgeClient);
      entreeAgeClient.requestFocus();
      return;
    } catch (IllegalArgumentException e) {
      popupErreur(e.getMessage());
      setChampInvalide(entreeAgeClient);
      entreeAgeClient.requestFocus();
      return;
    }
    
    try {
      String nom = entreeNomClient.getText().trim();
      String prenom = entreePrenomClient.getText().trim();
      String adresse = entreeAdresseClient.getText();
      
      String email = entreeEmailClient.getText();
      String mdp = entreeMotDePasseClient.getText();
      
      InformationPersonnelle info =
          new InformationPersonnelle(nom, prenom, adresse, age);
      
      int code = model.inscription(email, mdp, info);
      
      if (code == 0) {
        boolean okConnexion = model.connexion(email, mdp);
        if (!okConnexion) {
          popupErreur(
              "Inscription réussie, mais connexion automatique impossible.");
          return;
        }
        
        rafraichirInfosClient();
        popupInfo("Inscription réussie et vous êtes connecté.");
        
      } else if (code == 2) {
        popupErreur("Inscription impossible : email déjà utilisé.");
      } else {
        popupErreur("Inscription impossible : champs invalides.");
      }
    } catch (Exception e) {
      popupErreur("Inscription impossible : " + e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonConnexion(ActionEvent event) {
    
    // si déjà connecté
    if (Compte.getClientConnecte() != null) {
      popupInfo("vous ete deja connecter");
      return;
    }
    
    String email = entreeEmailClient.getText();
    String mdp = entreeMotDePasseClient.getText();
    boolean ok = model.connexion(email, mdp);
    
    if (!ok) {
      popupErreur("Connexion impossible : identifiants incorrects.");
      return;
    }
    
    rafraichirInfosClient();
    popupInfo("Connexion réussie.");
  }
  
  @FXML
  void actionBoutonDeconnexion(ActionEvent event) {
    try {
      model.deconnexion();
      
      // vider infos client
      entreeNomClient.clear();
      entreePrenomClient.clear();
      entreeAdresseClient.clear();
      entreeAgeClient.clear();
      entreeAuteurEvaluation.clear();
      
      // vider email + mdp demandés
      entreeEmailClient.clear();
      entreeMotDePasseClient.clear();
      
      setChampValide(entreeNomClient);
      setChampValide(entreePrenomClient);
      setChampValide(entreeEmailClient);
      setChampValide(entreeAgeClient);
      
      // Vider aussi les listes affichées
      listeCommandes.getItems().clear();
      listePizzas.getItems().clear();
      panePhotoPizza.getChildren().clear();
      
      popupInfo("Déconnecté.");
      
    } catch (NonConnecteException e) {
      popupErreur("Aucun client n'est connecté.");
    }
  }
  
  
  @FXML
  void actionBoutonAfficherCommandesEnCours(ActionEvent event)
      throws NonConnecteException {
    afficherCommandesEnCours();
  }
  
  @FXML
  void actionBoutonAfficherCommandesTraitees(ActionEvent event) {
    listeCommandes.getItems().clear();
    try {
      for (Commande c : model.getCommandePassees()) {
        listeCommandes.getItems().add("Commande " + c.getIdCommande());
      }
      labelListeCommandes.setText("Commandes traitées");
    } catch (NonConnecteException e) {
      popupErreur(e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonAfficherEvaluationPizzas(ActionEvent event) {
    // 1. Récupérer la pizza sélectionnée
    String nomPizza = listePizzas.getSelectionModel().getSelectedItem();
    if (nomPizza == null) {
      popupErreur("Veuillez sélectionner une pizza.");
      return;
    }
    
    Pizza p = Pizza.getPizzaParNom(nomPizza);
    if (p == null) {
      popupErreur("Pizza introuvable.");
      return;
    }
    
    // 2. Récupérer les évaluations
    Set<Evaluation> evals = model.getEvaluationsPizza(p);
    listeEvaluations.getItems().clear();
    
    if (evals.isEmpty()) {
      listeEvaluations.getItems().add("Aucune évaluation.");
    } else {
      for (Evaluation e : evals) {
        // Formatage simple : Note/5 - Commentaire (Auteur)
        String s = String.format("%d/5 : %s (%s)", e.getNote(),
            (e.getCommentaire() == null ? "" : e.getCommentaire()),
            e.getAuteur().getCompte().getPrenom());
        listeEvaluations.getItems().add(s);
      }
    }
    
    // 3. Mettre à jour la moyenne globale dans l'onglet d'évaluation
    double moy = model.getNoteMoyenne(p);
    if (moy < 0) {
      entreeEvaluationMoyenneEvaluations.setText("Aucune");
    } else {
      entreeEvaluationMoyenneEvaluations
          .setText(String.format("%.2f / 5", moy));
    }
    
    entreeNomPizzaEvaluee.setText(p.getNom());
  }
  
  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {
    Set<Pizza> pizzas = model.getPizzas();
    majCatalogue(pizzas);
  }
  
  @FXML
  void actionBoutonAjouterMonEvaluation(ActionEvent event) {
    // 1. Vérifier la sélection de la pizza
    String nomPizza = listePizzas.getSelectionModel().getSelectedItem();
    if (nomPizza == null) {
      popupErreur("Veuillez sélectionner une pizza dans la liste.");
      return;
    }
    Pizza p = Pizza.getPizzaParNom(nomPizza);
    
    // 2. Vérifier la note
    Integer note = choiceBoxNoteEvaluation.getValue();
    if (note == null) {
      popupErreur("Veuillez choisir une note (0-5).");
      return;
    }
    
    // 3. Récupérer le commentaire
    String commentaire = texteCommentaireEvaluation.getText();
    
    try {
      // 4. Appel au modèle
      boolean succes = model.ajouterEvaluation(p, note, commentaire);
      
      if (succes) {
        popupInfo("Evaluation ajoutée avec succès !");
        texteCommentaireEvaluation.clear();
        // Rafraîchir l'affichage des évaluations pour voir la sienne
        actionBoutonAfficherEvaluationPizzas(event);
        // Rafraîchir la moyenne dans l'onglet pizza
        actionSelectionPizza(null);
      } else {
        popupErreur(
            "Impossible d'ajouter l'évaluation. Vous avez peut-être déjà évalué cette pizza.");
      }
      
    } catch (NonConnecteException e) {
      popupErreur("Vous devez être connecté pour évaluer.");
    } catch (CommandeException e) {
      popupErreur("Erreur : " + e.getMessage()); // Ex: Pizza non commandée
    }
  }
  
  @FXML
  void actionBoutonAjouterPizzaSelectionneeCommande(ActionEvent event) {
    String pizzaNom = listePizzas.getSelectionModel().getSelectedItem();
    if (pizzaNom == null) {
      popupErreur("Sélectionnez une pizza.");
      return;
    }
    
    String commandeNom = listeCommandes.getSelectionModel().getSelectedItem();
    if (commandeNom == null) {
      popupErreur("Sélectionnez une commande.");
      return;
    }
    
    int idCmd;
    try {
      idCmd = Integer.parseInt(commandeNom.replace("Commande ", ""));
    } catch (NumberFormatException e) {
      popupErreur("Identifiant de commande invalide.");
      return;
    }
    
    try {
      Commande cmd = null;
      for (Commande c : model.getCommandesEncours()) {
        if (c.getIdCommande() == idCmd) {
          cmd = c;
          break;
        }
      }
      
      if (cmd == null) {
        popupErreur("Commande introuvable ou déjà validée.");
        return;
      }
      
      Pizza pizza = null;
      for (Pizza p : model.getPizzas()) {
        if (p.getNom().equals(pizzaNom)) {
          pizza = p;
          break;
        }
      }
      
      if (pizza == null) {
        popupErreur("Pizza introuvable.");
        return;
      }
      model.ajouterPizza(pizza, 1, cmd);
      popupInfo("Pizza ajoutée à la commande.");
      
    } catch (NonConnecteException e) {
      popupErreur("Vous devez être connecté : " + e.getMessage());
    } catch (CommandeException e) {
      popupErreur("Erreur commande : " + e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonAppliquerFiltreContientngredient(ActionEvent event) {
    String texte = entreeFiltreContientIngredient.getText();
    if (texte == null || texte.isBlank()) {
      popupErreur("Veuillez saisir un ou plusieurs ingrédients.");
      return;
    }
    
    String[] ingredients = texte.split("[,\\s]+");
    model.ajouterFiltre(ingredients);
    
    majCatalogue(model.selectionPizzaFiltres());
  }
  
  @FXML
  void actionBoutonAppliquerFiltrePrixMax(ActionEvent event) {
    try {
      String textePrix = entreeFiltrePrixMax.getText();
      if (textePrix == null || textePrix.isBlank()) {
        return;
      }
      double prixMax = Double.parseDouble(textePrix);
      
      model.ajouterFiltre(prixMax);
      majCatalogue(model.selectionPizzaFiltres());
      
    } catch (NumberFormatException e) {
      popupErreur("Le prix maximum doit être un nombre valide.");
      setChampInvalide(entreeFiltrePrixMax);
    } catch (IllegalArgumentException e) {
      popupErreur(e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonAppliquerFiltreType(ActionEvent event) {
    String typeSelectionne = choiceBoxFiltreType.getValue();
    if (typeSelectionne == null) {
      popupErreur("Veuillez sélectionner un type de pizza.");
      return;
    }
    
    try {
      TypePizza type = TypePizza.valueOf(typeSelectionne);
      model.ajouterFiltre(type);
      majCatalogue(model.selectionPizzaFiltres());
    } catch (IllegalArgumentException e) {
      popupErreur("Type de pizza inconnu.");
    }
  }
  
  @FXML
  void actionBoutonCreerNouvelleCommande(ActionEvent event) {
    try {
      Commande nouvelleCommande = model.debuterCommande();
      
      if (nouvelleCommande == null) {
        popupErreur("Erreur lors de la création de la commande");
        return;
      }
      
      popupInfo("Nouvelle commande créée");
      afficherCommandesEnCours();
      
      // Sélectionner automatiquement la nouvelle commande
      listeCommandes.getSelectionModel()
          .select("Commande " + nouvelleCommande.getIdCommande());
      
    } catch (NonConnecteException e) {
      popupErreur(e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonReinitialiserFiltre(ActionEvent event) {
    model.supprimerFiltres();
    
    choiceBoxFiltreType.getSelectionModel().clearSelection();
    entreeFiltreContientIngredient.clear();
    entreeFiltrePrixMax.clear();
    setChampValide(entreeFiltrePrixMax);
    
    majCatalogue(model.selectionPizzaFiltres());
  }
  
  @FXML
  void actionBoutonValiderCommandeEnCours(ActionEvent event) {
    String selection = listeCommandes.getSelectionModel().getSelectedItem();
    if (selection == null) {
      popupErreur("Sélectionnez une commande.");
      return;
    }
    
    int idCmd = Integer.parseInt(selection.replace("Commande ", ""));
    
    try {
      for (Commande c : model.getCommandesEncours()) {
        if (c.getIdCommande() == idCmd) {
          model.validerCommande(c);
          popupInfo("Commande validée");
          afficherCommandesEnCours();
          return;
        }
      }
      popupErreur("Commande introuvable.");
    } catch (NonConnecteException | CommandeException e) {
      popupErreur(e.getMessage());
    }
  }
  
  @FXML
  void actionSelectionEvaluation(MouseEvent event) {
    // Facultatif : si on clique sur une évaluation, on peut afficher l'auteur
    // ou le détail
    // Pour l'instant, l'affichage dans la liste suffit.
  }
  
  @FXML
  void actionSelectionPizza(MouseEvent event) {
    String nomPizza = listePizzas.getSelectionModel().getSelectedItem();
    if (nomPizza == null) {
      return;
    }
    
    Pizza p = Pizza.getPizzaParNom(nomPizza);
    if (p != null) {
      // Affichage infos de base
      entreeNomPizza.setText(p.getNom());
      entreeTypePizza.setText(p.getType().toString());
      entreePrixPizza.setText(String.format("%.2f €", p.getPrix()));
      
      // Affichage ingrédients
      listeIngredients.getItems().clear();
      for (Ingredient i : p.getIngredients()) {
        listeIngredients.getItems().add(i.getNom());
      }
      
      // Affichage Note Moyenne
      double moy = model.getNoteMoyenne(p);
      if (moy < 0) {
        entreeNoteMoyennePizza.setText("Aucune");
      } else {
        entreeNoteMoyennePizza.setText(String.format("%.1f / 5", moy));
      }
      
      // Affichage Photo
      panePhotoPizza.getChildren().clear();
      if (p.getPhoto() != null && !p.getPhoto().isBlank()) {
        try {
          // On essaie de charger l'image
          // Note : le chemin doit être compatible JavaFX (ex:
          // "file:images/pizza.jpg" ou ressource)
          Image img = new Image(p.getPhoto());
          ImageView iv = new ImageView(img);
          iv.setPreserveRatio(true);
          iv.setFitHeight(panePhotoPizza.getHeight());
          iv.setFitWidth(panePhotoPizza.getWidth());
          panePhotoPizza.getChildren().add(iv);
        } catch (Exception e) {
          // Erreur silencieuse si image introuvable, ou afficher un placeholder
          Label lbl = new Label("Photo introuvable");
          panePhotoPizza.getChildren().add(lbl);
        }
      }
    }
  }
  
  @FXML
  void actionSelectionCommande(MouseEvent event) {
    String selection = listeCommandes.getSelectionModel().getSelectedItem();
    if (selection == null) {
      return;
    }
    
    int idCmd = Integer.parseInt(selection.replace("Commande ", ""));
    
    try {
      for (Commande c : model.getCommandesEncours()) {
        if (c.getIdCommande() == idCmd) {
          
          listePizzas.getItems().clear();
          
          for (Pizza p : c.getPizzas()) {
            listePizzas.getItems().add(p.getNom());
          }
          
          labelListePizzas.setText("Pizzas de la commande");
          break;
        }
      }
    } catch (NonConnecteException e) {
      popupErreur(e.getMessage());
    }
  }
  
  @FXML
  void initialize() {
    choiceBoxNoteEvaluation
        .setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5));
    
    choiceBoxFiltreType.setItems(FXCollections.observableArrayList("Viande",
        "Vegetarienne", "Regionale"));
    
    // âge : chiffres seulement (0..3)
    UnaryOperator<TextFormatter.Change> ageFilter = change -> {
      String next = change.getControlNewText();
      if (next.isEmpty() || next.matches("\\d{0,3}")) {
        return change;
      }
      return null;
    };
    entreeAgeClient.setTextFormatter(new TextFormatter<>(ageFilter));
  }
}
