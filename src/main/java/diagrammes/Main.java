package diagrammes;

import diagrammes.controleur.ControleurDiagramme;
import diagrammes.controleur.ControleurImportExport;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    public final static double SCREEN_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    public final static double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

    @Override
    public void start(Stage primaryStage) {

        // 1. Initialiser le modèle
        ModeleDiagramme modele = new ModeleDiagramme();

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
        bImport.setPrefWidth(150);

        Button bExport = new Button("Exporter");
        bExport.setId("exporterButton");
        bExport.setPrefWidth(150);

        Button bReset = new Button("Réinitialiser");
        bReset.setId("resetButton");
        bReset.setPrefWidth(150);
        bReset.setOnAction(e -> {
            System.out.println("Réinitialisation !");
            modele.getClasses().clear();
            modele.getRelations().clear();
            modele.notifierObservateur();
        });

        Button bCreate = new Button("Créer un nouveau diagramme");
        bCreate.setPrefWidth(300);


        // 5. Associer le ControleurImportExport pour les boutons
        ControleurImportExport controleurImportExport = new ControleurImportExport(modele, primaryStage);
        bImport.setOnAction(controleurImportExport);
        bExport.setOnAction(controleurImportExport);

        // 6. Disposition principale
        HBox buttons = new HBox(100, bImport, bExport, bReset, bCreate);
        buttons.setAlignment(Pos.CENTER);
        buttons.setFillHeight(false);
        BorderPane root = new BorderPane();
        root.setTop(buttons);
        root.setCenter(vueDiagramme);
        root.setStyle("-fx-background-color: #87CEED");

        // 7. Afficher la scène
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setTitle("Test VueDiagramme");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
