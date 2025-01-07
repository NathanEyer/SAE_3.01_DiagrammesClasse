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
    private final HashMap<Classe, Boolean> attributsMasques = new HashMap<>();
    private final HashMap<Classe, Boolean> methodesMasquees = new HashMap<>();
    private double startDragX;
    private double startDragY;

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

        boolean attributsMasquesActuels = attributsMasques.getOrDefault(classe, false);
        boolean methodesMasqueesActuelles = methodesMasquees.getOrDefault(classe, false);

        double hauteurAttributs = attributsMasquesActuels ? 0 : classe.getAttributs().size() * hauteurSection;
        double hauteurMethodes = methodesMasqueesActuelles ? 0 : classe.getMethodes().size() * hauteurSection;
        double hauteur = hauteurNom + hauteurAttributs + hauteurMethodes;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, largeur, hauteur);

        Color couleurFond = estInterface(classe) ? Color.LIGHTGREEN : Color.LIGHTBLUE;
        gc.setFill(couleurFond);
        gc.fillRect(x, y, largeur, hauteurNom);
        gc.strokeRect(x, y, largeur, hauteurNom);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 14));
        gc.fillText(classe.getNom(), x + padding, y + hauteurNom - 10);

        double currentY = y + hauteurNom;
        if (!attributsMasquesActuels) {
            for (Attribut attribut : classe.getAttributs()) {
                gc.fillText("- " + attribut.getNomAttribut() + " : " + attribut.getTypeAttribut(), x + padding, currentY + 15);
                currentY += hauteurSection;
            }
            gc.strokeLine(x, currentY, x + largeur, currentY);
        }

        if (!methodesMasqueesActuelles) {
            for (Methode methode : classe.getMethodes()) {
                gc.fillText("+ " + methode.getNomMethode() + "()", x + padding, currentY + 15);
                currentY += hauteurSection;
            }
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
            double[] start = getClosestPoint(positionsClasses.get(source), positionsClasses.get(cible));
            double[] end = getClosestPoint(positionsClasses.get(cible), positionsClasses.get(source));

            // Dessiner la ligne
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeLine(start[0], start[1], end[0], end[1]);

            // Dessiner la flèche
            double arrowLength = 15;
            double arrowWidth = 10;
            double angle = Math.atan2(end[1] - start[1], end[0] - start[0]);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double x1 = end[0] - arrowLength * cos + arrowWidth * sin;
            double y1 = end[1] - arrowLength * sin - arrowWidth * cos;
            double x2 = end[0] - arrowLength * cos - arrowWidth * sin;
            double y2 = end[1] - arrowLength * sin + arrowWidth * cos;

            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{end[0], x1, x2}, new double[]{end[1], y1, y2}, 3);
            gc.strokePolygon(new double[]{end[0], x1, x2}, new double[]{end[1], y1, y2}, 3);
        }
    }

    private void dessinerFlecheImplementation(GraphicsContext gc, Relation r) {
        Classe source = r.getDepart();
        Classe cible = r.getDestination();

        if (positionsClasses.containsKey(source) && positionsClasses.containsKey(cible)) {
            double[] start = getClosestPoint(positionsClasses.get(source), positionsClasses.get(cible));
            double[] end = getClosestPoint(positionsClasses.get(cible), positionsClasses.get(source));

            // Dessiner la ligne en pointillés
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.setLineDashes(5);
            gc.strokeLine(start[0], start[1], end[0], end[1]);
            gc.setLineDashes(0);

            // Dessiner la flèche
            double arrowLength = 15;
            double arrowWidth = 10;
            double angle = Math.atan2(end[1] - start[1], end[0] - start[0]);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double x1 = end[0] - arrowLength * cos + arrowWidth * sin;
            double y1 = end[1] - arrowLength * sin - arrowWidth * cos;
            double x2 = end[0] - arrowLength * cos - arrowWidth * sin;
            double y2 = end[1] - arrowLength * sin + arrowWidth * cos;

            gc.setFill(Color.WHITE);
            gc.fillPolygon(new double[]{end[0], x1, x2}, new double[]{end[1], y1, y2}, 3);
            gc.strokePolygon(new double[]{end[0], x1, x2}, new double[]{end[1], y1, y2}, 3);
        }
    }

    private void dessinerFlecheAssociation(GraphicsContext gc, Relation r) {
        Classe source = r.getDepart();
        Classe cible = r.getDestination();

        if (positionsClasses.containsKey(source) && positionsClasses.containsKey(cible)) {
            double[] start = getClosestPoint(positionsClasses.get(source), positionsClasses.get(cible));
            double[] end = getClosestPoint(positionsClasses.get(cible), positionsClasses.get(source));

            // Dessiner la ligne
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeLine(start[0], start[1], end[0], end[1]);

            // Dessiner la flèche
            double arrowLength = 15;
            double arrowWidth = 10;
            double angle = Math.atan2(end[1] - start[1], end[0] - start[0]);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double x1 = end[0] - arrowLength * cos + arrowWidth * sin;
            double y1 = end[1] - arrowLength * sin - arrowWidth * cos;
            double x2 = end[0] - arrowLength * cos - arrowWidth * sin;
            double y2 = end[1] - arrowLength * sin + arrowWidth * cos;

            gc.strokeLine(end[0], end[1], x1, y1);
            gc.strokeLine(end[0], end[1], x2, y2);
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

        return maxLength + 35;
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
                        attributsMasques.remove(classeCible); // Supprime l'état des attributs
                        methodesMasquees.remove(classeCible); // Supprime l'état des méthodes
                        dessinerDiagramme(); // Rafraîchit l'affichage
                    });

                    boolean attributsMasquesActuels = attributsMasques.getOrDefault(classeCible, false);
                    MenuItem masquerAttributs = new MenuItem(attributsMasquesActuels ? "Démasquer Attributs" : "Masquer Attributs");
                    masquerAttributs.setOnAction(e -> {
                        attributsMasques.put(classeCible, !attributsMasquesActuels);
                        dessinerDiagramme(); // Rafraîchit l'affichage
                    });

                    boolean methodesMasqueesActuelles = methodesMasquees.getOrDefault(classeCible, false);
                    MenuItem masquerMethodes = new MenuItem(methodesMasqueesActuelles ? "Démasquer Méthodes" : "Masquer Méthodes");
                    masquerMethodes.setOnAction(e -> {
                        methodesMasquees.put(classeCible, !methodesMasqueesActuelles);
                        dessinerDiagramme(); // Rafraîchit l'affichage
                    });

                    // Ajoutez les options au menu
                    contextMenu.getItems().addAll(supprimer, masquerAttributs, masquerMethodes);

                    // Affichez le menu contextuel
                    contextMenu.show(this, event.getScreenX(), event.getScreenY());

                    // Enregistrer le menu actif
                    this.getProperties().put("activeMenu", contextMenu);
                    return; // Stoppe la recherche après avoir trouvé la classe cible
                }
            }
        }
    }

    /**
     * Calcule le point le plus proche sur le rectangle cible à partir du rectangle source
     * @param sourceRect rectangle source
     * @param targetRect rectangle cible
     * @return tableau contenant les coordonnées X et Y du point le plus proche
     */
    private double[] getClosestPoint(Rectangle sourceRect, Rectangle targetRect) {
        double sourceCenterX = sourceRect.getX() + sourceRect.getWidth() / 2;
        double sourceCenterY = sourceRect.getY() + 15; // Center of the title rectangle

        double targetX = targetRect.getX();
        double targetY = targetRect.getY();
        double targetWidth = targetRect.getWidth();
        double targetHeight = 30; // Height of the title rectangle

        double closestX = targetX;
        double closestY = targetY;

        if (sourceCenterX > targetX + targetWidth) {
            closestX = targetX + targetWidth;
        } else if (sourceCenterX > targetX) {
            closestX = sourceCenterX;
        }

        if (sourceCenterY > targetY + targetHeight) {
            closestY = targetY + targetHeight;
        } else if (sourceCenterY > targetY) {
            closestY = sourceCenterY;
        }

        return new double[]{closestX, closestY};
    }

    public double getStartDragX() {
        return startDragX;
    }

    public void setStartDragX(double startDragX) {
        this.startDragX = startDragX;
    }

    public double getStartDragY() {
        return startDragY;
    }

    public void setStartDragY(double startDragY) {
        this.startDragY = startDragY;
    }
}