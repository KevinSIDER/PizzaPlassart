package tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Client;
import pizzas.Commande;
import pizzas.Compte;
import pizzas.InformationPersonnelle;


/**
 * Tests JUnit de la classe {pizzas.Client}.
 *
 * @author Yaouanc kevin
 */
public class TestClient {
  
  /**
   * Compte utilisÃ© pour instancier un client de test.
   */
  private Compte compte;
  
  /**
   * Instance de {pizzas.Client} testÃ©e.
   */
  private Client client;
  
  /**
   * PrÃ©pare un compte et un client neufs avant chaque test.
   */
  @BeforeEach
  void setUp() {
    compte = creerCompteDeTest();
    client = new Client(compte);
  }
  
  /**
   * VÃ©rifie le constructeur : un compte null est refusÃ© et le compte fourni
   * est conservÃ©.
   */
  @Test
  void testConstructeur() {
    assertThrows(IllegalArgumentException.class, () -> new Client(null));
    assertEquals(compte, client.getCompte());
  }
  
  /**
   * VÃ©rifie que le client expose les informations personnelles de son compte.
   */
  @Test
  void testGetInfoPersonnelle() {
    assertEquals(compte.getInfoPersonnelle(), client.getInfoPersonnelle());
  }
  
  /**
   * VÃ©rifie la crÃ©ation d'une commande et la rÃ©cupÃ©ration par identifiant.
   */
  @Test
  void testNouvelleCommande_etGetCommandesParId() {
    Commande cmd = client.nouvelleCommande();
    assertNotNull(cmd);
    assertEquals(cmd, client.getCommandes(cmd.getIdCommande()));
    assertNull(client.getCommandes(-1));
  }
  
  /**
   * VÃ©rifie l'ajout et la suppression d'une commande dans le client.
   */
  @Test
  void testAjouterRetirerCommande() {
    Commande cmd = client.nouvelleCommande();
    int id = cmd.getIdCommande();
    
    // Retirer
    client.retirerCommande(cmd);
    assertNull(client.getCommandes(id));
    
    // RÃ©-ajouter
    client.ajouterCommande(cmd);
    assertEquals(cmd, client.getCommandes(id));
  }
  
  /**
   * VÃ©rifie que l'ajout d'une commande null.
   */
  @Test
  void testAjouterCommandeNull_neCassePas() {
    Commande cmd = client.nouvelleCommande();
    int id = cmd.getIdCommande();
    
    client.ajouterCommande(null); // doit juste ignorer
    assertEquals(cmd, client.getCommandes(id));
  }
  
  /**
   * VÃ©rifie que dÃ©buter une commande crÃ©e et enregistre une commande sans
   * exception.
   */
  @Test
  void testDebuterCommande() {
    assertDoesNotThrow(() -> {
      Commande cmd = client.debuterCommande();
      assertNotNull(cmd);
      assertEquals(cmd, client.getCommandes(cmd.getIdCommande()));
    });
  }
  
  /**
   * VÃ©rifie la prÃ©sence d'un gestionnaire de commandes.
   */
  @Test
  void testGetGestionnaireCommande() {
    assertNotNull(client.getGestionnaireCommande());
  }
  
  /**
   * VÃ©rifie que l'Ã©valuation d'une pizza null est refusÃ©e.
   */
  @Test
  void testPeutEvaluerPizzaNull() {
    assertFalse(client.peutEvaluerPizza(null));
  }
  
  /**
   * VÃ©rifie que la validation d'une commande null est refusÃ©e.
   */
  @Test
  void testValiderCommandeNull() {
    assertThrows(IllegalArgumentException.class,
        () -> client.validerCommande(null));
  }
  
  /**
   * VÃ©rifie les contrats equals/hashCode et une reprÃ©sentation toString
   * minimale.
   */
  @Test
  void testEqualsHashCodeToString_base() {
    Client memeCompte = new Client(compte);
    
    assertEquals(client, memeCompte);
    assertEquals(client.hashCode(), memeCompte.hashCode());
    
    assertNotEquals(client, null);
    assertNotEquals(client, "Client");
    
    assertTrue(client.toString().startsWith("Client("));
  }
  
  /**
   * Fabrique un {pizzas.Compte} utilisable pour les tests. Cette mÃ©thode
   * essaye plusieurs combinaisons d'informations personnelles et de couples
   * login/mot de passe. Elle tente ensuite d'instancier {pizzas.Compte} via
   * rÃ©flexion afin de rester compatible avec d'Ã©ventuelles variantes de
   * constructeur.
   */
  private static Compte creerCompteDeTest() {
    
    List<InformationPersonnelle> infos = new ArrayList<>();
    addInfoIfPossible(infos, "Vador", "Dark", "1 rue de Test, 29000 Quimper",
        30);
    
    if (infos.isEmpty()) {
      fail("Impossible de crÃ©er InformationPersonnelle");
    }
    
    String[] logins = {"test@test.fr", "vador@test.fr", "client@test.fr", "test", "client"};
    String[] mdps = {"Test1234!", "Azerty123!", "MotDePasse123!", "testtesttest", "test"};
    
    List<Constructor<?>> ctors = Arrays.asList(Compte.class.getConstructors());
    ctors.sort(Comparator.comparingInt(Constructor::getParameterCount));
    
    Exception last = null;
    
    for (InformationPersonnelle info : infos) {
      for (String login : logins) {
        for (String mdp : mdps) {
          
          // Essai (login, mdp, info) puis (mdp, login, info) au cas oÃ¹
          // lâ€™ordre soit diffÃ©rent
          Object[][] essais =
              new Object[][] {{login, mdp, info}, {mdp, login, info}};
          
          for (Object[] args : essais) {
            for (Constructor<?> ctor : ctors) {
              try {
                Class<?>[] p = ctor.getParameterTypes();
                if (p.length != 3 || p[1] != String.class
                    || p[2] != InformationPersonnelle.class) {
                  continue;
                }
                
                return (Compte) ctor.newInstance(args);
              } catch (Exception e) {
                last = e;
              }
            }
          }
        }
      }
    }
    
    String constructors = Arrays.stream(Compte.class.getConstructors())
        .map(c -> c.getName() + "("
            + Arrays.stream(c.getParameterTypes()).map(Class::getSimpleName)
                .collect(Collectors.joining(", "))
            + ") " + Modifier.toString(c.getModifiers()))
        .collect(Collectors.joining("\n"));
    
    String lastMsg = (last == null) ? "(aucune exception)"
        : (last.getClass().getSimpleName() + " : " + last.getMessage());
    
    fail("Impossible de crÃ©er un Compte\n" + constructors
        + "\nDerniÃ¨re exception : " + lastMsg);
    return null;
  }
  
  /**
   * Ajoute une {pizzas.InformationPersonnelle} Ã la liste si le constructeur
   * l'accepte.
   *
   * @param infos liste Ã enrichir
   * @param nom nom
   * @param prenom prÃ©nom
   * @param adr adresse
   * @param age Ã¢ge
   */
  private static void addInfoIfPossible(List<InformationPersonnelle> infos,
      String nom, String prenom, String adr, int age) {
    try {
      infos.add(new InformationPersonnelle(nom, prenom, adr, age));
    } catch (Exception e) {
      // Le test
    }
  }
}
