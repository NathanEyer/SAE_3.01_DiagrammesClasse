package diagrammes.controleur;

import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueDiagramme;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


/**
 * ControleurDiagramme est la classe responsable de gérer les interactions
 *  * entre l'utilisateur, le modèle (ModeleDiagramme) et la vue (VueDiagramme).
 *  * Elle configure les gestionnaires d'événements pour répondre aux actions de l'utilisateur
 *  * dans l'interface graphique.
 */
public class ControleurDiagramme implements EventHandler<Event> {


    /**
     * Référence au modèle qui contient les données du diagramme.
     */
    private ModeleDiagramme modele;

    /**
     * Constructeur du ControleurDiagramme.
     * Initialise le contrôleur avec une référence au modèle.
     *
     * @param modele Le modèle contenant les données du diagramme.
     */
    public ControleurDiagramme(ModeleDiagramme modele) {
        this.modele = modele;
    }

    /**
     * Gère les événements déclenchés par l'utilisateur.
     * Ici, uniquement les clics de la souris.
     *
     * @param event L'événement déclenché par l'utilisateur.
     */
    @Override
    public void handle(Event event) {
        if (event instanceof MouseEvent mouseEvent) {
            switch (mouseEvent.getEventType().toString()) {
                default:
            }
        }
    }
}
