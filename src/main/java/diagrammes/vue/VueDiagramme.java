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
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

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
        GraphicsContext gc = this.getGraphicsContext2D();

        // Effacer le canvas
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        // Variables pour le positionnement
        double x = 50;
        double y = 50;
        double spacing = 150;

        // Dessiner chaque classe
        List<Classe> classes = modele.getClasses();
        for (Classe classe : classes) {
            dessinerClasse(gc, classe, x, y);
            y += spacing; // Décaler verticalement pour la prochaine classe

            // Remettre à zéro si dépassement de la hauteur du Canvas
            if (y + spacing > this.getHeight()) {
                y = 50;
                x += 300; // Décaler horizontalement pour une nouvelle colonne
            }
        }
    }

    /**
     * Dessine une classe UML avec ses attributs et méthodes.
     *
     * @param gc     Le contexte graphique.
     * @param classe La classe à dessiner.
     * @param x      La coordonnée X de la classe.
     * @param y      La coordonnée Y de la classe.
     */
    private void dessinerClasse(GraphicsContext gc, Classe classe, double x, double y) {
        double largeur = 200;
        double hauteurNom = 30;
        double hauteurSection = 20;
        double padding = 5;

        // Calculer la hauteur totale
        double hauteur = hauteurNom + (classe.getAttributs().size() + classe.getMethodes().size()) * hauteurSection;

        // Dessiner le contour de la classe
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, largeur, hauteur);

        // Dessiner la section du nom
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(x, y, largeur, hauteurNom);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, largeur, hauteurNom);

        // Afficher le nom de la classe
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 14));
        gc.fillText(classe.getNom(), x + padding, y + hauteurNom - 10);

        // Dessiner les attributs
        double currentY = y + hauteurNom;
        for (Attribut attribut : classe.getAttributs()) {
            gc.fillText("- " + attribut.getNomAttribut() + " : " + attribut.getTypeAttribut(), x + padding, currentY + 15);
            currentY += hauteurSection;
        }

        // Dessiner les méthodes
        for (Methode methode : classe.getMethodes()) {
            gc.fillText("+ " + methode.getNomMethode() + "()", x + padding, currentY + 15);
            currentY += hauteurSection;
        }
    }

    /**
     * Dessine une relation entre deux classes sur le Canvas.
     *
     * @param gc       Le contexte graphique.
     * @param relation La relation à dessiner.
     */


    public void actualiser(ModeleDiagramme modele) {
        this.getChildren().clear();
        dessinerClasses();
        dessinerRelations();
    }

    private void dessinerClasses() {
        // Logic to draw classes
    }

    private void dessinerRelations() {
        for (Relation relation : modele.getRelations()) {
            dessinerRelation(relation);
        }
    }

    private void dessinerRelation(Relation relation) {
        double startX = getClassePositionX(relation.getDepart());
        double startY = getClassePositionY(relation.getDepart());
        double endX = getClassePositionX(relation.getDestination());
        double endY = getClassePositionY(relation.getDestination());

        Line line = new Line(startX, startY, endX, endY);
        this.getChildren().add(line);

        if (relation.getType().equals(Heritage.type())) {
            dessinerFlecheHeritage(endX, endY);
        } else if (relation.getType().equals(Implementation.type())) {
            dessinerFlecheImplementation(endX, endY);
        } else {
            dessinerFlecheAssociation(endX, endY);
        }
    }

    private void dessinerFlecheHeritage(double x, double y) {
        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(x, y, x - 10, y - 5, x - 10, y + 5);
        this.getChildren().add(arrowHead);
    }

    private void dessinerFlecheImplementation(double x, double y) {
        Line dashedLine = new Line(x, y, x - 10, y - 5);
        dashedLine.getStrokeDashArray().addAll(5.0, 5.0);
        this.getChildren().add(dashedLine);
    }

    private void dessinerFlecheAssociation(double x, double y) {
        Line line = new Line(x, y, x - 10, y - 5);
        this.getChildren().add(line);
    }

    private double getClassePositionX(Class classe) {
        // Logic to get X position of the class
        return 0;
    }

    private double getClassePositionY(Class classe) {
        // Logic to get Y position of the class
        return 0;
    }
}
