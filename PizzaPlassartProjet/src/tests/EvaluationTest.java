package tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Client;
import pizzas.Compte;
import pizzas.Evaluation;
import pizzas.InformationPersonnelle;

/**
 * Tests JUnit 5 de la classe {@link pizzas.Evaluation}.
 *
 * @author Kevin SIDER
 */
public class EvaluationTest {
  
  private Client client;
  
  /**
   * Initialisation avant chaque test. Crée un client valide.
   */
  @BeforeEach
  public void setUp() {
    InformationPersonnelle info =
        new InformationPersonnelle("TILLY", "Stéphane", "Brest", 25);
    Compte compte = new Compte("s.tilly@gmail.com", "password123", info);
    this.client = new Client(compte);
  }
  
  /**
   * Teste que l'évaluation avec un commentaire est bien crée.
   */
  @Test
  public void testConstructeurAvecCommentaire() {
    Evaluation eval =
        new Evaluation(4, "La meilleur pizza de Brest !", this.client);
    
    assertEquals(4, eval.getNote());
    assertEquals("La meilleur pizza de Brest !", eval.getCommentaire());
    assertEquals(this.client, eval.getAuteur());
  }
  
  /**
   * Teste que l'évaluation sans commentaire est bien crée.
   */
  @Test
  public void testConstructeurSansCommentaire() {
    Evaluation eval = new Evaluation(3, this.client);
    assertEquals(3, eval.getNote());
    assertEquals("", eval.getCommentaire(),
        "Le commentaire devrait être vide par défaut");
    assertEquals(this.client, eval.getAuteur());
  }
  
  /**
   * Teste qu'une exception est bien lancée si le commentaire est "null".
   */
  @Test
  public void testConstructeurCommentaireNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(5, null, this.client));
  }
  
  @Test
  public void testConstructeurNotesLimites() {
    assertDoesNotThrow(() -> new Evaluation(0, this.client));
    assertDoesNotThrow(() -> new Evaluation(5, this.client));
  }
  
  /**
   * Teste qu'une exception est bien lancée si la note de l'évaluation est
   * inférieure à la note minimale.
   */
  @Test
  public void testConstructeurNoteInvalideInferieure() {
    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(-1, this.client));
  }
  
  /**
   * Teste qu'une exception est bien lancée si la note de l'évaluation est
   * supérieure à la note maximale.
   */
  @Test
  public void testConstructeurNoteInvalideSuperieure() {
    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(6, this.client));
  }
  
  /**
   * Teste qu'une exception est bien lancée si il n'y a pas d'auteur (client
   * associé).
   */
  @Test
  public void testConstructeurAuteurNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Evaluation(4, "Cette pizza est incroyable !", null));
  }
  
  /**
   * Teste que l'overriding de la méthode toString() fonctionne.
   */
  @Test
  public void testToString() {
    // Cas 1 : Avec commentaire
    Evaluation evalAvecCommentaire =
        new Evaluation(4, "Incroyable pizza", this.client);
    String resultatAttendu1 =
        "Evaluation de : Stéphane TILLY : 4/5 Incroyable pizza";
    assertEquals(resultatAttendu1, evalAvecCommentaire.toString());
    
    // Cas 2 : Sans commentaire
    Evaluation evalSansCommentaire = new Evaluation(5, this.client);
    String resultatAttendu2 = "Evaluation de : Stéphane TILLY : 5/5 ";
    assertEquals(resultatAttendu2, evalSansCommentaire.toString());
  }
}
