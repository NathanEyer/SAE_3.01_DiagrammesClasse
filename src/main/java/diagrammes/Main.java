package diagrammes;

import diagrammes.classe.Classe;
import diagrammes.controleur.ControleurDiagramme;
import diagrammes.controleur.ControleurImportExport;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Initialiser le modèle
        ModeleDiagramme modele = new ModeleDiagramme();
        modele.addClass(new Classe("Classe1"));
        modele.addClass(new Classe("Classe2"));
        modele.addClass(new Classe("Classe3"));

        // 2. Initialiser la VueDiagramme
        VueDiagramme vueDiagramme = new VueDiagramme(modele);

        // 3. Associer le ControleurDiagramme pour gérer les interactions souris
        ControleurDiagramme controleurDiagramme = new ControleurDiagramme(modele);
        vueDiagramme.setOnMouseClicked(controleurDiagramme);
        vueDiagramme.setOnMousePressed(controleurDiagramme);
        vueDiagramme.setOnMouseDragged(controleurDiagramme);
        vueDiagramme.setOnMouseReleased(controleurDiagramme);

        // 4. Boutons pour importer, exporter et réinitialiser
        Button bImport = new Button("Importer");
        bImport.setId("importerButton");

        Button bExport = new Button("Exporter");
        bExport.setId("exporterButton");

        Button bReset = new Button("Réinitialiser");
        bReset.setId("resetButton");
        bReset.setOnAction(e -> {
            System.out.println("Réinitialisation !");
            modele.getClasses().clear();
            modele.getRelations().clear();
            modele.notifierObservateur();
        });

        // 5. Associer le ControleurImportExport pour les boutons
        ControleurImportExport controleurImportExport = new ControleurImportExport(modele, primaryStage);
        bImport.setOnAction(controleurImportExport);
        bExport.setOnAction(controleurImportExport);

        // 6. Disposition principale
        HBox buttons = new HBox(10, bImport, bExport, bReset);
        BorderPane root = new BorderPane();
        root.setTop(buttons);
        root.setCenter(vueDiagramme);

        // 7. Afficher la scène
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Test VueDiagramme");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
