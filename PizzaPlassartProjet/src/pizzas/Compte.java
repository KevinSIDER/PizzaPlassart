package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Représente les informations de connexion d'un client (email + mot de passe)
 * ainsi que ses informations personnelles.
 */
public final class Compte implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
  
  /** Email stocké normalisé (trim + lower). */
  private final String email;
  
  private final String mdp;
  private final InformationPersonnelle infoPersonnelle;
  
  /** Clients enregistrés (clé = email normalisé). */
  private static final Map<String, Client> clientsParEmail = new HashMap<>();
  
  /** Le seul client connecté à un instant donné (ou null). */
  private static Client clientConnecte = null;
  
  /**
   * Liste statique de tous les comptes créés.
   */
  @SuppressWarnings("unused")
  private static List<Compte> comptes = new ArrayList<>();
  
  /**
   * Inscrit un nouveau client.
   *
   * @param email email
   * @param mdp mot de passe
   * @param infoPersonnelle informations personnelles
   * @return 0 si OK, 1 si données invalides, 2 si email déjà utilisé
   */
  public static int inscription(String email, String mdp,
      InformationPersonnelle infoPersonnelle) {
    
    if (email == null || email.isBlank() || !estEmailValide(email)) {
      return 1;
    }
    if (mdp == null || mdp.isBlank() || infoPersonnelle == null) {
      return 1;
    }
    
    String key = normaliserEmail(email);
    if (clientsParEmail.containsKey(key)) {
      return 2;
    }
    
    Compte compte = new Compte(email, mdp, infoPersonnelle);
    clientsParEmail.put(key, new Client(compte));
    return 0;
  }
  
  /**
   * Connecte un client si email+mdp corrects.
   *
   * @param email email
   * @param mdp mot de passe
   * @return true si connecté, false sinon
   */
  public static boolean connexion(String email, String mdp) {
    if (email == null || mdp == null) {
      return false;
    }
    if (!estEmailValide(email)) {
      return false;
    }
    
    String key = normaliserEmail(email);
    Client c = clientsParEmail.get(key);
    if (c == null) {
      return false;
    }
    
    if (!c.getCompte().correspondaLogin(email, mdp)) {
      return false;
    }
    
    clientConnecte = c;
    return true;
  }
  
  /**
   * Déconnecte le client courant.
   *
   * @return true si un client a été déconnecté, false si personne n’était
   *         connecté
   */
  public static boolean deconnexion() {
    if (clientConnecte == null) {
      return false;
    }
    clientConnecte = null;
    return true;
  }
  
  /**
   * Retourne le client associé à ce compte.
   */
  public Client getClient() {
    String key = normaliserEmail(this.email);
    return clientsParEmail.get(key);
  }
  
  /**
   * Renvoie le client actuellement connecté.
   *
   * @return client connecté ou null
   */
  public static Client getClientConnecte() {
    return clientConnecte;
  }
  
  /**
   * Retourne tous les comptes enregistrés.
   */
  public static List<Compte> getComptes() {
    List<Compte> listeComptes = new ArrayList<>();
    for (Client client : clientsParEmail.values()) {
      listeComptes.add(client.getCompte());
    }
    return listeComptes;
  }
  
  /**
   * Vide toutes les données en mémoire (utile pour tests/démo).
   */
  public static void resetMemoire() {
    clientsParEmail.clear();
    clientConnecte = null;
  }
  
  /* -------------------- Partie “objet Compte” -------------------- */
  
  /**
   * Crée un compte avec email, mot de passe et informations personnelles.
   *
   * @param email email
   * @param mdp mot de passe
   * @param infoPersonnelle informations personnelles
   */
  public Compte(String email, String mdp,
      InformationPersonnelle infoPersonnelle) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("email null ou vide");
    }
    if (!estEmailValide(email)) {
      throw new IllegalArgumentException("email invalide : " + email);
    }
    if (mdp == null || mdp.isBlank()) {
      throw new IllegalArgumentException("mot de passe null ou vide");
    }
    if (infoPersonnelle == null) {
      throw new IllegalArgumentException("informations personnelles null");
    }
    this.email = normaliserEmail(email);
    this.mdp = mdp;
    this.infoPersonnelle = infoPersonnelle;
  }
  
  /**
   * Renvoie l'email normalisé du compte.
   *
   * @return email normalisé
   */
  public String getEmail() {
    return email;
  }
  
  /**
   * Renvoie les informations personnelles du client.
   *
   * @return informations personnelles
   */
  public InformationPersonnelle getInfoPersonnelle() {
    return infoPersonnelle;
  }
  
  /**
   * Renvoie le nom du client.
   *
   * @return nom
   */
  public String getNom() {
    return infoPersonnelle.getNom();
  }
  
  /**
   * Renvoie le prénom du client.
   *
   * @return prénom
   */
  public String getPrenom() {
    return infoPersonnelle.getPrenom();
  }
  
  /**
   * Renvoie l'adresse du client.
   *
   * @return adresse
   */
  public String getAdresse() {
    return infoPersonnelle.getAdresse();
  }
  
  /**
   * Renvoie l'âge du client.
   *
   * @return âge
   */
  public int getAge() {
    return infoPersonnelle.getAge();
  }
  
  /**
   * Vérifie le mot de passe saisi (comparaison simple pour le projet).
   *
   * @param mdpSaisi mot de passe saisi
   * @return true si identique, false sinon
   */
  public boolean verifierMotDePasse(String mdpSaisi) {
    return Objects.equals(this.mdp, mdpSaisi);
  }
  
  /**
   * Méthode pratique pour le contrôleur JavaFX : true si (email + mdp)
   * correspondent au compte.
   *
   * @param emailSaisi email saisi
   * @param mdpSaisi mot de passe saisi
   * @return true si correspond, false sinon
   */
  public boolean correspondaLogin(String emailSaisi, String mdpSaisi) {
    if (emailSaisi == null || mdpSaisi == null) {
      return false;
    }
    if (!estEmailValide(emailSaisi)) {
      return false;
    }
    return this.email.equals(normaliserEmail(emailSaisi))
        && verifierMotDePasse(mdpSaisi);
  }
  
  /**
   * Renvoie le mot de passe (utile seulement si ton code en a besoin).
   *
   * @return mot de passe
   */
  public String getMotDePasse() {
    return mdp;
  }
  
  /**
   * Normalise un email (trim + minuscules) pour servir de clé.
   *
   * @param email email à normaliser
   * @return email normalisé
   */
  public static String normaliserEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }
  
  /**
   * Vérifie si un email est valide.
   *
   * @param email email à vérifier
   * @return true si valide, false sinon
   */
  public static boolean estEmailValide(String email) {
    if (email == null) {
      return false;
    }
    return EMAIL_PATTERN.matcher(email.trim()).matches();
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
  
  /** Unicité basée sur l’email (id de connexion). */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Compte other = (Compte) obj;
    return Objects.equals(email, other.email);
  }
  
  @Override
  public String toString() {
    // Utile si tu affiches le compte/client dans une liste côté IHM.
    return getNom() + " " + getPrenom() + " <" + email + ">";
  }
  
  /** Méthode pour avoir tout les clients, pour la sauvegarde. */
  public static java.util.Collection<Client> getTousLesClients() {
    // On crée une NOUVELLE liste qui contient les mêmes éléments
    return new java.util.ArrayList<>(clientsParEmail.values());
  }
}
