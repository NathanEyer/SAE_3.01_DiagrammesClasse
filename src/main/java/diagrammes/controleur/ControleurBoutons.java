package diagrammes.controleur;

import diagrammes.modele.ModeleDiagramme;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * ControleurImportExport gère les interactions utilisateur pour l'importation et l'exportation
 * des données du diagramme UML.
 */
public class ControleurBoutons implements EventHandler<ActionEvent> {

    /**
     * Référence au modèle qui contient les données du diagramme.
     */
    private final ModeleDiagramme modele;

    /**
     * Constructeur du ControleurImportExport.
     *
     * @param modele       Le modèle contenant les données du diagramme.
     */
    public ControleurBoutons(ModeleDiagramme modele) {
        this.modele = modele;
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
                    modele.importerFichierClass();
                    break;
                case "exporterPngButton":
                    modele.exporter("PNG");
                    break;
                case "exporterUmlButton":
                    modele.exporter("UML");
                    break;
                default:
                    System.out.println("Bouton inconnu : " + button.getId());
            }
        }
//                    FileChooser fileChooser = new FileChooser();
//                    fileChooser.setTitle("Importer un fichier .class");
//                    fileChooser.getExtensionFilters().add(
//                            new FileChooser.ExtensionFilter("Fichiers Class", "*.class")
//                    );
//
//                    File fichier = fileChooser.showOpenDialog(primaryStage);
//                    if (fichier != null) {
//                        String cheminClasse = fichier.getAbsolutePath()
//                                .replace(File.separator, ".")
//                                .replace(".class", "")
//                                .replace("src.main.java.", "");
//
//                        modele.analyserFichierClass(cheminClasse);
//                    }
//                    break;
    }
}

