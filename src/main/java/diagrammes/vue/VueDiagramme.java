package diagrammes.vue;

import diagrammes.Main;
import diagrammes.modele.Diagramme;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.classe.Classe;
import diagrammes.relations.Relation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.List;

/**
 * VueDiagramme affiche le diagramme UML à l'utilisateur en utilisant un Canvas.
 */
public class VueDiagramme extends Canvas implements Observateur {

    private final ModeleDiagramme modele;

    /**
     * Constructeur de VueDiagramme.
     * Initialise la vue et s'enregistre comme observateur auprès du modèle.
     *
     * @param modeleDiagramme Le modèle contenant les données du diagramme.
     */
    public VueDiagramme(ModeleDiagramme modeleDiagramme) {
        super(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        this.modele = modeleDiagramme;
        this.modele.enregistrerObservateur(this);
    }

    /**
     * Méthode appelée lorsque le modèle est mis à jour.
     *
     * @param diagramme Le diagramme mis à jour.
     */
    @Override
    public void actualiser(Diagramme diagramme) {
        if (diagramme instanceof ModeleDiagramme) {
            System.out.println("Mise à jour de VueDiagramme...");
            dessinerDiagramme();
        }
    }

    /**
     * Dessine le diagramme UML en fonction des données du modèle.
     */
    public void dessinerDiagramme() {
        // Obtenir le contexte graphique pour dessiner sur le Canvas
        GraphicsContext gc = this.getGraphicsContext2D();

        // Effacer le Canvas
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        // Dessiner les classes
        List<Classe> classes = modele.getClasses();
        for (int i = 0; i < classes.size(); i++) {
            Classe classe = classes.get(i);
            double x = 50 + i * 150; // Positionnement simple
            double y = 100;

            dessinerClasse(gc, classe, x, y);
        }

        // Dessiner les relations (si elles existent dans le modèle)
        List<Relation> relations = modele.getRelations();
        for (Relation relation : relations) {
            dessinerRelation(gc, relation);
        }
    }

    /**
     * Dessine une classe UML sur le Canvas.
     *
     * @param gc     Le contexte graphique.
     * @param classe La classe à dessiner.
     * @param x      Coordonnée X de la classe.
     * @param y      Coordonnée Y de la classe.
     */
    private void dessinerClasse(GraphicsContext gc, Classe classe, double x, double y) {
        double largeur = 100;
        double hauteur = 50;

        // Dessiner le rectangle
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(x, y, largeur, hauteur);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, largeur, hauteur);

        // Dessiner le texte
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 14));
        gc.fillText(classe.getNom(), x + 10, y + 25);
    }

    /**
     * Dessine une relation entre deux classes sur le Canvas.
     *
     * @param gc       Le contexte graphique.
     * @param relation La relation à dessiner.
     */
    private void dessinerRelation(GraphicsContext gc, Relation relation) {
        double startX = 100; // Position fictive pour l'exemple
        double startY = 100;
        double endX = 200;
        double endY = 150;

        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeLine(startX, startY, endX, endY);
    }
}
