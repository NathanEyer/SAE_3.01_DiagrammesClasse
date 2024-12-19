package diagrammes;

import diagrammes.controleur.ControleurDiagramme;
import diagrammes.controleur.ControleurImportExport;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
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

        MenuButton bExport = new MenuButton("Exporter");
        bExport.setId("exporterButton");
        bExport.setPrefWidth(150);
        MenuItem exportUML = new MenuItem("Exporter en UML");
        MenuItem exportPNG = new MenuItem("Exporter en PNG");


        bExport.getItems().addAll(exportUML, exportPNG);

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

        String buttonStyle = """
            -fx-background-color: linear-gradient(to bottom, #ff7e5f, #feb47b);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
            -fx-border-radius: 10px;
            -fx-background-radius: 10px;
        """;

        String pressedStyle = """
            -fx-background-color: linear-gradient(to bottom, #feb47b, #ff7e5f);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8px 18px; /* Réduction pour simuler une pression */
            -fx-border-radius: 10px;
            -fx-background-radius: 10px;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0.3, 0, 1);
        """;


        bCreate.setStyle(buttonStyle);
        bExport.setStyle(buttonStyle);
        bImport.setStyle(buttonStyle);
        bReset.setStyle(buttonStyle);

        applyPressEffect(bCreate, buttonStyle, pressedStyle);
        applyPressEffect(bReset, buttonStyle, pressedStyle);
        applyPressEffect(bImport, buttonStyle, pressedStyle);
        bExport.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> bExport.setStyle(pressedStyle));
        bExport.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> bExport.setStyle(buttonStyle));



        BorderPane root = new BorderPane();
        root.setTop(buttons);
        root.setCenter(vueDiagramme);
        root.setStyle("-fx-background-color: #87CEED");

        // 7. Afficher la scène
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setTitle("Application de diagrammes UML");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void applyPressEffect(Button button, String normalStyle, String pressedStyle) {
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> button.setStyle(pressedStyle));
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> button.setStyle(normalStyle));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
