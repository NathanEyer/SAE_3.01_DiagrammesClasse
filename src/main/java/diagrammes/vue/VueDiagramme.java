package diagrammes.vue;

import diagrammes.Main;
import diagrammes.classe.Attribut;
import diagrammes.classe.Methode;
import diagrammes.modele.Diagramme;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.classe.Classe;
import diagrammes.relations.Heritage;
import diagrammes.relations.Implementation;
import diagrammes.relations.Relation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.List;

/**
 * VueDiagramme affiche le diagramme UML à l'utilisateur en utilisant un Canvas.
 */
public class VueDiagramme extends Canvas implements Observateur {

    private final ModeleDiagramme modele;

    /**
     * Constructeur de VueDiagramme.
     * Initialise la vue et s'enregistre comme observateur auprès du modèle.
     * @param modeleDiagramme Le modèle contenant les données du diagramme.
     */
    public VueDiagramme(ModeleDiagramme modeleDiagramme) {
        super(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.modele = modeleDiagramme;
        this.modele.enregistrerObservateur(this);
    }

    /**
     * Méthode appelée lorsque le modèle est mis à jour.
     * @param diagramme Le diagramme mis à jour.
     */
    @Override
    public void actualiser(Diagramme diagramme) {
        if (diagramme instanceof ModeleDiagramme) {
            dessinerDiagramme();
        }
    }

    /**
     * Dessine le diagramme UML en fonction des données du modèle.
     */
    public void dessinerDiagramme() {
        GraphicsContext gc = this.getGraphicsContext2D();

        // Effacer le canvas
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        // Variables pour le positionnement
        double x = 50;
        double y = 50;

        // Dessiner chaque Classe
        List<Classe> classes = modele.getClasses();
        for (Classe classe : classes) {
            double espace = 30 + (classe.getAttributs().size() + classe.getMethodes().size()) * 20 + 20;
            dessinerClasse(gc, classe, x, y);
            y += espace;

            // Remettre à zéro si dépassement de la hauteur du Canvas
            if (y + espace > this.getHeight()) {
                y = 50;
                x += 300; // Décaler horizontalement pour une nouvelle colonne
            }
        }

        // Dessiner les relations
        dessinerRelations(gc);
    }

    /**
     * Dessine une Classe UML avec ses attributs et méthodes.
     *
     * @param gc     Le contexte graphique.
     * @param classe La Classe à dessiner.
     * @param x      La coordonnée X de la Classe.
     * @param y      La coordonnée Y de la Classe.
     */
    private void dessinerClasse(GraphicsContext gc, Classe classe, double x, double y) {
        double largeur = this.getLargeurClasse(classe);
        double hauteurNom = 30;
        double hauteurSection = 20;
        double padding = 5;

        // Calculer la hauteur totale
        double hauteur = hauteurNom + (classe.getAttributs().size() + classe.getMethodes().size()) * hauteurSection;

        // Dessiner le contour de la Classe
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, largeur, hauteur);

        // Dessiner la section du nom
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(x, y, largeur, hauteurNom);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, largeur, hauteurNom);

        // Afficher le nom de la Classe
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 14));
        gc.fillText(classe.getNom(), x + padding, y + hauteurNom - 10);

        // Dessiner les attributs
        double currentY = y + hauteurNom;
        for (Attribut attribut : classe.getAttributs()) {
            gc.fillText("- " + attribut.getNomAttribut() + " : " + attribut.getTypeAttribut(), x + padding, currentY + 15);
            currentY += hauteurSection;
        }

        gc.strokeLine(x, currentY, x + largeur, currentY);

        // Dessiner les méthodes
        for (Methode methode : classe.getMethodes()) {
            gc.fillText("+ " + methode.getNomMethode() + "()", x + padding, currentY + 15);
            currentY += hauteurSection;
        }
    }

    /**
     * Dessine les relations entre les classes.
     *
     * @param gc Le contexte graphique.
     */
    private void dessinerRelations(GraphicsContext gc) {
        for (Relation relation : modele.getRelations()) {
            dessinerRelation(gc, relation);
        }
    }

    /**
     * Dessine une relation entre deux classes.
     *
     * @param gc       Le contexte graphique.
     * @param relation La relation à dessiner.
     */
    private void dessinerRelation(GraphicsContext gc, Relation relation) {
        double startX = 0, startY = 0, endX = 0, endY = 0;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine(startX, startY, endX, endY);

        if (relation.getType() instanceof Heritage) {
            dessinerFlecheHeritage(gc, endX, endY);
        } else if (relation.getType() instanceof Implementation) {
            dessinerFlecheImplementation(gc, endX, endY);
        } else {
            dessinerFlecheAssociation(gc, endX, endY);
        }
    }

    private void dessinerFlecheHeritage(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.BLACK);
        double[] xPoints = {x, x - 10, x - 10};
        double[] yPoints = {y, y - 5, y + 5};
        gc.fillPolygon(xPoints, yPoints, xPoints.length);
    }

    private void dessinerFlecheImplementation(GraphicsContext gc, double x, double y) {
        gc.setStroke(Color.BLACK);
        gc.setLineDashes(5);
        gc.strokeLine(x, y, x - 10, y - 5);
        gc.setLineDashes(0);
    }

    private void dessinerFlecheAssociation(GraphicsContext gc, double x, double y) {
        gc.setStroke(Color.BLACK);
        gc.strokeLine(x, y, x - 10, y - 5);
    }

    private double getLargeurClasse(Classe classe) {
        double maxLength = 0;

        Text text = new Text(classe.getNom());
        maxLength = Math.max(maxLength, text.getLayoutBounds().getWidth());

        for (Attribut attribut : classe.getAttributs()) {
            text = new Text("- " + attribut.getNomAttribut() + " : " + attribut.getTypeAttribut());
            maxLength = Math.max(maxLength, text.getLayoutBounds().getWidth());
        }

        for (Methode methode : classe.getMethodes()) {
            text = new Text("+ " + methode.getNomMethode() + "()");
            maxLength = Math.max(maxLength, text.getLayoutBounds().getWidth());
        }

        return maxLength + 20;
    }

    public double getHauteurClasse(Classe classe) {
        return classe.getAttributs().size() + classe.getMethodes().size() + 20;
    }
}