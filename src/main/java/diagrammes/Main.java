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

public class Main extends Application {
    //Tailles de l'écran arrangées pour l'interface
    public final static double SCREEN_WIDTH = Screen.getPrimary().getVisualBounds().getWidth() / 1.5;
    public final static double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight()*0.95;

    @Override
    public void start(Stage primaryStage) {
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

        // Création et affichage de la scène
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setTitle("Commencez par ajouter des fichiers .class");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
