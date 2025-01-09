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
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Diagramme concret
 */
public class ModeleDiagramme implements Diagramme {
    /**
     * Attributs du diagramme
     */
    private List<Classe> classes;
    private List<Relation> relations;
    private List<Observateur> observateurs;

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
     *
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
     *
     * @param relation à ajouter
     */
    public void addRelation(Relation relation){
        relations.add(relation);
        notifierObservateur();
    }

    /**
     * Ouvre un explorateur pour sélectionner un fichier class
     * et lance l'analyse introspective du fichier
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
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) genericType;
                        Type[] typeArguments = parameterizedType.getActualTypeArguments();
                        if (typeArguments.length > 0 && typeArguments[0] instanceof Class<?>) {
                            Class<?> genericClass = (Class<?>) typeArguments[0];
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
            e.printStackTrace();
        }
    }

    private void reevaluerAssociations() throws ClassNotFoundException {
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


    private Classe chargerOuCreerClasse(String nomClasse) throws ClassNotFoundException {
        // Vérifie si la classe existe déjà dans le modèle
        Classe classe = getClasseParNom(nomClasse);
        if (classe == null) {
            try {
                // Tente de charger la classe dynamiquement
                Class<?> classeDynamique = Class.forName(nomClasse);
                classe = new Classe(classeDynamique.getSimpleName());
                addClass(classe);
            } catch (ClassNotFoundException e) {
                // Si elle n'existe pas, la crée comme une nouvelle classe
                classe = new Classe(nomClasse);
                addClass(classe);
            }
        }
        return classe;
    }


    public Classe getClasseParNom(String nomClasse) {
        for (Classe classe : classes) {
            if (classe.getNom().equals(nomClasse)) {
                return classe;
            }
        }
        return null;
    }

    private final List<AssociationIncomplete> associationsIncompletes = new ArrayList<>();

    private static class AssociationIncomplete {
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
     *
     * @param stage  La fenêtre de l'application pour afficher le FileChooser.
     * @param vue    La vue du diagramme (utile pour l'export PNG).
     * @param format Le format d'exportation ("PNG" ou "UML").
     * @return true si l'exportation a réussi, false sinon.
     */
    public boolean exporter(Stage stage, VueDiagramme vue, String format) {
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
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * Permet d'enregistrer un observateur
     *
     * @param o observateur concerné
     */
    @Override
    public void enregistrerObservateur(Observateur o) {
        observateurs.add(o);
    }

    /**
     * Permet de supprimer un observateur
     *
     * @param o à supprimer
     */
    @Override
    public void supprimerObservateur(Observateur o) {
        observateurs.remove(o);
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
     *
     * @return liste
     */
    public List<Classe> getClasses() {
        return classes;
    }

    /**
     * Renvoie la liste des relations
     *
     * @return liste
     */
    public List<Relation> getRelations() {
        return relations;
    }


    /**
     * methode creerClasse
     */
    public void creerClasse() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Créer une nouvelle classe");
        dialog.setHeaderText("Entrez le nom de la nouvelle classe");
        dialog.setContentText("Nom de la classe:");

        dialog.showAndWait().ifPresent(nomClasse -> {
            if (nomClasse != null && !nomClasse.trim().isEmpty()) {
                Classe nouvelleClasse = new Classe(nomClasse);
                try {
                    addClass(nouvelleClasse);
                    VueDiagramme.setMessage(nomClasse + " créé");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                afficherAlert("Erreur", "Le nom de la classe ne peut pas être vide.");
            }
        });
    }


    /**
     * @param title
     * @param message
     */

    private void afficherAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
