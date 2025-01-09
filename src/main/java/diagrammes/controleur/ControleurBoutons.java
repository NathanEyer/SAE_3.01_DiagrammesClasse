package diagrammes.controleur;

import diagrammes.classe.Classe;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.vue.VueCreationClasse;
import diagrammes.vue.VueDiagramme;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Redirige les actions des boutons vers les méthodes correspondantes
 */
public class ControleurBoutons implements EventHandler<ActionEvent> {
    /**
     * Référence au modèle qui contient les données du diagramme
     */
    private final ModeleDiagramme modele;
    private final Stage stage;
    private final VueDiagramme vue;

    /**
     * Construit un objet ControleurBoutons
     * @param modele de l'application
     * @param stage de l'application
     * @param vue du diagramme
     */
    public ControleurBoutons(ModeleDiagramme modele, Stage stage, VueDiagramme vue) {
        this.modele = modele;
        this.stage = stage;
        this.vue = vue;
    }

    /**
     * Gère les événements déclenchés par les boutons
     * @param event L'événement déclenché par le bouton
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
                case "Créer une nouvelle classe":
                    VueCreationClasse vueCreation = new VueCreationClasse();
                    Classe nouvelleClasse = vueCreation.afficher();
                    if (nouvelleClasse != null) {
                        try {
                            modele.addClass(nouvelleClasse);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        VueDiagramme.setMessage("Classe " + nouvelleClasse.getNom() + " créée !");
                    }
                    break;
                case "Exporter en Java":
                    modele.exporter(stage, vue, "JAVA");
                    break;
                default:
                    VueDiagramme.setMessage("Bouton inconnu : " + control.getId());
            }
        }
    }
}

