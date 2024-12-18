package diagrammes;

import diagrammes.controleur.ControleurDiagramme;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ModeleDiagramme modeleDiagramme = new ModeleDiagramme();
        VueDiagramme vueDiagramme = new VueDiagramme(modeleDiagramme);

        modeleDiagramme.enregistrerObservateur(vueDiagramme);

        ControleurDiagramme controleurDiagramme = new ControleurDiagramme(modeleDiagramme);

        Button bImport = new Button("Importer package");
        Button bCreate = new Button("Créer diagramme");
        Button bDel = new Button("Réinitialiser");
        Button bExport = new Button("Exporter package");
        VueDiagramme lZone = new VueDiagramme(modeleDiagramme);

        bImport.setOnAction(e -> System.out.println("Importer"));
        bCreate.setOnAction(controleurDiagramme);
        bDel.setOnAction(controleurDiagramme);
        bExport.setOnAction(controleurDiagramme);
        lZone.setOnMouseClicked(e -> {System.out.println("Zone");});

        HBox buttons = new HBox(bImport, bCreate, bDel, bExport, lZone);
        VBox root = new VBox(buttons, lZone);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Diagramme de classe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
