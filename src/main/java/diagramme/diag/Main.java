package diagramme.diag;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création d'un bouton
        Button button = new Button("Cliquez-moi !");
        button.setOnAction(e -> System.out.println("Bouton cliqué !"));

        // Création d'un layout
        StackPane root = new StackPane();
        root.getChildren().add(button);

        // Création de la scène
        Scene scene = new Scene(root, 300, 200);

        // Configuration de la fenêtre principale
        primaryStage.setTitle("Test JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
