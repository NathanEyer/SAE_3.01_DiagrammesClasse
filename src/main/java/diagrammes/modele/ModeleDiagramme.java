package diagrammes.modele;

import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import diagrammes.classe.Attribut;
import diagrammes.fichier.*;
import diagrammes.relations.Association;
import diagrammes.relations.Heritage;
import diagrammes.relations.Implementation;
import diagrammes.relations.Relation;
import diagrammes.vue.Observateur;
import diagrammes.vue.VueDiagramme;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Diagramme concret
 */
public class ModeleDiagramme implements Diagramme {
    /**
     * Attributs du diagramme
     */
    private final List<Classe> classes;
    private final List<Relation> relations;
    private final List<Observateur> observateurs;
    private final List<AssociationIncomplete> associationsIncompletes = new ArrayList<>();


    /**
     * Construit un ModeleDiagramme par défaut
     */
    public ModeleDiagramme() {
        this.classes = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.observateurs = new ArrayList<>();
    }

    /**
     * Ajoute une classe au diagramme
     * @param classe à ajouter
     */
    public void addClass(Classe classe) throws ClassNotFoundException {
        if(!classes.contains(classe)) {
            classes.add(classe);
            reevaluerAssociations();
            notifierObservateur();
        }
    }

    /**
     * Ajoute une relation entre les classes
     * @param relation à ajouter
     */
    public void addRelation(Relation relation){
        relations.add(relation);
        notifierObservateur();
    }

    /**
     * Analyse un fichier class grâce à l'introspection pour extraire
     * les attributs et les méthodes.
     *
     * @param cheminFichierClass Le nom complet de la classe (package inclus).
     */
    public void analyserFichierClass(String cheminFichierClass) {
        try {
            File fichierClass = new File(cheminFichierClass);

            // Détecter le dossier parent comme racine
            File dossierParent = fichierClass.getParentFile().getParentFile();
            URL dossierParentURL = dossierParent.toURI().toURL();

            // Créer un ClassLoader pour ce dossier
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{dossierParentURL});

            // Obtenir le nom complet de la classe
            String nomClasse = ChargementClasse.getGoodName(fichierClass.toPath(), urlClassLoader);
            while (nomClasse == null) {
                dossierParent = fichierClass.getParentFile();
                dossierParentURL = dossierParent.toURI().toURL();
                urlClassLoader = URLClassLoader.newInstance(new URL[]{dossierParentURL});
                nomClasse = ChargementClasse.getGoodName(fichierClass.toPath(), urlClassLoader);
            }

            // Charger la classe
            Class<?> classe = urlClassLoader.loadClass(nomClasse);

            // Analyse introspective
            Classe nouvelleClasse = new Classe(classe.getSimpleName());

            for (Field field : classe.getDeclaredFields()) {
                String modificateur = Modifier.toString(field.getModifiers()); // Récupération du modificateur
                nouvelleClasse.ajouterAttribut(new Attribut(
                        field.getName(),
                        field.getType().getSimpleName(),
                        modificateur // Ajout du modificateur
                ));

                // Gérer les associations pour les collections
                if (Collection.class.isAssignableFrom(field.getType())) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType parameterizedType) {
                        Type[] typeArguments = parameterizedType.getActualTypeArguments();
                        if (typeArguments.length > 0 && typeArguments[0] instanceof Class<?> genericClass) {
                            String nomSimpl = genericClass.getSimpleName();
                            Classe classeDesty = getClasseParNom(nomSimpl);
                            if (classeDesty != null) {
                                Relation collectionRelation = new Relation(nouvelleClasse, classeDesty, new Association());
                                collectionRelation.setAttribut(field.getName());
                                addRelation(collectionRelation);
                            } else {
                                associationsIncompletes.add(new AssociationIncomplete(nouvelleClasse, nomSimpl, field.getName(),true));
                            }
                        }
                    }
                }

                // Gérer les associations pour les types non primitifs
                if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.")) {
                    Classe classeDestination = getClasseParNom(field.getType().getSimpleName());
                    if (classeDestination != null) {
                        Relation association = new Relation(nouvelleClasse, classeDestination, new Association());
                        association.setAttribut(field.getName());
                        addRelation(association);
                    } else {
                        associationsIncompletes.add(new AssociationIncomplete(nouvelleClasse, field.getType().getSimpleName(), field.getName(), false));
                    }

                }
            }

            for (Method method : classe.getDeclaredMethods()) {
                List<String> parametres = new ArrayList<>();
                for (Class<?> paramType : method.getParameterTypes()) {
                    parametres.add(paramType.getSimpleName());
                }

                int typeMethode;
                if (Modifier.isAbstract(method.getModifiers())) {
                    typeMethode = 1; // Méthode abstraite
                } else if (Modifier.isStatic(method.getModifiers())) {
                    typeMethode = 2; // Méthode statique
                } else {
                    typeMethode = 0; // Méthode normale
                }

                String modificateur = Modifier.toString(method.getModifiers()).split(" ")[0]; // Récupération du modificateur
                nouvelleClasse.ajouterMethode(new Methode(
                        method.getName(),
                        method.getReturnType().getSimpleName(),
                        parametres,
                        modificateur, typeMethode
                ));
            }

            addClass(nouvelleClasse);
            Class<?> classeParente = classe.getSuperclass();
            if (classeParente != null && !classeParente.getSimpleName().equals("Object")) {
                Relation relation = new Relation(nouvelleClasse, new Classe(classeParente.getSimpleName()), new Heritage());
                addRelation(relation);
            }
            Class<?>[] interfaces = classe.getInterfaces();
            for (Class<?> inter : interfaces) {
                Relation implementation = new Relation(nouvelleClasse, new Classe(inter.getSimpleName()), new Implementation());
                addRelation(implementation);
            }
            urlClassLoader.close();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * Évalue à nouveau les associations
     */
    private void reevaluerAssociations() {
        List<AssociationIncomplete> resolues = new ArrayList<>();

        for (AssociationIncomplete incomplete : associationsIncompletes) {
            Classe classeDestination = getClasseParNom(incomplete.getNomClasseAssociee());
            if (classeDestination != null) {
                Relation association;
                if (incomplete.isCollection()) {
                    // Si c'est une collection
                    association = new Relation(incomplete.getSource(), classeDestination, new Association());
                    association.setAttribut(incomplete.getAttribut() + " (Collection)");
                } else {
                    // Si ce n'est pas une collection
                    association = new Relation(incomplete.getSource(), classeDestination, new Association());
                    association.setAttribut(incomplete.getAttribut());
                }

                addRelation(association);
                resolues.add(incomplete);
            }
        }

        // Supprimer les associations résolues de la liste des incomplètes
        associationsIncompletes.removeAll(resolues);
    }

    /**
     * Recherche une classe en fonction de son nom
     * @param nomClasse nom de la classe
     * @return Classe trouvée
     */
    public Classe getClasseParNom(String nomClasse) {
        for (Classe classe : classes) {
            if (classe.getNom().equals(nomClasse)) {
                return classe;
            }
        }
        return null;
    }

    /**
     * Sous-classe
     */
    private static class AssociationIncomplete {
        /**
         * Attributs
         */
        private final Classe source;
        private final String nomClasseAssociee;
        private final String attribut;
        private final boolean isCollection;

        public AssociationIncomplete(Classe source, String nomClasseAssociee, String attribut, boolean isCollection) {
            this.source = source;
            this.nomClasseAssociee = nomClasseAssociee;
            this.attribut = attribut;
            this.isCollection = isCollection;
        }

        public Classe getSource() {
            return source;
        }

        public String getNomClasseAssociee() {
            return nomClasseAssociee;
        }

        public String getAttribut() {
            return attribut;
        }

        public boolean isCollection() {
            return isCollection;
        }
    }


    /**
     * Réinitialise tout le diagramme
     */
    public void reinitialiser() {
        classes.clear();
        relations.clear();
        VueDiagramme.reinitialiser();
        this.notifierObservateur();
    }

    /**
     * Exporte le diagramme dans un fichier au format spécifié (PNG ou UML).
     * @param stage  La fenêtre de l'application pour afficher le FileChooser.
     * @param vue    La vue du diagramme (utile pour l'export PNG).
     * @param format Le format d'exportation ("PNG" ou "UML").
     */
    public void exporter(Stage stage, VueDiagramme vue, String format) {
        Exporter export = null;
        FileChooser fileChooser = new FileChooser();

        // Configure le FileChooser en fonction du format choisi
        if (format.equalsIgnoreCase("PNG")) {
            export = new ExporterImage();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        } else if (format.equalsIgnoreCase("UML")) {
            export = new ExporterUml();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PlantUML files (*.puml)", "*.puml"));

        } else if (format.equalsIgnoreCase("JAVA")) {
        export = new ExporterJava();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java files (*.java)", "*.java"));
        }
        // Affiche le FileChooser pour choisir l'emplacement du fichier
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                // Passe les bons objets pour l'exportation en fonction du format
                if (format.equalsIgnoreCase("PNG")) {
                    export.exporter(file.getAbsolutePath(), vue); // La vue est nécessaire pour l'export PNG
                } else if (format.equalsIgnoreCase("UML")) {
                    export.exporter(file.getAbsolutePath(), this); // Le modèle est nécessaire pour l'export UML
                } else if (format.equalsIgnoreCase("JAVA")) {
                    export.exporter(file.getAbsolutePath(), this); // Le modèle est nécessaire pour l'export JAVA
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Permet d'enregistrer un observateur
     * @param o observateur concerné
     */
    @Override
    public void enregistrerObservateur(Observateur o) {
        observateurs.add(o);
    }

    /**
     * Notifie les observateurs
     */
    @Override
    public void notifierObservateur() {
        for (Observateur observateur : observateurs) {
            observateur.actualiser(this);
        }
    }

    /**
     * Renvoie la liste des classes
     * @return liste
     */
    public List<Classe> getClasses() {
        return classes;
    }

    /**
     * Renvoie la liste des relations
     * @return liste
     */
    public List<Relation> getRelations() {
        return relations;
    }
}