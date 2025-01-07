package diagrammes.controleur;

import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import java.io.File;

/**
 * Gère les interactions de drag and drop
 */
public class ControleurDragDrop {
    private final ModeleDiagramme modele;
    private final VueDiagramme vue; // Référence à la vue pour afficher les messages

    /**
     * Construit le contrôleur avec une référence au modèle et à la vue.
     * @param modele Le modèle contenant les données du diagramme.
     * @param vue La vue permettant d'afficher les messages.
     */
    public ControleurDragDrop(ModeleDiagramme modele, VueDiagramme vue) {
        this.modele = modele;
        this.vue = vue;
    }

    /**
     * Méthode de drag (glissement de la souris)
     * @param event évènement déclenché
     */
    public void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != event.getSource() && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
            vue.setMessage("Relâchez pour ajouter le fichier.");
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
                    vue.setMessage("Fichier ajouté : " + file.getName());
                } else {
                    vue.setMessage("Erreur : " + file.getName() + " n'est pas un fichier .class valide.");
                }
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }
}

