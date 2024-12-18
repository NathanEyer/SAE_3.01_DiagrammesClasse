package diagrammes.modele;

import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import diagrammes.classe.Attribut;
import diagrammes.relations.Relation;
import diagrammes.vue.Observateur;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        if (!fichier.getName().endsWith(".java")) {
            System.out.println("Le fichier sélectionné n'est pas un fichier Java : " + cheminFichier);
            return;
        }

        System.out.println("Importation du fichier : " + fichier.getName());
        analyserFichier(fichier);
    }

    private void analyserFichier(File fichier) {
        try {
            // Lire le contenu du fichier
            List<String> lignes = Files.readAllLines(fichier.toPath(), StandardCharsets.UTF_8);
            String contenu = String.join("\n", lignes);

            // Extraire le nom de la classe
            Pattern patternClasse = Pattern.compile("class\\s+(\\w+)");
            Matcher matcherClasse = patternClasse.matcher(contenu);
            String nomClasse = matcherClasse.find() ? matcherClasse.group(1) : "ClasseAnonyme";

            // Extraire les attributs
            Pattern patternAttributs = Pattern.compile("(private|protected|public)\\s+(\\w+)\\s+(\\w+);");
            Matcher matcherAttributs = patternAttributs.matcher(contenu);
            List<Attribut> attributs = new ArrayList<>();
            while (matcherAttributs.find()) {
                String typeAttribut = matcherAttributs.group(2); // Type de l'attribut
                String nomAttribut = matcherAttributs.group(3);  // Nom de l'attribut
                attributs.add(new Attribut(nomAttribut, typeAttribut));
            }

            // Extraire les méthodes
            Pattern patternMethodes = Pattern.compile(
                    "(private|protected|public)\\s+(\\w+)\\s+(\\w+)\\(([^)]*)\\)\\s*\\{?");
            Matcher matcherMethodes = patternMethodes.matcher(contenu);
            List<Methode> methodes = new ArrayList<>();
            while (matcherMethodes.find()) {
                String typeRetour = matcherMethodes.group(2); // Type de retour
                String nomMethode = matcherMethodes.group(3); // Nom de la méthode
                String params = matcherMethodes.group(4);     // Paramètres sous forme de chaîne

                // Traiter les paramètres
                List<String> listeParametres = new ArrayList<>();
                if (!params.trim().isEmpty()) {
                    for (String param : params.split(",")) {
                        listeParametres.add(param.trim());
                    }
                }

                methodes.add(new Methode(nomMethode, typeRetour, listeParametres));
            }

            // Créer une nouvelle classe avec les attributs et méthodes extraits
            Classe nouvelleClasse = new Classe(nomClasse);
            nouvelleClasse.setAttributs(attributs);
            nouvelleClasse.setMethodes(methodes);

            // Ajouter la classe au modèle
            addClass(nouvelleClasse);

            System.out.println("Classe analysée : " + nomClasse);
            System.out.println("Attributs : " + attributs);
            System.out.println("Méthodes : " + methodes);

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + fichier.getName());
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
