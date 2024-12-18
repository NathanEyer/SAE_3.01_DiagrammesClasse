package diagrammes;

import diagrammes.controleur.ControleurBoutons;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ModeleDiagramme modeleDiagramme = new ModeleDiagramme();
        VueDiagramme vueDiagramme = new VueDiagramme(modeleDiagramme);

        modeleDiagramme.enregistrerObservateur(vueDiagramme);

        ControleurBoutons controleurBoutons = new ControleurBoutons(modeleDiagramme);

        Button bImport = new Button("Importer");
        Button bCreate = new Button("Créer un nouveau diagramme");
        Button bDel = new Button("Réinitialiser");
        Button bExport = new Button("Exporter");
        VueDiagramme lZone = new VueDiagramme(modeleDiagramme);

        bImport.setOnAction(e -> System.out.println("Importer"));
        bCreate.setOnAction(controleurBoutons);
        bDel.setOnAction(controleurBoutons);
        bExport.setOnAction(controleurBoutons);
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
