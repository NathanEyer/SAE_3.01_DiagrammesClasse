package diagrammes.controleur;

import diagrammes.modele.ModeleDiagramme;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import java.io.File;

/**
 * Gère les interactions de drag and drop
 */
public class ControleurDragDrop {
    /**
     * Modèle qui contient les données du diagramme.
     */
    private ModeleDiagramme modele;

    /**
     * Construit le contrôleur avec une référence au modèle.
     * @param modele Le modèle contenant les données du diagramme.
     */
    public ControleurDragDrop(ModeleDiagramme modele) {
        this.modele = modele;
    }

    /**
     * Méthode de drag (glissement de la souris)
     * @param event évènement déclenché
     */
    public void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != event.getSource() && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    /**
     * Méthode de drop (relachement de la souris)
     * @param event évènement déclenché
     */
    public void handleDragDropped(DragEvent event) {
        var db = event.getDragboard();
        if (db.hasFiles()) {
            for (File file : db.getFiles()) {
                if (file.getName().endsWith(".class")) {
                    modele.analyserFichierClass(file.getAbsolutePath());
                }
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }
}
