package diagrammes.modele;

import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import diagrammes.classe.Attribut;
import diagrammes.fichier.Exporter;
import diagrammes.fichier.ExporterImage;
import diagrammes.fichier.ChargementClasse;
import diagrammes.fichier.ExporterUml;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

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


    private boolean afficherAttributs = true;
    private boolean afficherMethodes = true;

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
        classes.add(classe);
        notifierObservateur();
    }

    /**
     * Ajoute une relation entre les classes
     * @param relation à ajouter
     */
    public void addRelation(Relation relation) throws ClassNotFoundException {
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
            while(nomClasse == null) {
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
                //gérer associations
                if(!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.")){
                    Relation association = new Relation(nouvelleClasse,new Classe(field.getType().getSimpleName()),new Association());
                    System.out.println(classe.getSimpleName() + "possède un attribut de type " + field.getType().getSimpleName());
                    addRelation(association);
                }
                // gérer les associations pour les collections
                if (estCollection(field.getType())) {
                    String typeAttribut = field.getGenericType().getTypeName();
                    if (typeAttribut.contains("<") && typeAttribut.contains(">")) {
                        // on extrait le type contenu dans la collection
                        String typeAssocie = typeAttribut.substring(typeAttribut.indexOf("<") + 1, typeAttribut.indexOf(">"));
                        System.out.println("Association détectée via une collection : " + typeAssocie);

                        // on ajoute une relation pour le type contenu dans la collection
                        Relation association = new Relation(nouvelleClasse, new Classe(typeAssocie), new Association());
                        addRelation(association);
                    }
                }

            }

            for (Method method : classe.getDeclaredMethods()) {
                List<String> parametres = new ArrayList<>();
                for (Class<?> paramType : method.getParameterTypes()) {
                    parametres.add(paramType.getSimpleName());
                }
                String modificateur = Modifier.toString(method.getModifiers()); // Récupération du modificateur
                nouvelleClasse.ajouterMethode(new Methode(
                        method.getName(),
                        method.getReturnType().getSimpleName(),
                        parametres,
                        modificateur // Ajout du modificateur
                ));
            }

            // Ajouter la classe au modèle
            addClass(nouvelleClasse);
            Class<?> classeParente = classe.getSuperclass();

            if (classeParente != null) {
                Relation relation = new Relation(nouvelleClasse, new Classe(classeParente.getSimpleName()), new Heritage());
                addRelation(relation);
            }
            Class<?>[] interfaces = classe.getInterfaces();
            for (Class<?> inter : interfaces){
                Relation implementation = new Relation(nouvelleClasse,new Classe(inter.getSimpleName()),new Implementation());
                addRelation(implementation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean estCollection(Class<?> type) {
        return type.getName().startsWith("java.util.");
    }

    /**
     * Réinitialise tout le diagramme
     */
    public void reinitialiser()   {
        classes.clear();
        relations.clear();
        VueDiagramme.reinitialiser();
        this.notifierObservateur();
    }

    /**
     * Exporte le diagramme dans un fichier au format spécifié (PNG ou UML).
     *
     * @param stage La fenêtre de l'application pour afficher le FileChooser.
     * @param vue   La vue du diagramme (utile pour l'export PNG).
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
     * @param o observateur concerné
     */
    @Override
    public void enregistrerObservateur(Observateur o) {
        observateurs.add(o);
    }

    /**
     * Permet de supprimer un observateur
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
    public void notifierObservateur()   {
        for (Observateur observateur : observateurs) {
            observateur.actualiser(this);
        }
    }

    
    
    /**
     * Renvoie la liste des classes
     * @return liste
     */
    public List<Classe> getClasses() {return classes;}

    /**
     * Renvoie la liste des relations
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
