# SAE_3.01_DiagrammesClasse
# 📌 Générateur Automatique de Diagrammes UML 📌  

## 📝 Description  
Ce projet a été réalisé dans le cadre d’une **SAE (Situation d’Apprentissage et d’Évaluation)** en groupe de **5 étudiants**. L’objectif était de développer une application capable de générer des diagrammes UML de classes à partir de code Java, avec une interface intuitive et des fonctionnalités avancées de manipulation et d’export.

L’application permet d’importer un package Java, d’analyser sa structure grâce à l’introspection, et de générer un diagramme UML interactif avec diverses options de personnalisation.

---

## 🎯 **Fonctionnalités principales**

### 🔍 **Analyse et Introspection**
- Analyse dynamique des classes Java d’un package.
- Extraction des attributs, méthodes et relations (héritage, association, composition).
- Gestion des classes `abstract` et `static`.

### 🎨 **Affichage et Manipulation du Diagramme**
- **Visualisation UML** avec affichage des classes, attributs et méthodes.
- **Drag and Drop** amélioré pour organiser les classes sur le canevas.
- **Gestion du clic droit** :
  - Suppression d’une classe.
  - Masquage/affichage des attributs, méthodes et relations.
- **Mouvement fluide des classes** pour éviter les croisements des flèches.
- **Personnalisation visuelle** (ajout de couleurs, ajustement des dimensions des classes).
- **Suppression et réinitialisation des diagrammes**.

### 🖥️ **Génération et Export**
- **Export du diagramme en image** (PNG, JPEG).
- **Conversion du diagramme UML en code source Java** (génération automatique des squelettes de classes).
- **Création de répertoires** pour organiser les fichiers générés.

### 🛠️ **Optimisation et Robustesse**
- Gestion des **exceptions** pour éviter les erreurs lors de l’analyse des packages.
- **Tests unitaires** pour garantir la fiabilité des fonctionnalités.
- **Correction de bugs** liés aux flèches, relations et affichages.

### 📄 **Préparation et Documentation**
- Rédaction du **rapport final** détaillant la conception, les choix techniques et les tests.
- Création d’un **diaporama de présentation** du projet.

---

## 🚀 **Technologies utilisées**
- **Langage :** Java  
- **Framework UI :** JavaFX  
- **Modèle d’architecture :** MVC (Modèle-Vue-Contrôleur)  
- **Introspection Java :** `java.lang.reflect`  
- **Export d’images :** API JavaFX  
- **Gestion des tests :** JUnit  

---

## 🎬 **Installation et Exécution**
### 📥 **Pré-requis**
- Java **17+**
- JavaFX **11+**
- Un IDE compatible (IntelliJ IDEA, Eclipse, NetBeans)
