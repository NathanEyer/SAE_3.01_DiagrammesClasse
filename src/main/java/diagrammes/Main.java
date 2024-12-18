package diagrammes;

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
        Button bImport = new Button("Importer package");
        Button bCreate = new Button("Créer diagramme");
        Button bDel = new Button("Réinitialiser");
        Button bExport = new Button("Exporter package");
        Label lZone = new Label("Zone d'affichage des diagrammes");

        bImport.setOnAction(e -> System.out.println("Importer"));
        bCreate.setOnAction(e -> System.out.println("Créer"));
        bDel.setOnAction(e -> System.out.println("Réinitialiser"));
        bExport.setOnAction(e -> System.out.println("Exporter"));
        lZone.setOnMouseClicked(e -> {System.out.println("Zone");});

        VBox root = new VBox();
        HBox buttons = new HBox();
        buttons.getChildren().addAll(bImport, bCreate, bDel, bExport);
        root.getChildren().addAll(buttons, lZone);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Création d'un diagramme");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
