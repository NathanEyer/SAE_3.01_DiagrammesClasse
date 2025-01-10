package diagrammes.controleur;

import diagrammes.classe.Classe;
import diagrammes.modele.Diagramme;
import diagrammes.modele.ModeleDiagramme;
import diagrammes.relations.Relation;
import diagrammes.vue.VueDiagramme;
import diagrammes.vue.VueModifier;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;

import static diagrammes.vue.VueDiagramme.positionsClasses;
import static diagrammes.vue.VueDiagramme.setMessage;

public class ControleurSouris {

    public Classe classeSelectionnee = null;
    private double offsetX, offsetY;
    private VueDiagramme vueDiagramme;
    private ModeleDiagramme modele;


    /**
     *
     * @param vueDiagramme
     * @param modele
     */
    public ControleurSouris(VueDiagramme vueDiagramme, ModeleDiagramme modele) {
        this.vueDiagramme=  vueDiagramme;
        this.modele = modele;

        this.vueDiagramme.setOnMousePressed(this::gererMousePressed);
        this.vueDiagramme.setOnMouseDragged(this::gererMouseDragged);
        this.vueDiagramme.setOnMouseReleased(this::gererMouseReleased);
        this.vueDiagramme.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                gererClicDroit(event);
            } else {
                gererDoubleClic(event);
            }
        });
    }

    /**
     *
     * @param event
     */
    public void gererMousePressed(MouseEvent event) {

        double mouseX = event.getX();
        double mouseY = event.getY();

        for (var entry : positionsClasses.entrySet()) {
            Rectangle rect = entry.getValue();
            System.out.println("Clic à (" + mouseX + ", " + mouseY + "), Rectangle : (" + rect.getX() + ", " + rect.getY() + ")");
            if (rect.contains(mouseX, mouseY)) {
                classeSelectionnee = entry.getKey();
                offsetX = mouseX - rect.getX();
                offsetY = mouseY - rect.getY();
                break;
            }
        }
        System.out.println("Souris pressée à la position : " + event.getX() + ", " + event.getY());
    }

    /**
     *
     * @param event
     */
    public void gererMouseDragged(MouseEvent event) {
        if (classeSelectionnee != null) {
            double newX = event.getX() - offsetX;
            double newY = event.getY() - offsetY;

            Rectangle rect = positionsClasses.get(classeSelectionnee);
            rect.setX(newX);
            rect.setY(newY);

            this.vueDiagramme.dessinerDiagramme();
        }
    }

    /**
     *
     * @param event
     */
    public void gererMouseReleased(MouseEvent event) {
        classeSelectionnee = null;
        System.out.println("Souris relâchée à la position : " + event.getX() + ", " + event.getY());
    }

    /**
     *
     * @param event
     */
    private void gererClicDroit(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) { // Vérifie que c'est un clic droit
            double mouseX = event.getX();
            double mouseY = event.getY();

            for (var entry : positionsClasses.entrySet()) {
                Rectangle rect = entry.getValue();
                if (rect.contains(mouseX, mouseY)) {
                    Classe classeCible = entry.getKey();

                    // Masquer tous les menus contextuels existants
                    ContextMenu existingMenu = (ContextMenu) vueDiagramme.getProperties().get("activeMenu");
                    if (existingMenu != null) {
                        existingMenu.hide();
                    }

                    // Créez un menu contextuel
                    ContextMenu contextMenu = new ContextMenu();

                    // Supprimer la classe
                    MenuItem supprimer = new MenuItem("Supprimer");
                    supprimer.setOnAction(e -> {
                        modele.getClasses().remove(classeCible);
                        positionsClasses.remove(classeCible);
                        this.vueDiagramme.attributsMasques.remove(classeCible);
                        this.vueDiagramme.methodesMasquees.remove(classeCible);
                        this.vueDiagramme.dessinerDiagramme();
                        setMessage("Classe supprimée : " + classeCible.getNom());
                    });

                    // Masquer/Démasquer les attributs
                    boolean attributsMasquesActuels = this.vueDiagramme.attributsMasques.getOrDefault(classeCible, false);
                    if (!classeCible.getAttributs().isEmpty()) {
                        MenuItem masquerAttributs = getMasquerAttributs(attributsMasquesActuels, classeCible);
                        contextMenu.getItems().add(masquerAttributs);
                    }

                    // Masquer/Démasquer les méthodes
                    if (!classeCible.getMethodes().isEmpty()) {
                        boolean methodesMasqueesActuelles = this.vueDiagramme.methodesMasquees.getOrDefault(classeCible, false);
                        MenuItem masquerMethodes = getMasquerMethodes(methodesMasqueesActuelles, classeCible);
                        contextMenu.getItems().add(masquerMethodes);
                    }

                    // Ajouter le bouton Modifier
                    MenuItem modifier = getModifier(classeCible);
                    contextMenu.getItems().add(modifier);

                    // Masquer/Démasquer les relations
                    boolean relationsMasqueesActuelles = modele.getRelations().stream()
                            .filter(relation -> relation.getDepart().equals(classeCible) || relation.getDestination().equals(classeCible))
                            .allMatch(relation -> this.vueDiagramme.relationsMasquees.getOrDefault(relation, false));

                    MenuItem masquerDemasquerRelations = getMasquerDemasquerRelations(relationsMasqueesActuelles, classeCible);

                    contextMenu.getItems().addAll(masquerDemasquerRelations, supprimer);
                    contextMenu.show(this.vueDiagramme, event.getScreenX(), event.getScreenY());

                    this.vueDiagramme.getProperties().put("activeMenu", contextMenu);
                    return;
                }
            }
        }
    }

    /**
     * Renvoie le MenuItem de MasquerDemasquerRelations
     * @param relationsMasqueesActuelles boolean
     * @param classeCible classe concernée
     * @return MenuItem
     */
    public MenuItem getMasquerDemasquerRelations(boolean relationsMasqueesActuelles, Classe classeCible) {
        MenuItem masquerDemasquerRelations = new MenuItem(
                relationsMasqueesActuelles ? "Démasquer Relations" : "Masquer Relations"
        );
        masquerDemasquerRelations.setOnAction(e -> {
            for (Relation relation : modele.getRelations()) {
                if (relation.getDepart().equals(classeCible) || relation.getDestination().equals(classeCible)) {
                    this.vueDiagramme.relationsMasquees.put(relation, !relationsMasqueesActuelles);
                }
            }
            this.vueDiagramme.dessinerDiagramme();
            setMessage(relationsMasqueesActuelles ? "Relations démasquées pour : " + classeCible.getNom() : "Relations masquées pour : " + classeCible.getNom());
        });
        return masquerDemasquerRelations;
    }

    /**
     * Renvoie le menuItem de Modifier
     * @param classeCible classe concernée
     * @return MenuItem
     */
    private MenuItem getModifier(Classe classeCible) {
        MenuItem modifier = new MenuItem("Modifier");
        modifier.setOnAction(e -> {
            List<Relation> relations = modele.getRelations(); // Vérifiez que cette liste est mutable
            List<Classe> autresClasses = modele.getClasses().stream()
                    .filter(c -> !c.equals(classeCible))
                    .toList();
            VueModifier vueModifier = new VueModifier(classeCible);
            Classe classeModifiee = vueModifier.afficher(relations, autresClasses);


            int indexClasse = modele.getClasses().indexOf(classeCible);
            if (indexClasse >= 0) {
                modele.getClasses().set(indexClasse, classeModifiee);
                this.vueDiagramme.dessinerDiagramme();
                setMessage("Classe modifiée : " + classeModifiee.getNom());
            }
        });
        return modifier;
    }

    /**
     * Renvoie le menuItem de masquerMethodes
     * @param methodesMasqueesActuelles boolean
     * @param classeCible classe concernée
     * @return MenuItem
     */
    private MenuItem getMasquerMethodes(boolean methodesMasqueesActuelles, Classe classeCible) {
        MenuItem masquerMethodes = new MenuItem(methodesMasqueesActuelles ? "Démasquer Méthodes" : "Masquer Méthodes");
        masquerMethodes.setOnAction(e -> {
            this.vueDiagramme.methodesMasquees.put(classeCible, !methodesMasqueesActuelles);
            this.vueDiagramme.dessinerDiagramme();
            setMessage(methodesMasqueesActuelles ? "Méthodes démasquées pour : " + classeCible.getNom() : "Méthodes masquées pour : " + classeCible.getNom());
        });
        return masquerMethodes;
    }

    /**
     * Renvoie le menuItem de masquerAttributs
     * @param attributsMasquesActuels boolean
     * @param classeCible classe concernée
     * @return MenuItem
     */
    private MenuItem getMasquerAttributs(boolean attributsMasquesActuels, Classe classeCible) {
        MenuItem masquerAttributs = new MenuItem(attributsMasquesActuels ? "Démasquer Attributs" : "Masquer Attributs");
        masquerAttributs.setOnAction(e -> {
            this.vueDiagramme.attributsMasques.put(classeCible, !attributsMasquesActuels);
            this.vueDiagramme.dessinerDiagramme();
            setMessage(attributsMasquesActuels ? "Attributs démasqués pour : " + classeCible.getNom() : "Attributs masqués pour : " + classeCible.getNom());
        });
        return masquerAttributs;
    }


    /**
     *
     * @param event
     */
    public void gererDoubleClic(MouseEvent event) {
        if (event.getClickCount() == 2) {
            double mouseX = event.getX();
            double mouseY = event.getY();

            System.out.println("Double clic détecté à : (" + mouseX + ", " + mouseY + ")");

            for (var entry : positionsClasses.entrySet()) {
                Rectangle rect = entry.getValue();
                System.out.println("Rectangle : (" + rect.getX() + ", " + rect.getY() + ", "
                        + rect.getWidth() + ", " + rect.getHeight() + ")");

                if (rect.contains(mouseX, mouseY)) {
                    Classe classeCible = entry.getKey();
                    boolean attributsMasquesActuels = this.vueDiagramme.attributsMasques.getOrDefault(classeCible, false);
                    boolean methodesMasqueesActuelles = this.vueDiagramme.methodesMasquees.getOrDefault(classeCible, false);

                    this.vueDiagramme.attributsMasques.put(classeCible, !attributsMasquesActuels);
                    this.vueDiagramme.methodesMasquees.put(classeCible, !methodesMasqueesActuelles);

                    this.vueDiagramme.dessinerDiagramme();

                    setMessage((attributsMasquesActuels ? "Attributs démasqués" : "Attributs masqués") +
                            " et " + (methodesMasqueesActuelles ? "Méthodes démasquées" : "Méthodes masquées") +
                            " pour : " + classeCible.getNom());

                    System.out.println("Double clic traité pour la classe : " + classeCible.getNom());
                    return;
                }
            }

            System.out.println("Aucune classe trouvée pour ce double clic.");
        }
    }

}
