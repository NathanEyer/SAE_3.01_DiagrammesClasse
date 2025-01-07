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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;

/**
 * VueDiagramme affiche le diagramme à l'utilisateur en utilisant un Canvas.
 */
public class VueDiagramme extends Canvas implements Observateur {
    /**
     * Diagramme
     */
    private final ModeleDiagramme modele;
    private final HashMap<Classe, Rectangle> positionsClasses = new HashMap<>();
    private Classe classeSelectionnee = null;
    private double offsetX, offsetY;

    /**
     * Initialise le diagramme
     * @param modeleDiagramme Le modèle contenant les données du diagramme
     */
    public VueDiagramme(ModeleDiagramme modeleDiagramme) {
        super(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.modele = modeleDiagramme;
        this.modele.enregistrerObservateur(this);

        this.setOnMousePressed(this::gererMousePressed);
        this.setOnMouseDragged(this::gererMouseDragged);
        this.setOnMouseReleased(this::gererMouseReleased);
    }

    /**
     * Mise à jour de la vue
     * @param diagramme diagramme à actualiser
     */
    @Override
    public void actualiser(Diagramme diagramme) {
        if (diagramme instanceof ModeleDiagramme) {
            dessinerDiagramme();
        }
    }

    /**
     * Dessine le diagramme UML en fonction des données du modèle
     */
    public void dessinerDiagramme() {
        GraphicsContext gc = this.getGraphicsContext2D();

        // Effacer le canvas
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        // Variables pour le positionnement
        double x = 50, y = 50;

        // Dessiner chaque Classe
        List<Classe> classes = modele.getClasses();
        for (Classe classe : classes) {
            double largeur = getLargeurClasse(classe);
            double hauteur = getHauteurClasse(classe);

            double finalX = x;
            double finalY = y;
            Rectangle position = positionsClasses.computeIfAbsent(classe, c -> new Rectangle(finalX, finalY, largeur, hauteur));
            dessinerClasse(gc, classe, position.getX(), position.getY());

            y += hauteur + 30;
            if (y + hauteur > this.getHeight()) {
                y = 50;
                x += 300;
            }
        }

        dessinerRelations(gc);
    }

    private void gererMousePressed(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        for (var entry : positionsClasses.entrySet()) {
            Rectangle rect = entry.getValue();
            if (rect.contains(mouseX, mouseY)) {
                classeSelectionnee = entry.getKey();
                offsetX = mouseX - rect.getX();
                offsetY = mouseY - rect.getY();
                break;
            }
        }
    }

    private void gererMouseDragged(MouseEvent event) {
        if (classeSelectionnee != null) {
            double newX = event.getX() - offsetX;
            double newY = event.getY() - offsetY;

            Rectangle rect = positionsClasses.get(classeSelectionnee);
            rect.setX(newX);
            rect.setY(newY);

            dessinerDiagramme();
        }
    }

    private void gererMouseReleased(MouseEvent event) {
        classeSelectionnee = null;
    }

    /**
     * Dessine une Classe avec ses attributs et méthodes
     * @param gc contexte graphique
     * @param classe classe à dessiner
     * @param x coordonnée X de la Classe
     * @param y coordonnée Y de la Classe
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
     * Dessine les relations entre les classes
     * @param gc contexte graphique
     */
    private void dessinerRelations(GraphicsContext gc) {
        for (Relation relation : modele.getRelations()) {
            dessinerRelation(gc, relation);
        }
    }

    /**
     * Dessine une relation entre deux classes
     * @param gc contexte graphique
     * @param relation relation à dessiner
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

    /**
     * Dessine la flèche d'héritage
     * @param gc contexte graphique
     * @param x coordonnée X de la Classe
     * @param y coordonnée Y de la Classe
     */
    private void dessinerFlecheHeritage(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.BLACK);
        double[] xPoints = {x, x - 10, x - 10};
        double[] yPoints = {y, y - 5, y + 5};
        gc.fillPolygon(xPoints, yPoints, xPoints.length);
    }

    /**
     *Dessine la flèche d'implémentation
     * @param gc contexte graphique
     * @param x coordonnée X de la Classe
     * @param y coordonnée Y de la Classe
     */
    private void dessinerFlecheImplementation(GraphicsContext gc, double x, double y) {
        gc.setStroke(Color.BLACK);
        gc.setLineDashes(5);
        gc.strokeLine(x, y, x - 10, y - 5);
        gc.setLineDashes(0);
    }

    /**
     * Dessine la flèche d'association
     * @param gc contexte graphique
     * @param x coordonnée X de la Classe
     * @param y coordonnée Y de la Classe
     */
    private void dessinerFlecheAssociation(GraphicsContext gc, double x, double y) {
        gc.setStroke(Color.BLACK);
        gc.strokeLine(x, y, x - 10, y - 5);
    }

    /**
     * Renvoie la largeur de la classe à dessiner
     * @param classe classe concernée
     * @return largeur en px
     */
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

    /**
     * Renvoie la hauteur de la classe à dessiner
     * @param classe classe concernée
     * @return hauteur en px
     */
    public double getHauteurClasse(Classe classe) {
        return classe.getAttributs().size() + classe.getMethodes().size() + 20;
    }
}