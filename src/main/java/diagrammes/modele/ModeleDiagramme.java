package diagrammes.modele;

import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import diagrammes.classe.Attribut;
import diagrammes.fichier.Exporter;
import diagrammes.fichier.ExporterImage;
import diagrammes.fichier.ChargementClasse;
import diagrammes.fichier.ExporterUml;
import diagrammes.relations.Relation;
import diagrammes.vue.Observateur;
import diagrammes.vue.VueDiagramme;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * ModeleDiagramme gère les données et la logique métier du diagramme UML.
 * Il suit le patron Observateur pour notifier les vues des modifications.
 */
public class ModeleDiagramme implements Diagramme {

    private List<Classe> classes;
    private List<Relation> relations;
    private List<Observateur> observateurs;

    /**
     * Constructeur par défaut.
     * Initialise les listes des classes, relations et observateurs.
     */
    public ModeleDiagramme() {
        this.classes = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.observateurs = new ArrayList<>();
    }

    /**
     * Ajoute une classe au diagramme.
     * @param classe La classe à ajouter.
     */
    public void addClass(Classe classe) {
        classes.add(classe);
        notifierObservateur();
    }

    /**
     * Ajoute une relation entre les classes.
     * @param relation La relation à ajouter.
     */
    public void addRelation(Relation relation) {
        relations.add(relation);
        notifierObservateur();
    }

    /**
     * Ouvre un explorateur pour sélectionner un fichier .class
     * et lance l'analyse introspective du fichier.
     */
    public void importerFichierClass() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer un fichier .class");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers Class", "*.class")
        );

        File fichierSelectionne = fileChooser.showOpenDialog(null);
        if (fichierSelectionne != null) {
            String cheminClasse = fichierSelectionne.getPath()
                    .replace("\\", ".")
                    .replace(".class", "")
                    .replace("src.main.java.", ""); // Adapter le chemin pour la structure des packages

            analyserFichierClass(cheminClasse);
        }
    }

    /**
     * Analyse un fichier .class grâce à l'introspection pour extraire
     * les attributs et les méthodes.
     *
     * @param cheminFichierClass Le nom complet de la classe (package inclus).
     */
    public void analyserFichierClass(String cheminFichierClass) {
        try {
            File fichierClass = new File(cheminFichierClass);

            // Détecter le dossier parent comme racine
            File dossierParent = fichierClass.getParentFile();
            URL dossierParentURL = dossierParent.toURI().toURL();

            // Créer un ClassLoader pour ce dossier
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{dossierParentURL});

            // Obtenir le nom complet de la classe
            String nomClasse = ChargementClasse.getGoodName(fichierClass.toPath());

            // Charger la classe
            Class<?> classe = urlClassLoader.loadClass(nomClasse);

            // Analyse introspective
            Classe nouvelleClasse = new Classe(classe.getSimpleName());

            for (Field field : classe.getDeclaredFields()) {
                nouvelleClasse.ajouterAttribut(new Attribut(field.getName(), field.getType().getSimpleName()));
            }

            for (Method method : classe.getDeclaredMethods()) {
                List<String> parametres = new ArrayList<>();
                for (Class<?> paramType : method.getParameterTypes()) {
                    parametres.add(paramType.getSimpleName());
                }
                nouvelleClasse.ajouterMethode(new Methode(method.getName(), method.getReturnType().getSimpleName(), parametres));
            }

            // Ajouter la classe au modèle
            addClass(nouvelleClasse);
            System.out.println("Classe analysée : " + classe.getSimpleName());
        } catch (Exception e) {
            System.out.println("Erreur lors de l'analyse : " + cheminFichierClass);
            e.printStackTrace();
        }
    }

    public void reinitialiser(){
        classes.clear();
        relations.clear();
        this.notifierObservateur();
    }

    public void nouveau(){
    }

    /**
     * Exporte le diagramme dans un format spécifique.
     *
     * @param format Le format d'exportation (ex : PNG, PlantUML).
     * @return true si l'exportation a réussi, false sinon.
     */
    public boolean exporter(Stage stage, VueDiagramme vue, String format) {
        Exporter export = null;
        FileChooser fileChooser = new FileChooser();
        if(format.equalsIgnoreCase("PNG")) {
            export = new ExporterImage();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        }else if(format.equalsIgnoreCase("UML")) {
            export = new ExporterUml();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("puml files (*.puml)", "*.puml"));
        }

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                export.exporter(file.getAbsolutePath(), vue);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void enregistrerObservateur(Observateur o) {
        observateurs.add(o);
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        observateurs.remove(o);
    }

    @Override
    public void notifierObservateur() {
        for (Observateur observateur : observateurs) {
            observateur.actualiser(this);
        }
    }

    public List<Classe> getClasses() {return classes;}

    public List<Relation> getRelations() {
        return relations;
    }
}
