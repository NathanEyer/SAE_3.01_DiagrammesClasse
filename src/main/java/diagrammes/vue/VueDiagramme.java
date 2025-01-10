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
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * VueDiagramme affiche le diagramme à l'utilisateur en utilisant un Canvas.
 */
public class VueDiagramme extends Canvas implements Observateur {
    /**
     * Attributs
     */
    private final ModeleDiagramme modele;
    public final static HashMap<Classe, Rectangle> positionsClasses = new HashMap<>();
    public final HashMap<Classe, Boolean> attributsMasques = new HashMap<>();
    public final HashMap<Classe, Boolean> methodesMasquees = new HashMap<>();
    public final HashMap<Relation, Boolean> relationsMasquees = new HashMap<>();
    private static Label messageLabel;

    /**
     * Initialise le diagramme
     * @param modeleDiagramme Le modèle contenant les données du diagramme
     */
    public VueDiagramme(ModeleDiagramme modeleDiagramme){
        super(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.modele = modeleDiagramme;
        VueDiagramme.messageLabel = new Label();
        this.modele.enregistrerObservateur(this);


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
        double y = Math.random()*(Main.SCREEN_HEIGHT * 0.8), x = Math.random()*(Main.SCREEN_WIDTH * 0.5);

        // Dessiner chaque Classe
        List<Classe> classes = modele.getClasses();
        for (Classe classe : classes) {
            double largeur = getLargeurClasse(classe);
            double hauteur = getHauteurClasse(classe);

            double finalX = x;
            double finalY = y;
            Rectangle position = positionsClasses.computeIfAbsent(classe, c -> new Rectangle(finalX, finalY, largeur, hauteur));
            dessinerClasse(gc, classe, position.getX(), position.getY());
            position.setWidth(largeur);
            position.setHeight(hauteur);
            y = Math.random()*(Main.SCREEN_HEIGHT) / 1.2;
            x = Math.random()*(Main.SCREEN_WIDTH / 1.6);
        }

        for (Relation relation : modele.getRelations()) {
            dessinerRelation(gc, relation);
        }
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

        Color couleurFond = Color.LIGHTBLUE;
        if (estInterface(classe)) {
            couleurFond = Color.LIGHTGREEN;
        } else if (estParente(classe)) {
            couleurFond = Color.RED;
        } else if (estParente(classe) && estInterface(classe)) {
            couleurFond = Color.GREEN;
        }
        gc.setFill(couleurFond);
        gc.fillRect(x, y, largeur, hauteurNom);
        gc.strokeRect(x, y, largeur, hauteurNom);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 14));
        gc.fillText(classe.getNom(), x + padding, y + hauteurNom - 10);

        double currentY = y + hauteurNom;
        if (!attributsMasquesActuels) {
            for (Attribut attribut : classe.getAttributs()) {
                // Convertir le modificateur en signe
                String modificateur = convertirModificateur(attribut.getModificateur());
                gc.fillText(modificateur + " " + attribut.getNomAttribut() + " : " + attribut.getTypeAttribut(),
                        x + padding, currentY + 15);
                currentY += hauteurSection;
            }
            gc.strokeLine(x, currentY, x + largeur, currentY);
        }

        if (!methodesMasqueesActuelles) {
            for (Methode methode : classe.getMethodes()) {
                // Convertir le modificateur en signe et afficher les paramètres
                String modificateur = convertirModificateur(methode.getModificateur());
                String params = String.join(", ", methode.getParametres());
                String abstractStatic = " ";
                switch(methode.getAbstractStatic()){
                    case 1:
                        abstractStatic += "abstract ";
                        break;
                    case 2:
                        abstractStatic += "static ";
                        break;
                }

                String parametresAvecParentheses = "(" + params + ")";

                gc.fillText(modificateur + abstractStatic + methode.getNomMethode() + parametresAvecParentheses + " : " + methode.getTypeRetour(),
                        x + padding, currentY + 15);
                currentY += hauteurSection;
            }

        }
    }

    /**
     * Convertit un modificateur d'accès en son symbole UML.
     * @param modificateur Le modificateur d'accès sous forme de texte (e.g., "public", "private", "protected").
     * @return Le symbole correspondant ("+", "-", "#").
     */
    private String convertirModificateur(String modificateur) {
        return switch (modificateur) {
            case "public" -> "+";
            case "private" -> "-";
            case "protected" -> "#";
            default -> "";
        };
    }

    /**
     * Vérifie que c'est une interface
     * @param classe Classe concernée
     * @return boolean
     */
    private boolean estInterface(Classe classe) {
        for(Relation relation : modele.getRelations()) {
            if(relation.getDestination().equals(classe) && relation.getType() instanceof Implementation) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie que c'est une classe parente
     * @param classe Classe concernée
     * @return boolean
     */
    private boolean estParente(Classe classe) {
        for(Relation relation : modele.getRelations()) {
            if(relation.getDestination().equals(classe) && relation.getType() instanceof Heritage) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dessine une relation entre deux classes
     * @param gc contexte graphique
     * @param relation relation à dessiner
     */
    private void dessinerRelation(GraphicsContext gc, Relation relation) {
        if (relationsMasquees.getOrDefault(relation, false)) {
            return;
        }

        if (relation.getType() instanceof Heritage) {
            dessinerFlecheHeritage(gc, relation);
        } else if (relation.getType() instanceof Implementation) {
            dessinerFlecheImplementation(gc, relation);
        } else {
            dessinerFlecheAssociation(gc, relation);
        }
    }

    /**
     * Dessine une flèche d'héritage
     * @param gc contexte graphique
     * @param r relation à dessiner
     */
    private void dessinerFlecheHeritage(GraphicsContext gc, Relation r) {
        Classe source = r.getDepart();
        Classe cible = r.getDestination();
        if (positionsClasses.containsKey(source) && positionsClasses.containsKey(cible)) {
            double[] start = getClosestPoint(positionsClasses.get(cible), positionsClasses.get(source));
            double[] end = getClosestPoint(positionsClasses.get(source), positionsClasses.get(cible));

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

    /**
     * Dessine une flèche d'implémentation
     * @param gc contexte graphique
     * @param r relation à dessiner
     */
    private void dessinerFlecheImplementation(GraphicsContext gc, Relation r) {
        Classe source = r.getDepart();
        Classe cible = r.getDestination();

        if (positionsClasses.containsKey(source) && positionsClasses.containsKey(cible)) {
            double[] start = getClosestPoint(positionsClasses.get(cible), positionsClasses.get(source));
            double[] end = getClosestPoint(positionsClasses.get(source), positionsClasses.get(cible));

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

    /**
     * Dessine une flèche d'association
     * @param gc contexte graphique
     * @param r relation à dessiner
     */
    private void dessinerFlecheAssociation(GraphicsContext gc, Relation r) {
        Classe source = r.getDepart();
        Classe cible = r.getDestination();

        if (positionsClasses.containsKey(source) && positionsClasses.containsKey(cible)) {
            double[] start = getClosestPoint(positionsClasses.get(cible), positionsClasses.get(source));
            double[] end = getClosestPoint(positionsClasses.get(source), positionsClasses.get(cible));

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

            if (r.getAttribut() != null) {
                double textX = (start[0] + end[0]) / 2;
                double textY = (start[1] + end[1]) / 2;
                gc.setFill(Color.BLACK);
                gc.fillText(r.getAttribut(), textX,textY);
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
        double sourceCenterY = sourceRect.getY() + sourceRect.getHeight() / 2;

        double targetX = targetRect.getX();
        double targetY = targetRect.getY();
        double targetWidth = targetRect.getWidth();
        double targetHeight = targetRect.getHeight();

        double closestX = sourceCenterX;
        double closestY;

        if (sourceCenterX < targetX) {
            closestX = targetX;
        } else if (sourceCenterX > targetX + targetWidth) {
            closestX = targetX + targetWidth;
        }

        if (sourceCenterY < targetY) {
            closestY = targetY;
        } else if (sourceCenterY > targetY + targetHeight) {
            closestY = targetY + targetHeight - 10;
        } else {
            closestY = Math.max(targetY, Math.min(sourceCenterY, targetY + targetHeight));
        }
        return new double[]{closestX, closestY};
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
            String type = switch (methode.getAbstractStatic()) {
                case 1 -> "abstract";
                case 2 -> "static";
                default -> "";
            };
            StringBuilder s = new StringBuilder("+ " + type + methode.getNomMethode() + "(");
            for(String p: methode.getParametres()){
                s.append(p).append(",");
            }
            s.append("): ").append(methode.getTypeRetour());
            text = new Text(s.toString());
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
        boolean attributsMasquesActuels = attributsMasques.getOrDefault(classe, false);
        boolean methodesMasqueesActuelles = methodesMasquees.getOrDefault(classe, false);
        double hauteurAttributs = attributsMasquesActuels ? 0 : classe.getAttributs().size() * 20;
        double hauteurMethodes = methodesMasqueesActuelles ? 0 : classe.getMethodes().size() * 20;
        return hauteurNom + hauteurAttributs + hauteurMethodes + 10;
    }



    /**
     * Met à jour le texte du message affiché en bas de l'écran.
     * @param message Le message à afficher.
     */
    public static void setMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Retourne le `Label` du message.
     * @return Label pour les messages utilisateur.
     */
    public Label getMessageLabel() {
        return messageLabel;
    }

    /**
     * Réinitialise les classes
     */
    public static void reinitialiser(){
        positionsClasses.clear();
    }



}