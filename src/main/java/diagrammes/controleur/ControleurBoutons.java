package diagrammes.controleur;

import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * ControleurImportExport gère les interactions utilisateur pour l'importation et l'exportation
 * des données du diagramme UML.
 */
public class ControleurBoutons implements EventHandler<ActionEvent> {

    /**
     * Référence au modèle qui contient les données du diagramme.
     */
    private final ModeleDiagramme modele;
    private Stage stage;
    private VueDiagramme vue;

    /**
     * Constructeur du ControleurImportExport.
     *
     * @param modele       Le modèle contenant les données du diagramme.
     */
    public ControleurBoutons(ModeleDiagramme modele, Stage stage, VueDiagramme vue) {
        this.modele = modele;
        this.stage = stage;
        this.vue = vue;
    }

    /**
     * Gère les événements déclenchés par l'utilisateur pour l'importation ou l'exportation.
     *
     * @param event L'événement déclenché par l'utilisateur.
     */
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() instanceof MenuItem control) {
            switch (control.getText()) {
                case "Importer":
                    modele.importerFichierClass();
                    break;
                case "Exporter en PNG":
                    modele.exporter(stage, vue, "PNG");
                    break;
                case "Exporter en UML":
                    modele.exporter(stage, vue, "UML");
                    break;
                case "Réinitialiser":
                    modele.reinitialiser();
                    break;
                case "Nouveau":
                    modele.nouveau();
                    break;
                default:
                    System.out.println("Bouton inconnu : " + control.getId());
            }
        }
    }
}

