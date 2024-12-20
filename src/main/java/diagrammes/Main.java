package diagrammes;

import diagrammes.classe.Classe;
import diagrammes.controleur.ControleurDragDrop;
import diagrammes.exporter.ExporterImage;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
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
        ExporterImage exporterImage = new ExporterImage();
        Button button = new Button("Exporter en PNG");
        button.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    exporterImage.exporter(file.getAbsolutePath(), vueDiagramme);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        root.setTop(button);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setTitle("Commencez par ajouter des fichiers .class");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}