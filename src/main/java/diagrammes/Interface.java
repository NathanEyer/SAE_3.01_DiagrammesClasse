package diagrammes;

import diagrammes.controleur.ControleurBoutons;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static diagrammes.Main.SCREEN_HEIGHT;
import static diagrammes.Main.SCREEN_WIDTH;

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
        MenuItem creer = new MenuItem("Ajouter une classe");
        creer.setOnAction(controleurBoutons);
        editer.getItems().addAll(creer, reinitialiser);

        Menu aide = new Menu("Aide");
        MenuItem afficherAide = new MenuItem("Afficher l'aide");
        afficherAide.setOnAction(e -> afficherAide());

        aide.getItems().add(afficherAide);

        fichier.getItems().addAll(exporter);
        menu.getMenus().addAll(fichier, editer, aide);
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


    /**
     * Affiche l'aide
     */
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
                + "- Pour avoir plus de fonctionnalités, cliquez droit sur une classe et vous pourrez la modifier a votre guise.\n"
                + "Si vous avez des questions supplémentaires, consultez la documentation ou contactez le support.\n"
                + "Lorsqu'une classe est de couleur verte, c'est une interface. Lorsqu'elle est de couleur rouge, c'est une classe parent et lorsqu'elle est bleu c'est une classe normale.\n\n"
                + "Si vous double cliquez sur une classe, alors les attributs et les méthodes seront masqués, si vous double cliquez a nouveau, les attributs et les méthodes se démasqueront.";


        TextArea textAreaAide = new TextArea(contenuAide);
        textAreaAide.setEditable(false);
        textAreaAide.setWrapText(true);
        textAreaAide.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        Scene sceneAide = new Scene(textAreaAide, SCREEN_WIDTH * 0.5, SCREEN_HEIGHT * 0.7);

        aideStage.setScene(sceneAide);
        aideStage.setResizable(true);
        aideStage.show();
    }
}
