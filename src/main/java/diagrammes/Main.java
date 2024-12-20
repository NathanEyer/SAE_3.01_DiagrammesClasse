package diagrammes;

import diagrammes.classe.Classe;
import diagrammes.controleur.ControleurDragDrop;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
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

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setTitle("Application de diagrammes UML");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}