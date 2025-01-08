package diagrammes;

import diagrammes.controleur.ControleurBoutons;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
        MenuItem exporterJava = new MenuItem("Exporter en Java");
        exporterJava.setOnAction(controleurBoutons);
        exporter.getItems().addAll(exporterPng, exporterUml, exporterJava);

        //Menu Edit
        Menu editer  = new Menu("Éditer");
        MenuItem reinitialiser = new MenuItem("Réinitialiser");
        reinitialiser.setOnAction(controleurBoutons);
        editer.getItems().addAll(reinitialiser);

        Menu creer = new Menu("Créer");
        MenuItem creerNvClasse = new MenuItem("Créer une nouvelle classe");
        creerNvClasse.setOnAction(controleurBoutons);
        creer.getItems().add(creerNvClasse);

        Menu masquer = new Menu("Masquer");
        MenuItem masquerToutMethodes = new MenuItem("Masquer toutes les méthodes");
        MenuItem masquerToutAttributs = new MenuItem("Masquer tout les attributs");

        masquerToutAttributs.setOnAction(controleurBoutons);
        masquerToutMethodes.setOnAction(controleurBoutons);

        masquer.getItems().addAll(masquerToutMethodes, masquerToutAttributs);

        Menu aide = new Menu("Aide");
        // Item pour ouvrir la page d'aide
        MenuItem afficherAide = new MenuItem("Afficher l'aide");
        afficherAide.setOnAction(e -> afficherAide());

        aide.getItems().add(afficherAide);

        fichier.getItems().addAll(importer, exporter);
        menu.getMenus().addAll(fichier, editer, creer , masquer , aide);
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


    public static void afficherAide() {

        Stage aideStage = new Stage();
        aideStage.setTitle("Aide - Application Diagramme");


        String contenuAide = "Bienvenue dans Banal UML !\n\n"
                + "Voici comment utiliser l'application :\n\n"
                + "- Pour créer une nouvelle classe, utilisez le menu 'Créer' et sélectionnez 'Créer une nouvelle classe'.\n"
                + "- Pour importer des données, utilisez l'option 'Importer' dans le menu 'Fichier', vous pouvez aussi simplement glisser des fichiers .class et notre application se chargera du reste'.\n"
                + "- Vous pouvez exporter votre diagramme en différents formats via le menu 'Exporter'.\n"
                + "- Pour masquer les méthodes ou attributs cliquez deux fois sur n'importe quelle classe'.\n"
                + "- Pour réinitialiser le diagramme, utilisez l'option 'Réinitialiser' dans le menu 'Éditer'.\n"
                + "- Pour avoir plus de fonctionnalités, cliquez droit sur une classe et vous pourrez la modifier a votre guise.\n\n"
                + "Si vous avez des questions supplémentaires, consultez la documentation ou contactez le support.";

        TextArea textAreaAide = new TextArea(contenuAide);
        textAreaAide.setEditable(false);
        textAreaAide.setWrapText(true);
        textAreaAide.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        Scene sceneAide = new Scene(textAreaAide, 400, 300);
        aideStage.setScene(sceneAide);
        aideStage.show();
    }
}
