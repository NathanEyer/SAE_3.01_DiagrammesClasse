package diagrammes.modele;

import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import diagrammes.classe.Attribut;
import diagrammes.exporter.Exporter;
import diagrammes.exporter.ExporterImage;
import diagrammes.exporter.ExporterUml;
import diagrammes.exporter.ChargementClasse;
import diagrammes.relations.Relation;
import diagrammes.vue.Observateur;

import javafx.stage.FileChooser;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * ModeleDiagramme gère les données et la logique métier du diagramme UML.
 * Il suit le patron Observateur pour notifier les vues des modifications.
 */
public class ModeleDiagramme implements Diagramme {

    /** Liste des classes dans le diagramme. */
    private List<Classe> classes;

    /** Liste des relations entre les classes. */
    private List<Relation> relations;

    /** Liste des observateurs enregistrés pour être notifiés des changements. */
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
     *
     * @param classe La classe à ajouter.
     */
    public void addClass(Classe classe) {
        classes.add(classe);
        notifierObservateur();
    }

    /**
     * Ajoute une relation entre les classes.
     *
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
     * @param cheminClasse Le nom complet de la classe (package inclus).
     */
    public void analyserFichierClass(String cheminClasse) {
        try {
            //Charge la bonne classe
            Path path = Paths.get(cheminClasse);
            String goodName = ChargementClasse.getGoodName(path);
            ChargementClasse chargementClasse = new ChargementClasse(path);
            Class<?> classe = chargementClasse.loadClass(goodName);
            Classe nouvelleClasse = new Classe(classe.getSimpleName());

            //Ajout des attributs
            for (Field field : classe.getDeclaredFields()) {
                nouvelleClasse.ajouterAttribut(
                        new Attribut(field.getName(), field.getType().getSimpleName())
                );
            }

            //Ajout des méthodes
            for (Method method : classe.getDeclaredMethods()) {
                List<String> parametres = new ArrayList<>();
                for (Class<?> paramType : method.getParameterTypes()) {
                    parametres.add(paramType.getSimpleName());
                }
                nouvelleClasse.ajouterMethode(
                        new Methode(method.getName(), method.getReturnType().getSimpleName(), parametres)
                );
            }

            //Ajout de la classe
            addClass(nouvelleClasse);
            System.out.println("Classe analysée : " + classe.getSimpleName());
        } catch (ClassNotFoundException e) {
            System.out.println("Classe non trouvée : " + cheminClasse);
            e.printStackTrace();
        }
    }





    /**
     * Exporte le diagramme dans un format spécifique.
     *
     * @param format Le format d'exportation (ex : PNG, PlantUML).
     * @return true si l'exportation a réussi, false sinon.
     */
    public boolean exporter(String format) {
        try {
            Exporter exporter = null;
            if ("PNG".equalsIgnoreCase(format) ) {
                exporter = new ExporterImage();
            }else if("PlantUML".equalsIgnoreCase(format)){
                exporter = new ExporterUml();
            }else return false;
            exporter.exporter("", this);                // A COMPLETER !!!!
            System.out.println("Exportation réussie avec succès !");
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public List<Classe> getClasses() {
        return classes;
    }

    public List<Relation> getRelations() {
        return relations;
    }
}
