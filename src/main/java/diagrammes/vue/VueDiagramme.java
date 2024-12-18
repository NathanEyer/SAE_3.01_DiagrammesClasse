package diagrammes.vue;

import diagrammes.modele.Diagramme;
import diagrammes.modele.ModeleDiagramme;
import javafx.scene.canvas.Canvas;

public class VueDiagramme extends Canvas implements Observateur {
    public VueDiagramme(ModeleDiagramme modeleDiagramme) {
        super();
    }

    @Override
    public void actualiser(Diagramme diagramme) {

    }
}
