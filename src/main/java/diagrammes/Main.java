package diagrammes;

import diagrammes.controleur.ControleurDragDrop;
import diagrammes.fichier.ExporterImage;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    public final static double SCREEN_WIDTH = Screen.getPrimary().getVisualBounds().getWidth() / 1.5;
    public final static double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

    @Override
    public void start(Stage primaryStage) {
        ModeleDiagramme modele = new ModeleDiagramme();
        BorderPane root = new BorderPane();
        ControleurDragDrop dragDrop = new ControleurDragDrop(modele);

        VueDiagramme vueDiagramme = new VueDiagramme(modele);
        modele.enregistrerObservateur(vueDiagramme);
        root.getChildren().add(vueDiagramme);

        root.setOnDragOver(dragDrop::handleDragOver);
        root.setOnDragDropped(dragDrop::handleDragDropped);

        // Création de la barre de menu
        MenuBar menuBar = new MenuBar();
        menuBar.setMinHeight(30); // Set minimum height for the menu bar
        menuBar.setPrefHeight(30); // Set preferred height for the menu bar

        // Menu Fichier
        Menu menuFichier = new Menu("Fichier");
        MenuItem importer = new MenuItem("Importer");
        Menu menuExporter = new Menu("Exporter");
        MenuItem exporterPng = new MenuItem("Exporter en PNG");
        MenuItem exporterUml = new MenuItem("Exporter en UML");

        // Action pour exporter en PNG
        exporterPng.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    ExporterImage exporterImage = new ExporterImage();
                    exporterImage.exporter(file.getAbsolutePath(), vueDiagramme);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        importer.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Class files (*.class)", "*.class"));
            fileChooser.setTitle("Sélectionnez un fichier .class");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                modele.analyserFichierClass(file.getAbsolutePath());
            }
        });

        // Ajout des éléments au menu Exporter
        menuExporter.getItems().addAll(exporterPng, exporterUml);
        // Ajout des éléments au menu Fichier
        menuFichier.getItems().addAll(importer, menuExporter);

        // Ajout du menu Fichier à la barre de menu
        menuBar.getMenus().add(menuFichier);

        // Création des boutons
        Button btnReinitialisation = new Button("Réinitialisation");
        Button btnNouveau = new Button("Nouveau");

        // Appliquer les styles aux boutons
        String buttonStyle = """
                -fx-focus-color: transparent;
                -fx-faint-focus-color: transparent;
                -fx-background-color: lightgray;
                -fx-border-color: darkgray;
                -fx-padding: 5 10 5 10;
                -fx-alignment: center;
                """;

        // Ajouter un style pour le bouton pressé
        String buttonPressedStyle = """
                -fx-background-color: darkgray;
                -fx-border-color: black;
                """;

        btnReinitialisation.setStyle(buttonStyle);
        btnNouveau.setStyle(buttonStyle);

        // Gestion de l'effet de pression
        btnReinitialisation.setOnMousePressed(e -> btnReinitialisation.setStyle(buttonPressedStyle));
        btnReinitialisation.setOnMouseReleased(e -> btnReinitialisation.setStyle(buttonStyle));
        btnNouveau.setOnMousePressed(e -> btnNouveau.setStyle(buttonPressedStyle));
        btnNouveau.setOnMouseReleased(e -> btnNouveau.setStyle(buttonStyle));

        // Conteneur pour les boutons
        HBox buttonBox = new HBox(10, btnReinitialisation, btnNouveau); // Espacement entre les boutons
        buttonBox.setPadding(new Insets(0, 10, 0, 0)); // Padding droit pour un écart entre les boutons et le bord de la barre
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT); // Alignement des boutons à droite

        // Conteneur principal pour la barre supérieure
        HBox topBar = new HBox(menuBar, buttonBox);
        HBox.setHgrow(buttonBox, Priority.ALWAYS);
        topBar.setSpacing(0); // Pas d'espacement entre les éléments
        topBar.setStyle("-fx-background-color: lightgray;"); // Couleur de fond pour unifier

        // Ajout de la barre de menu en haut
        root.setTop(topBar);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setTitle("Commencez par ajouter des fichiers .class");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
