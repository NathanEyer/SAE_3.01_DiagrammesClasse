package diagrammes;

import diagrammes.controleur.ControleurBoutons;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

/**
 * Permet de raccourcir le Main.java
 */
public class Interface {

    /**
     * Crée la barre de menu avec ses éléments.
     *
     * @param controleurBoutons Le contrôleur des boutons pour gérer les actions.
     * @return Une MenuBar configurée.
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

        // Ajout des éléments au menu Fichier
        fichier.getItems().addAll(importer, exporter);
        menu.getMenus().addAll(fichier, editer);

        return menu;
    }

    /**
     * Crée la barre supérieure
     * @param menuBar
     * @return
     */
    public static HBox createTopBar(MenuBar menuBar) {
        HBox topBar = new HBox(menuBar);
        topBar.setStyle("-fx-background-color: lightgray;");
        return topBar;
    }
}
