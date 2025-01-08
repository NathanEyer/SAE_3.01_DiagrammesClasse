package diagrammes.vue;

import diagrammes.classe.Attribut;
import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class VueModifier {

    private Classe classe;

    public VueModifier(Classe classe) {
        this.classe = classe;
    }

    public Classe afficher() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Section pour modifier le nom de la classe
        Label lblNomClasse = new Label("Nom de la classe :");
        TextField txtNomClasse = new TextField(classe.getNom());

        // Section pour les attributs
        Label lblAttributs = new Label("Attributs :");
        ListView<String> listAttributs = new ListView<>();
        for (Attribut attribut : classe.getAttributs()) {
            listAttributs.getItems().add(formatAttribut(attribut));
        }
        listAttributs.setPrefHeight(100);

        Button btnModifierAttribut = new Button("Modifier Attribut");
        Button btnSupprimerAttribut = new Button("Supprimer Attribut");
        Button btnAjouterAttribut = new Button("Ajouter Attribut");

        btnModifierAttribut.setOnAction(e -> modifierAttribut(listAttributs));
        btnSupprimerAttribut.setOnAction(e -> {
            String selectedAttribut = listAttributs.getSelectionModel().getSelectedItem();
            if (selectedAttribut != null) {
                listAttributs.getItems().remove(selectedAttribut);
            }
        });
        btnAjouterAttribut.setOnAction(e -> ajouterAttribut(listAttributs));

        // Section pour les méthodes
        Label lblMethodes = new Label("Méthodes :");
        ListView<String> listMethodes = new ListView<>();
        for (Methode methode : classe.getMethodes()) {
            listMethodes.getItems().add(formatMethode(methode));
        }
        listMethodes.setPrefHeight(100);

        Button btnModifierMethode = new Button("Modifier Méthode");
        Button btnSupprimerMethode = new Button("Supprimer Méthode");
        Button btnAjouterMethode = new Button("Ajouter Méthode");

        btnModifierMethode.setOnAction(e -> modifierMethode(listMethodes));
        btnSupprimerMethode.setOnAction(e -> {
            String selectedMethode = listMethodes.getSelectionModel().getSelectedItem();
            if (selectedMethode != null) {
                listMethodes.getItems().remove(selectedMethode);
            }
        });
        btnAjouterMethode.setOnAction(e -> ajouterMethode(listMethodes));

        // Bouton de validation
        Button btnValider = new Button("Valider");
        btnValider.setOnAction(e -> {
            classe.setNom(txtNomClasse.getText());

            // Mettre à jour les attributs
            classe.getAttributs().clear();
            for (String attributString : listAttributs.getItems()) {
                String[] parts = attributString.split(" ");
                String modificateur = parts[0];
                String type = parts[1];
                String nom = parts[2];
                classe.ajouterAttribut(new Attribut(nom, type, modificateur));
            }

            // Mettre à jour les méthodes
            classe.getMethodes().clear();
            for (String methodeString : listMethodes.getItems()) {
                String[] parts = methodeString.split(" ");
                String modificateur = parts[0];
                String retour = parts[1];
                String nom = parts[2];
                String parametres = methodeString.substring(methodeString.indexOf("(") + 1, methodeString.indexOf(")"));
                List<String> parametresListe = parametres.isEmpty() ? new ArrayList<>() : List.of(parametres.split(","));
                classe.ajouterMethode(new Methode(nom, retour, parametresListe, modificateur));
            }

            stage.close();
        });

        // Organisation des éléments dans le layout
        root.getChildren().addAll(
                lblNomClasse, txtNomClasse,
                lblAttributs, new HBox(10, listAttributs, btnModifierAttribut, btnSupprimerAttribut, btnAjouterAttribut),
                lblMethodes, new HBox(10, listMethodes, btnModifierMethode, btnSupprimerMethode, btnAjouterMethode),
                btnValider
        );

        Scene scene = new Scene(root, 800, 600); // Taille ajustée pour afficher tous les éléments
        stage.setScene(scene);
        stage.setTitle("Modifier une classe");
        stage.showAndWait();

        return classe;
    }

    private void modifierAttribut(ListView<String> listAttributs) {
        String selectedAttribut = listAttributs.getSelectionModel().getSelectedItem();
        if (selectedAttribut != null) {
            String[] parts = selectedAttribut.split(" ");
            String modificateur = parts[0];
            String type = parts[1];
            String nom = parts[2];

            // Fenêtre pour modifier l'attribut
            Stage attributStage = new Stage();
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            TextField txtNom = new TextField(nom);
            ComboBox<String> comboType = new ComboBox<>();
            comboType.getItems().addAll("String", "int", "double", "boolean", "float");
            comboType.setValue(type);

            ComboBox<String> comboModificateur = new ComboBox<>();
            comboModificateur.getItems().addAll("private", "public", "protected");
            comboModificateur.setValue(modificateur);

            Button btnValider = new Button("Valider");
            btnValider.setOnAction(e -> {
                listAttributs.getItems().set(
                        listAttributs.getSelectionModel().getSelectedIndex(),
                        comboModificateur.getValue() + " " + comboType.getValue() + " " + txtNom.getText()
                );
                attributStage.close();
            });

            root.getChildren().addAll(new Label("Modifier l'attribut"), txtNom, comboType, comboModificateur, btnValider);
            Scene scene = new Scene(root, 300, 200);
            attributStage.setScene(scene);
            attributStage.showAndWait();
        }
    }

    private void modifierMethode(ListView<String> listMethodes) {
        String selectedMethode = listMethodes.getSelectionModel().getSelectedItem();
        if (selectedMethode != null) {
            String[] parts = selectedMethode.split(" ");
            String modificateur = parts[0];
            String retour = parts[1];
            String nom = parts[2];
            String parametres = selectedMethode.substring(selectedMethode.indexOf("(") + 1, selectedMethode.indexOf(")"));

            // Fenêtre pour modifier la méthode
            Stage methodeStage = new Stage();
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            TextField txtNom = new TextField(nom);
            ComboBox<String> comboRetour = new ComboBox<>();
            comboRetour.getItems().addAll("void", "String", "int", "double", "boolean", "float");
            comboRetour.setValue(retour);

            ComboBox<String> comboModificateur = new ComboBox<>();
            comboModificateur.getItems().addAll("private", "public", "protected");
            comboModificateur.setValue(modificateur);

            TextField txtParametres = new TextField(parametres);

            Button btnValider = new Button("Valider");
            btnValider.setOnAction(e -> {
                listMethodes.getItems().set(
                        listMethodes.getSelectionModel().getSelectedIndex(),
                        comboModificateur.getValue() + " " + comboRetour.getValue() + " " + txtNom.getText() +
                                "(" + txtParametres.getText() + ")"
                );
                methodeStage.close();
            });

            root.getChildren().addAll(new Label("Modifier la méthode"), txtNom, comboRetour, comboModificateur, txtParametres, btnValider);
            Scene scene = new Scene(root, 400, 250);
            methodeStage.setScene(scene);
            methodeStage.showAndWait();
        }
    }

    private void ajouterAttribut(ListView<String> listAttributs) {
        // Même logique que pour modifier, mais pour ajouter un nouvel attribut
        Stage attributStage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField txtNom = new TextField();
        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("String", "int", "double", "boolean", "float");

        ComboBox<String> comboModificateur = new ComboBox<>();
        comboModificateur.getItems().addAll("private", "public", "protected");

        Button btnValider = new Button("Valider");
        btnValider.setOnAction(e -> {
            listAttributs.getItems().add(
                    comboModificateur.getValue() + " " + comboType.getValue() + " " + txtNom.getText()
            );
            attributStage.close();
        });

        root.getChildren().addAll(new Label("Ajouter un attribut"), txtNom, comboType, comboModificateur, btnValider);
        Scene scene = new Scene(root, 300, 200);
        attributStage.setScene(scene);
        attributStage.showAndWait();
    }

    private void ajouterMethode(ListView<String> listMethodes) {
        // Même logique que pour modifier, mais pour ajouter une nouvelle méthode
        Stage methodeStage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField txtNom = new TextField();
        ComboBox<String> comboRetour = new ComboBox<>();
        comboRetour.getItems().addAll("void", "String", "int", "double", "boolean", "float");

        ComboBox<String> comboModificateur = new ComboBox<>();
        comboModificateur.getItems().addAll("private", "public", "protected");

        TextField txtParametres = new TextField();
        txtParametres.setPromptText("Paramètres (séparés par des virgules)");

        Button btnValider = new Button("Valider");
        btnValider.setOnAction(e -> {
            listMethodes.getItems().add(
                    comboModificateur.getValue() + " " + comboRetour.getValue() + " " + txtNom.getText() +
                            "(" + txtParametres.getText() + ")"
            );
            methodeStage.close();
        });

        root.getChildren().addAll(new Label("Ajouter une méthode"), txtNom, comboRetour, comboModificateur, txtParametres, btnValider);
        Scene scene = new Scene(root, 400, 250);
        methodeStage.setScene(scene);
        methodeStage.showAndWait();
    }

    private String formatAttribut(Attribut attribut) {
        return attribut.getModificateur() + " " + attribut.getTypeAttribut() + " " + attribut.getNomAttribut();
    }

    private String formatMethode(Methode methode) {
        return methode.getModificateur() + " " + methode.getTypeRetour() + " " + methode.getNomMethode() +
                "(" + String.join(", ", methode.getParametres()) + ")";
    }
}

