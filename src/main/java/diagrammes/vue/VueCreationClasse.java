package diagrammes.vue;

import diagrammes.classe.Attribut;
import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class VueCreationClasse {
    private Classe nouvelleClasse;
    private final List<Attribut> attributs = new ArrayList<>();
    private final List<Methode> methodes = new ArrayList<>();

    public Classe afficher() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Section pour le nom de la classe
        Label lblNomClasse = new Label("Nom de la classe :");
        TextField txtNomClasse = new TextField();

        // Section pour les attributs
        Label lblAttributs = new Label("Ajouter des attributs :");
        TextField txtNomAttribut = new TextField();
        txtNomAttribut.setPromptText("Nom de l'attribut");

        ComboBox<String> comboTypeAttribut = new ComboBox<>();
        comboTypeAttribut.getItems().addAll("String", "int", "double", "boolean", "float");
        comboTypeAttribut.setPromptText("Type");

        ComboBox<String> comboModificateurAttribut = new ComboBox<>();
        comboModificateurAttribut.getItems().addAll("private", "public", "protected");
        comboModificateurAttribut.setPromptText("Modificateur");

        Button btnAjouterAttribut = new Button("Ajouter l'attribut");
        ListView<String> listAttributs = new ListView<>();
        listAttributs.setPrefHeight(100); // Ajuster la hauteur de la liste pour ne pas trop déborder.

        btnAjouterAttribut.setOnAction(e -> {
            String nom = txtNomAttribut.getText();
            String type = comboTypeAttribut.getValue();
            String modificateur = comboModificateurAttribut.getValue();

            if (nom != null && !nom.isEmpty() && type != null && modificateur != null) {
                listAttributs.getItems().add(modificateur + " " + type + " " + nom);
                txtNomAttribut.clear();
                comboTypeAttribut.setValue(null);
                comboModificateurAttribut.setValue(null);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs pour ajouter un attribut.");
                alert.showAndWait();
            }
        });

        // Section pour les méthodes
        Label lblMethodes = new Label("Ajouter des méthodes :");
        TextField txtNomMethode = new TextField();
        txtNomMethode.setPromptText("Nom de la méthode");

        ComboBox<String> comboRetourMethode = new ComboBox<>();
        comboRetourMethode.getItems().addAll("void", "String", "int", "double", "boolean", "float");
        comboRetourMethode.setPromptText("Type de retour");

        ComboBox<String> comboModificateurMethode = new ComboBox<>();
        comboModificateurMethode.getItems().addAll("private", "public", "protected");
        comboModificateurMethode.setPromptText("Modificateur");

        TextField txtParametres = new TextField();
        txtParametres.setPromptText("Paramètres (séparés par des virgules)");

        Button btnAjouterMethode = new Button("Ajouter la méthode");
        ListView<String> listMethodes = new ListView<>();
        listMethodes.setPrefHeight(100); // Ajuster la hauteur de la liste.

        btnAjouterMethode.setOnAction(e -> {
            String nom = txtNomMethode.getText();
            String retour = comboRetourMethode.getValue();
            String modificateur = comboModificateurMethode.getValue();
            String parametres = txtParametres.getText();

            if (nom != null && !nom.isEmpty() && retour != null && modificateur != null) {
                listMethodes.getItems().add(modificateur + " " + retour + " " + nom + "(" + parametres + ")");
                txtNomMethode.clear();
                comboRetourMethode.setValue(null);
                comboModificateurMethode.setValue(null);
                txtParametres.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs pour ajouter une méthode.");
                alert.showAndWait();
            }
        });

        // Bouton pour valider la création de la classe
        Button btnValider = new Button("Valider");
        btnValider.setOnAction(e -> stage.close());

        // Organisation des éléments dans le layout
        root.getChildren().addAll(
                lblNomClasse, txtNomClasse,
                lblAttributs, new HBox(10, txtNomAttribut, comboTypeAttribut, comboModificateurAttribut, btnAjouterAttribut), listAttributs,
                lblMethodes, new HBox(10, txtNomMethode, comboRetourMethode, comboModificateurMethode, txtParametres, btnAjouterMethode), listMethodes,
                btnValider
        );

        // Affichage de la scène
        Scene scene = new Scene(root, 700, 600); // Fixer une taille initiale pour la fenêtre
        stage.setScene(scene);
        stage.setTitle("Créer une classe");
        stage.setResizable(false); // Empêcher le redimensionnement pour un meilleur contrôle
        stage.showAndWait();

        // Création de la classe à partir des données saisies
        if (!txtNomClasse.getText().isEmpty()) {
            Classe classe = new Classe(txtNomClasse.getText());

            // Ajouter les attributs
            for (String attribut : listAttributs.getItems()) {
                String[] parts = attribut.split(" ");
                classe.ajouterAttribut(new Attribut(parts[2], parts[1], parts[0]));
            }

            // Ajouter les méthodes
            for (String methode : listMethodes.getItems()) {
                String[] parts = methode.split(" ");
                String modificateur = parts[0];
                String retour = parts[1];
                String nom = parts[2];
                String parametres = methode.substring(methode.indexOf("(") + 1, methode.indexOf(")"));
                List<String> parametresListe = parametres.isEmpty() ? new ArrayList<>() : List.of(parametres.split(","));
                classe.ajouterMethode(new Methode(nom, retour, parametresListe, modificateur));
            }

            return classe;
        }
        return null;
    }

    private Attribut afficherFormulaireAttribut() {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom de l'attribut");

        TextField typeField = new TextField();
        typeField.setPromptText("Type de l'attribut");

        ComboBox<String> modificateurBox = new ComboBox<>();
        modificateurBox.getItems().addAll("private", "public", "protected");
        modificateurBox.setPromptText("Modificateur d'accès");

        Button valider = new Button("Valider");
        valider.setOnAction(e -> stage.close());

        layout.getChildren().addAll(
                new Label("Nom :"), nomField,
                new Label("Type :"), typeField,
                new Label("Modificateur :"), modificateurBox,
                valider
        );

        stage.setScene(new Scene(layout, 300, 200));
        stage.showAndWait();

        return (nomField.getText() != null && typeField.getText() != null && modificateurBox.getValue() != null)
                ? new Attribut(nomField.getText(), typeField.getText(), modificateurBox.getValue())
                : null;
    }

    private Methode afficherFormulaireMethode() {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom de la méthode");

        TextField retourField = new TextField();
        retourField.setPromptText("Type de retour");

        ComboBox<String> modificateurBox = new ComboBox<>();
        modificateurBox.getItems().addAll("private", "public", "protected");
        modificateurBox.setPromptText("Modificateur");

        Button valider = new Button("Valider");
        valider.setOnAction(e -> stage.close());

        layout.getChildren().addAll(
                new Label("Nom :"), nomField,
                new Label("Type de retour :"), retourField,
                new Label("Modificateur :"), modificateurBox,
                valider
        );

        stage.setScene(new Scene(layout, 300, 200));
        stage.showAndWait();

        return (nomField.getText() != null && retourField.getText() != null && modificateurBox.getValue() != null)
                ? new Methode(nomField.getText(), retourField.getText(), new ArrayList<>(), modificateurBox.getValue())
                : null;
    }
}

