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
                case "MOUSE_CLICKED":
                    System.out.println("Clic détecté à : (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
                    // Exemple d'action sur le modèle
                    modele.selectionnerClasse("Classe");//nom de la classe en question
                    break;

                case "MOUSE_DRAGGED":
                    System.out.println("Déplacement détecté à : (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
                    modele.deplacerClasseSelectionnee(mouseEvent.getX(), mouseEvent.getY());
                    break;

                case "MOUSE_PRESSED":
                    System.out.println("Bouton pressé à : (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
                    // Logique pour initier une action
                    break;

                case "MOUSE_RELEASED":
                    System.out.println("Bouton relâché à : (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
                    // Logique pour finaliser une action
                    break;

                case "MOUSE_ENTERED":
                    System.out.println("Souris entrée dans la zone !");
                    // Logique pour survol d'un élément
                    break;

                case "MOUSE_EXITED":
                    System.out.println("Souris sortie de la zone !");
                    // Logique pour quitter la zone
                    break;

                case "MOUSE_MOVED":
                    System.out.println("Mouvement détecté à : (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
                    // Logique pour mettre en évidence un élément
                    break;

                default:
                    System.out.println("Autre événement détecté : " + mouseEvent.getEventType());
            }
        }
    }
}
