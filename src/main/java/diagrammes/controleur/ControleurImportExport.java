package diagrammes.controleur;

import diagrammes.modele.ModeleDiagramme;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * ControleurImportExport gère les interactions utilisateur pour l'importation et l'exportation
 * des données du diagramme UML.
 */
public class ControleurImportExport implements EventHandler<ActionEvent> {

    /**
     * Référence au modèle qui contient les données du diagramme.
     */
    private final ModeleDiagramme modele;

    /**
     * Fenêtre principale pour afficher les boîtes de dialogue de fichiers.
     */
    private final Stage primaryStage;

    /**
     * Constructeur du ControleurImportExport.
     *
     * @param modele       Le modèle contenant les données du diagramme.
     * @param primaryStage La fenêtre principale de l'application.
     */
    public ControleurImportExport(ModeleDiagramme modele, Stage primaryStage) {
        this.modele = modele;
        this.primaryStage = primaryStage;
    }

    /**
     * Gère les événements déclenchés par l'utilisateur pour l'importation ou l'exportation.
     *
     * @param event L'événement déclenché par l'utilisateur.
     */
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() instanceof Button button) {
            switch (button.getId()) {
                case "importerButton":
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Importer un fichier .class");
                    fileChooser.getExtensionFilters().add(
                            new FileChooser.ExtensionFilter("Fichiers Class", "*.class")
                    );

                    File fichier = fileChooser.showOpenDialog(primaryStage);
                    if (fichier != null) {
                        String cheminClasse = fichier.getAbsolutePath()
                                .replace(File.separator, ".")
                                .replace(".class", "")
                                .replace("src.main.java.", "");

                        modele.analyserFichierClass(cheminClasse);
                    }
                    break;

                case "exporterButton":
                    modele.exporter("PNG");
                    break;

                case "exporterPlantUMLButton":
                    modele.exporter("PlantUML");
                    break;

                default:
                    System.out.println("Bouton inconnu : " + button.getId());
            }
        }
    }
}

