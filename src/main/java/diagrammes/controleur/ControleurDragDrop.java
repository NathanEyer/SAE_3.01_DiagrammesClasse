package diagrammes.controleur;

import diagrammes.classe.Classe;
import diagrammes.modele.ModeleDiagramme;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import java.io.File;


/**
 * ControleurDragDrop est la classe responsable de gérer les interactions
 *  * entre l'utilisateur, le modèle (ModeleDiagramme) et la vue (VueDiagramme).
 *  * Elle configure les gestionnaires d'événements pour répondre aux actions de l'utilisateur
 *  * dans l'interface graphique.
 */
public class ControleurDragDrop {


    /**
     * Référence au modèle qui contient les données du diagramme.
     */
    private ModeleDiagramme modele;

    /**
     * Constructeur du ControleurDragDrop.
     * Initialise le contrôleur avec une référence au modèle.
     *
     * @param modele Le modèle contenant les données du diagramme.
     */
    public ControleurDragDrop(ModeleDiagramme modele) {
        this.modele = modele;
    }

    public void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != event.getSource() && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);  // Autoriser le mode de transfert
        }
        event.consume();
    }

    public void handleDragDropped(DragEvent event) {
        var db = event.getDragboard();
        if (db.hasFiles()) {
            for(int i=0; i<db.getFiles().size();i++) {
                File file = db.getFiles().get(i);
                if(file.getName().endsWith(".class")){
                    modele.addClass(new Classe(file.getName().split("\\.")[0]));
                }
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }
}
