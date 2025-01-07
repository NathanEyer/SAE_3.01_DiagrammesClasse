package diagrammes;

import diagrammes.controleur.ControleurBoutons;
import diagrammes.vue.VueDiagramme;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

/**
 * Permet de raccourcir le Main.java
 */
public class Interface {

    /**
     * Crée la barre de menu avec ses éléments
     * @param controleurBoutons contrôleur des boutons pour gérer les actions
     * @return MenuBar configurée
     */
    public static MenuBar createMenuBar(ControleurBoutons controleurBoutons) {
        MenuBar menu = new MenuBar();

        // Menu Fichier
        Menu fichier = new Menu("Fichier");

        // Bouton Importer
        MenuItem importer = new MenuItem("Importer");
        importer.setOnAction(controleurBoutons);

        // Menu Exporter
        Menu exporter = new Menu("Exporter");
        MenuItem exporterPng = new MenuItem("Exporter en PNG");
        exporterPng.setOnAction(controleurBoutons);
        MenuItem exporterUml = new MenuItem("Exporter en UML");
        exporterUml.setOnAction(controleurBoutons);
        exporter.getItems().addAll(exporterPng, exporterUml);

        //Menu Edit
        Menu editer  = new Menu("Éditer");
        MenuItem reinitialiser = new MenuItem("Réinitialiser");
        reinitialiser.setOnAction(controleurBoutons);
        MenuItem nouveau = new MenuItem("Nouveau");
        nouveau.setOnAction(controleurBoutons);
        editer.getItems().addAll(reinitialiser, nouveau);


        Menu creer = new Menu("Créer");
        MenuItem creerNvClasse = new MenuItem("Créer une nouvelle classe");
        creerNvClasse.setOnAction(controleurBoutons);
        creer.getItems().add(creerNvClasse);



        fichier.getItems().addAll(importer, exporter);


        menu.getMenus().addAll(fichier, editer, creer);

        return menu;
    }

    /**
     * Crée la barre supérieure
     * @param menuBar menu
     * @return XBox
     */
    public static HBox createTopBar(MenuBar menuBar) {
        HBox topBar = new HBox(menuBar);
        topBar.setStyle("-fx-background-color: lightgray;");
        return topBar;
    }

    public static void ajouterZoomDeplacement(VueDiagramme vue){
        vue.setOnScroll(event -> {
            if (event.isControlDown()) {
                double zoomFactor = 1.05;
                if (event.getDeltaY() < 0) {
                    zoomFactor = 0.8;
                }

                // Appliquer le zoom sur la vue
                vue.setScaleX(vue.getScaleX() * zoomFactor);
                vue.setScaleY(vue.getScaleY() * zoomFactor);
            }
        });

        vue.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                vue.setStartDragX(event.getSceneX());
                vue.setStartDragY(event.getSceneY());
            }
        });

        vue.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double deltaX = event.getSceneX() - vue.getStartDragX();
                double deltaY = event.getSceneY() - vue.getStartDragY();

                // Déplacer la vue
                vue.setTranslateX(vue.getTranslateX() + deltaX);
                vue.setTranslateY(vue.getTranslateY() + deltaY);

                vue.setStartDragX(event.getSceneX());
                vue.setStartDragY(event.getSceneY());
            }
        });
    }
}
