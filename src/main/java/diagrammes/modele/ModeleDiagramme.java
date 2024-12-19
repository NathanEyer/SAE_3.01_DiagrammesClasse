package diagrammes.modele;

import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import diagrammes.classe.Attribut;
import diagrammes.exporter.Exporter;
import diagrammes.exporter.ExporterImage;
import diagrammes.exporter.ExporterUml;
import diagrammes.relations.Relation;
import diagrammes.vue.Observateur;

import javafx.stage.FileChooser;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    /** Classe actuellement sélectionnée. */
    private Classe classeSelectionnee;

    /**
     * Constructeur par défaut.
     * Initialise les listes des classes, relations et observateurs.
     */
    public ModeleDiagramme() {
        this.classes = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.observateurs = new ArrayList<>();
        this.classeSelectionnee = null;
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
     * Sélectionne une classe en fonction de son nom.
     *
     * @param nomClasse Le nom de la classe à sélectionner.
     */
    public void selectionnerClasse(String nomClasse) {
        for (Classe classe : classes) {
            if (classe.getNom().equals(nomClasse)) {
                this.classeSelectionnee = classe;
                System.out.println("Classe sélectionnée : " + classe.getNom());
                notifierObservateur();
                return;
            }
        }
        System.out.println("Aucune classe trouvée avec le nom : " + nomClasse);
    }

    /**
     * Déplace la classe sélectionnée.
     * Ici, la logique peut être enrichie avec des métadonnées liées à la position.
     *
     * @param x Nouvelle coordonnée X.
     * @param y Nouvelle coordonnée Y.
     */
    public void deplacerClasseSelectionnee(double x, double y) {
        if (classeSelectionnee != null) {
            System.out.println("Déplacement de la classe : " + classeSelectionnee.getNom());
            notifierObservateur();
        } else {
            System.out.println("Aucune classe sélectionnée pour le déplacement.");
        }
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
            // Charger dynamiquement la classe
            Class<?> classe = Class.forName(cheminClasse);

            // Obtenir le nom de la classe
            String nomClasse = classe.getSimpleName();
            Classe nouvelleClasse = new Classe(nomClasse);

            // Obtenir les attributs
            Field[] fields = classe.getDeclaredFields();
            for (Field field : fields) {
                String typeAttribut = field.getType().getSimpleName();
                String nomAttribut = field.getName();
                nouvelleClasse.ajouterAttribut(new Attribut(nomAttribut, typeAttribut));
            }

            // Obtenir les méthodes
            Method[] methods = classe.getDeclaredMethods();
            for (Method method : methods) {
                String typeRetour = method.getReturnType().getSimpleName();
                String nomMethode = method.getName();
                List<String> parametres = new ArrayList<>();
                for (Class<?> paramType : method.getParameterTypes()) {
                    parametres.add(paramType.getSimpleName());
                }
                nouvelleClasse.ajouterMethode(new Methode(nomMethode, typeRetour, parametres));
            }

            // Ajouter la classe analysée au modèle
            addClass(nouvelleClasse);

            System.out.println("Classe analysée : " + nomClasse);
            System.out.println("Attributs : " + nouvelleClasse.getAttributs());
            System.out.println("Méthodes : " + nouvelleClasse.getMethodes());

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

    // Méthodes pour le patron Observateur
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

    /**
     * Retourne la liste des classes dans le diagramme.
     *
     * @return Liste des classes.
     */
    public List<Classe> getClasses() {
        return classes;
    }

    /**
     * Retourne la liste des relations dans le diagramme.
     *
     * @return Liste des relations.
     */
    public List<Relation> getRelations() {
        return relations;
    }
}
