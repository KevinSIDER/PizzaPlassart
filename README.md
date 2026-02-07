# PizzaPlassart - Application de Pizzeria

**PizzaPlassart** est une application de gestion de pizzeria développée en **Java** utilisant la bibliothèque graphique **JavaFX**. Réalisé par un **groupe de 4 étudiants**, ce projet implémente une architecture **MVC** (Modèle-Vue-Contrôleur) et sépare distinctement l'interface client (commande, filtres) de l'interface d'administration (gestion des stocks, statistiques).

| Interface Client | Interface Pizzaïolo |
| :---: | :---: |
| ![Vue Client](https://github.com/KevinSIDER/PizzaPlassart/blob/main/Illustration_client.JPG) | ![Vue Admin](https://github.com/KevinSIDER/PizzaPlassart/blob/main/Illustration_admin.JPG) |

---

## 📋 Fonctionnalités

L'application propose deux profils d'utilisation distincts :

### Module Client
- **Catalogue dynamique :** Visualisation des pizzas avec ingrédients et prix.
- **Système de filtres :** Recherche par type (Végétarienne/Viande), par prix maximum, ou par inclusion/exclusion d'ingrédients.
- **Prise de commande :** Ajout au panier et validation de commande en temps réel.
- **Gestion de compte :** Inscription, connexion et consultation de l'historique.
- **Évaluations :** Notation et commentaires sur les pizzas commandées.

### Module Pizzaïolo (Admin)
- **Monitoring des commandes :** Suivi des commandes "En cours" et validation vers l'état "Traitée".
- **Création de produits :** Interface de création de nouvelles pizzas à partir des ingrédients disponibles.
- **Statistiques métier :**
  - Calcul du chiffre d'affaires.
  - Identification de la pizza la plus/moins vendue.
  - Analyse de l'ingrédient favori des clients.

---

## 📂 Architecture du Dépôt

Le projet est organisé en deux dossiers principaux à la racine :

- **`PizzaPlassartApp/`** : Contient la version **livrable** (exécutable) pour l'utilisateur final.
- **`PizzaPlassartProjet/`** : Contient le **code source**, les ressources de développement et les tests.

```text
📦 Racine du projet
 ├── 📂 PizzaPlassartApp       # Dossier de déploiement
 │    ├── 📄 PizzaPlassart.jar      # L'archive Java exécutable
 │    └── 📄 donnees_pizzeria.txt   # Base de données (Persistance)
 │
 ├── 📂 PizzaPlassartProjet    # Dossier de développement (Eclipse/IntelliJ)
 │    ├── 📂 src                    # Code source (MVC)
 │    │    ├── 📦 pizzas            # Modèle (M) : Logique métier
 │    │    ├── 📦 ui                # Vue et Contrôleur (V & C) : JavaFX
 │    │    ├── 📦 io                # Gestion des fichiers et sauvegarde
 │    │    └── 📦 tests             # Tests unitaires JUnit 
 │    └── 📂 doc                    # Documentation Javadoc
 │
 └── 📄 README.md

```

## 🚀 Installation et Lancement

### ✅ Prérequis
* **Java 21** (ou version supérieure)
* **JavaFX SDK** (si non inclus dans votre JDK)

### ▶️ Lancer l’application
1.  Ouvrez un terminal dans le dossier `PizzaPlassartApp`
2.  Vérifiez que le fichier `donnees_pizzeria.txt` est bien présent à côté du `.jar`
3.  Exécutez la commande suivante (en adaptant le chemin vers JavaFX) :

```bash
# Exemple sous Windows
java --module-path "C:\Chemin\Vers\javafx-sdk-21\lib" \
--add-modules javafx.controls,javafx.fxml \
-jar PizzaPlassart.jar

# Exemple sous Linux / Mac
java --module-path "/Chemin/Vers/javafx-sdk-21/lib" \
--add-modules javafx.controls,javafx.fxml \
-jar PizzaPlassart.jar
```
---

## 🧠 Détails Techniques – Format des données

La persistance est assurée par un fichier texte structuré : `📄 donnees_pizzeria.txt`
**Séparateur utilisé :** `;`

### 📑 Structure des données

| Type d’objet | Format dans le fichier |
| :--- | :--- |
| **Ingrédient** | `INGREDIENT;Nom;Prix` |
| **Pizza** | `PIZZA;Nom;Type;Prix;Image;Base;Ingrédient1;Ingrédient2...` |
| **Client** | `CLIENT;Email;Mdp;Nom;Prénom;Adresse;Age` |
| **Commande** | `COMMANDE;ID;Date;Client;Statut;Pizza1;Pizza2...` |
