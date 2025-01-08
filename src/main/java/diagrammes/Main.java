package diagrammes;

import diagrammes.controleur.ControleurBoutons;
import diagrammes.controleur.ControleurDragDrop;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Launcher de l'application
 */
public class Main extends Application {
    /**
     * Récupère la taille de l'écran de l'utilisateur
     */
    public final static double SCREEN_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    public final static double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

    /**
     * Launcher
     * @param primaryStage squelette
     */
    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException {
        // Initialisations
        ModeleDiagramme modele = new ModeleDiagramme();
        VueDiagramme vue = new VueDiagramme(modele);
        modele.enregistrerObservateur(vue);

        ControleurBoutons controleurBoutons = new ControleurBoutons(modele, primaryStage, vue);
        ControleurDragDrop dragDrop = new ControleurDragDrop(modele);

        // Conteneur principal
        BorderPane root = new BorderPane();
        root.getChildren().add(vue);

        // Configuration des contrôleurs Drag and Drop
        root.setOnDragOver(dragDrop::handleDragOver);
        root.setOnDragDropped(dragDrop::handleDragDropped);

        // Barre de menu et barre supérieure
        MenuBar menuBar = Interface.createMenuBar(controleurBoutons);
        HBox topBar = Interface.createTopBar(menuBar);

        // Ajout des éléments à la vue principale
        root.setTop(topBar);

        // Récupération et configuration du Label pour les messages
        Label messageLabel = vue.getMessageLabel();
        messageLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 10; -fx-alignment: center-left;");
        messageLabel.setMaxWidth(Double.MAX_VALUE); // Étendre le Label à toute la largeur
        messageLabel.setMinHeight(30); // Hauteur minimale pour une meilleure apparence

        // Utilisation d'un conteneur HBox pour s'assurer du bon alignement
        HBox bottomBar = new HBox(messageLabel);
        bottomBar.setStyle("-fx-background-color: lightgray;"); // Couleur de fond pour correspondre au style global
        bottomBar.setMinHeight(40); // Fixer une hauteur minimale
        bottomBar.setPrefHeight(40);
        bottomBar.setMaxWidth(Double.MAX_VALUE); // Étendre la barre à toute la largeur

        // Ajouter la barre en bas du root
        root.setBottom(bottomBar);


        // Création et affichage de la scène
        Scene scene = new Scene(root, SCREEN_WIDTH / 1.5, SCREEN_HEIGHT * 0.95);
        primaryStage.setTitle("Commencez par ajouter des fichiers .class");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {launch(args);}
}
