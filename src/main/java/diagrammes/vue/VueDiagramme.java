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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
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
    public VueDiagramme(ModeleDiagramme modeleDiagramme) throws ClassNotFoundException {
        super(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.modele = modeleDiagramme;
        this.modele.enregistrerObservateur(this);

        this.setOnMousePressed(this::gererMousePressed);
        this.setOnMouseDragged(this::gererMouseDragged);
        this.setOnMouseReleased(this::gererMouseReleased);
        this.setOnMouseClicked(this::gererClicDroit);
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

        for (Relation relation : modele.getRelations()) {
            dessinerRelation(gc, relation);
        }
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

        // Appliquer la couleur en fonction du type de la classe (interface ou classe normale)
        Color couleurFond = estInterface(classe) ? Color.LIGHTGREEN : Color.LIGHTBLUE;
        gc.setFill(couleurFond);
        gc.fillRect(x, y, largeur, hauteurNom);
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

    private boolean estInterface(Classe classe) {
        try {
            Class<?> clas = Class.forName(classe.getNom());
            return clas.isInterface();
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Dessine une relation entre deux classes
     * @param gc contexte graphique
     * @param relation relation à dessiner
     */
    private void dessinerRelation(GraphicsContext gc, Relation relation) {
        if (relation.getType() instanceof Heritage) {
            dessinerFlecheHeritage(gc, relation);
        } else if (relation.getType() instanceof Implementation) {
            dessinerFlecheImplementation(gc, relation);
        } else {
            dessinerFlecheAssociation(gc, relation);
        }
    }

    private void dessinerFlecheHeritage(GraphicsContext gc, Relation r) {
        Classe source = r.getDepart();
        Classe cible = r.getDestination();
        if (positionsClasses.containsKey(source) && positionsClasses.containsKey(cible)) {
            Rectangle rectSource = positionsClasses.get(source);
            Rectangle rectCible = positionsClasses.get(cible);

            double startX = rectSource.getX() + rectSource.getWidth();
            double startY = rectSource.getY() + rectSource.getHeight() / 2;
            double endX = rectCible.getX();
            double endY = rectCible.getY() + rectCible.getHeight() / 2;

            // Dessiner la ligne
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeLine(startX, startY, endX, endY);

            // Dessiner la flèche
            double arrowLength = 10;
            double arrowWidth = 5;
            double angle = Math.atan2(endY - startY, endX - startX);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double x1 = endX - arrowLength * cos + arrowWidth * sin;
            double y1 = endY - arrowLength * sin - arrowWidth * cos;
            double x2 = endX - arrowLength * cos - arrowWidth * sin;
            double y2 = endY - arrowLength * sin + arrowWidth * cos;

            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{endX, x1, x2}, new double[]{endY, y1, y2}, 3);
            gc.strokePolygon(new double[]{endX, x1, x2}, new double[]{endY, y1, y2}, 3);
        }
    }

    private void dessinerFlecheImplementation(GraphicsContext gc, Relation r) {
        Classe source = r.getDepart();
        Classe cible = r.getDestination();

        if (positionsClasses.containsKey(source) && positionsClasses.containsKey(cible)) {
            Rectangle rectSource = positionsClasses.get(source);
            Rectangle rectCible = positionsClasses.get(cible);

            double startX = rectSource.getX() + rectSource.getWidth();
            double startY = rectSource.getY() + rectSource.getHeight() / 2;
            double endX = rectCible.getX();
            double endY = rectCible.getY() + rectCible.getHeight() / 2;

            // Dessiner la ligne en pointillés
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.setLineDashes(5);
            gc.strokeLine(startX, startY, endX, endY);
            gc.setLineDashes(0);

            // Dessiner la flèche
            double arrowLength = 10;
            double arrowWidth = 5;
            double angle = Math.atan2(endY - startY, endX - startX);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double x1 = endX - arrowLength * cos + arrowWidth * sin;
            double y1 = endY - arrowLength * sin - arrowWidth * cos;
            double x2 = endX - arrowLength * cos - arrowWidth * sin;
            double y2 = endY - arrowLength * sin + arrowWidth * cos;

            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{endX, x1, x2}, new double[]{endY, y1, y2}, 3);
            gc.strokePolygon(new double[]{endX, x1, x2}, new double[]{endY, y1, y2}, 3);
        }
    }

    private void dessinerFlecheAssociation(GraphicsContext gc, Relation r) {
        Classe source = r.getDepart();
        Classe cible = r.getDestination();

        if (positionsClasses.containsKey(source) && positionsClasses.containsKey(cible)) {
            Rectangle rectSource = positionsClasses.get(source);
            Rectangle rectCible = positionsClasses.get(cible);

            double startX = rectSource.getX() + rectSource.getWidth();
            double startY = rectSource.getY() + rectSource.getHeight() / 2;
            double endX = rectCible.getX();
            double endY = rectCible.getY() + rectCible.getHeight() / 2;

            // Dessiner la ligne
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeLine(startX, startY, endX, endY);

            // Dessiner la flèche
            double arrowLength = 10;
            double arrowWidth = 5;
            double angle = Math.atan2(endY - startY, endX - startX);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double x1 = endX - arrowLength * cos + arrowWidth * sin;
            double y1 = endY - arrowLength * sin - arrowWidth * cos;
            double x2 = endX - arrowLength * cos - arrowWidth * sin;
            double y2 = endY - arrowLength * sin + arrowWidth * cos;

            gc.strokeLine(endX, endY, x1, y1);
            gc.strokeLine(endX, endY, x2, y2);
        }
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
        double hauteurNom = 30; // Hauteur du titre
        double hauteurAttributs = classe.getAttributs().size() * 20;
        double hauteurMethodes = classe.getMethodes().size() * 20;
        return hauteurNom + hauteurAttributs + hauteurMethodes + 10; // Ajout d'un padding
    }

    private void gererClicDroit(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) { // Vérifie que c'est un clic droit
            double mouseX = event.getX();
            double mouseY = event.getY();
            for (var entry : positionsClasses.entrySet()) {
                Rectangle rect = entry.getValue();
                if (rect.contains(mouseX, mouseY)) {
                    Classe classeCible = entry.getKey();

                    // Masquer tous les menus contextuels existants
                    ContextMenu existingMenu = (ContextMenu) this.getProperties().get("activeMenu");
                    if (existingMenu != null) {
                        existingMenu.hide();
                    }

                    // Créez un menu contextuel
                    ContextMenu contextMenu = new ContextMenu();

                    MenuItem supprimer = new MenuItem("Supprimer");
                    supprimer.setOnAction(e -> {
                        modele.getClasses().remove(classeCible); // Supprime la classe du modèle
                        positionsClasses.remove(classeCible); // Supprime de la vue
                        dessinerDiagramme(); // Rafraîchit l'affichage
                    });

                    // Ajoutez l'option au menu
                    contextMenu.getItems().add(supprimer);

                    // Affichez le menu contextuel
                    contextMenu.show(this, event.getScreenX(), event.getScreenY());

                    // Enregistrer le menu actif
                    this.getProperties().put("activeMenu", contextMenu);
                    return; // Stoppe la recherche après avoir trouvé la classe cible
                }
            }
        }
    }
}