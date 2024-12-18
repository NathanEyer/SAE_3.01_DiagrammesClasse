package diagrammes.modele;

import diagrammes.classe.Classe;
import diagrammes.relations.Relation;
import diagrammes.vue.Observateur;

import java.io.File;
import java.nio.file.FileSystems;
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
            // Logique pour associer les nouvelles coordonnées (à implémenter selon les besoins)
            notifierObservateur();
        } else {
            System.out.println("Aucune classe sélectionnée pour le déplacement.");
        }
    }

    /**
     * Importe un package ou un fichier Java.
     *
     * @param cheminFichier Le chemin du fichier ou du package à importer.
     */
    public void importerPackage(String cheminFichier) {
        File fichier = new File(cheminFichier);
        if (!fichier.exists()) {
            System.out.println("Fichier non trouvé : " + cheminFichier);
            return;
        }
        System.out.println(cheminFichier);
        String sep = FileSystems.getDefault().getSeparator();
        String nomJava = cheminFichier.split(sep)[cheminFichier.split(sep).length - 1];
        String nom = nomJava.split("\\.")[0];
        // Exemple simplifié : Ajouter une classe fictive
        Classe nouvelleClasse = new Classe(nom);
        addClass(nouvelleClasse);

        System.out.println("Fichier importé avec succès : " + cheminFichier);
    }

    /**
     * Exporte le diagramme dans un format spécifique.
     *
     * @param format Le format d'exportation (ex : PNG, PlantUML).
     * @return true si l'exportation a réussi, false sinon.
     */
    public boolean exporter(String format) {
        System.out.println("Exportation en cours au format : " + format);

        // Exemple simplifié : Simuler une exportation réussie
        if ("PNG".equalsIgnoreCase(format) || "PlantUML".equalsIgnoreCase(format)) {
            System.out.println("Diagramme exporté avec succès !");
            return true;
        }

        System.out.println("Format d'exportation non pris en charge : " + format);
        return false;
    }

    // Méthodes du patron Observateur

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
