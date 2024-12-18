package diagrammes.controleur;

import diagrammes.modele.ModeleDiagramme;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ControleurBoutons implements EventHandler<ActionEvent> {
    private ModeleDiagramme modele;

    public ControleurBoutons(ModeleDiagramme modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent event) {
        Button b = (Button) event.getSource();
        switch(b.getText()){
            case "Importer":
                System.out.println("importer");
                break;
            case "Créer un nouveau diagramme":
                System.out.println("créer");
                break;
            case "Réinitialiser":
                System.out.println("réinitialiser");
                break;
            case "Exporter":
                System.out.println("Exporter");
                break;
        }
    }
}
