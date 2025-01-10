package diagrammes.vue;

import diagrammes.classe.Attribut;
import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import diagrammes.relations.Relation;
import diagrammes.relations.RelationStrategy;
import diagrammes.relations.RelationStrategyFactory;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Vue pour modifier une classe
 */
public class VueModifier {
    /**
     * Attribut
     */
    private final Classe classe;

    /**
     * Construit une vue VueModifier
     * @param classe classe
     */
    public VueModifier(Classe classe) {
        this.classe = classe;
    }

    /**
     * Affichage de l'interface de modification
     * @param relations liste des relations
     * @param autresClasses liste des classes
     * @return classe
     */
    public Classe afficher(List<Relation> relations, List<Classe> autresClasses) {
        //Initialisations
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        //SECTIONS
        //Section pour modifier le nom de la classe
        Label lblNomClasse = new Label("Nom de la classe :");
        TextField txtNomClasse = new TextField(classe.getNom());

        //Section pour les méthodes
        Label lblMethodes = new Label("Méthodes :");
        ListView<String> listMethodes = new ListView<>();
        for (Methode methode : classe.getMethodes()) {
            listMethodes.getItems().add(formatMethode(methode));
        }
        listMethodes.setPrefHeight(100);

        // Section pour les relations
        Label lblRelations = new Label("Relations :");
        ListView<Relation> listRelations = new ListView<>();
        listRelations.getItems().addAll(
                relations.stream()
                        .filter(rel -> rel.getDepart().equals(classe))
                        .toList()
        );
        listRelations.setPrefHeight(100);

        //Section pour les attributs
        Label lblAttributs = new Label("Attributs :");
        ListView<String> listAttributs = new ListView<>();
        for (Attribut attribut : classe.getAttributs()) {
            listAttributs.getItems().add(formatAttribut(attribut));
        }
        listAttributs.setPrefHeight(100);


        //ATTRIBUT
        //Modifier Attribut
        Button btnModifierAttribut = new Button("Modifier Attribut");
        btnModifierAttribut.setOnAction(e -> modifierAttribut(listAttributs));

        //Supprimer Attribut
        Button btnSupprimerAttribut = new Button("Supprimer Attribut");
        btnSupprimerAttribut.setOnAction(e -> {
            String selectedAttribut = listAttributs.getSelectionModel().getSelectedItem();
            if (selectedAttribut != null) {
                listAttributs.getItems().remove(selectedAttribut);
            }
        });

        //Ajouter Attribut
        Button btnAjouterAttribut = new Button("Ajouter Attribut");
        btnAjouterAttribut.setOnAction(e -> ajouterAttribut(listAttributs));


        //METHODE
        //Modifier Methode
        Button btnModifierMethode = new Button("Modifier Méthode");
        btnModifierMethode.setOnAction(e -> modifierMethode(listMethodes));

        //Supprimer Methode
        Button btnSupprimerMethode = new Button("Supprimer Méthode");
        btnSupprimerMethode.setOnAction(e -> {
            String selectedMethode = listMethodes.getSelectionModel().getSelectedItem();
            if (selectedMethode != null) {
                listMethodes.getItems().remove(selectedMethode);
            }
        });

        //Ajouter Methode
        Button btnAjouterMethode = new Button("Ajouter Méthode");
        btnAjouterMethode.setOnAction(e -> ajouterMethode(listMethodes));


        //RELATION
        //Modifier Relation
        Button btnModifierRelation = new Button("Modifier Relation");
        btnModifierRelation.setOnAction(e -> modifierRelation(listRelations, autresClasses));

        //Supprimer Relation
        Button btnSupprimerRelation = new Button("Supprimer Relation");
        btnSupprimerRelation.setOnAction(e -> {
            Relation selectedRelation = listRelations.getSelectionModel().getSelectedItem();
            if (selectedRelation != null) {
                listRelations.getItems().remove(selectedRelation);
            }
        });

        //Ajouter Relation
        Button btnAjouterRelation = new Button("Ajouter Relation");
        btnAjouterRelation.setOnAction(e -> ajouterRelation(listRelations, autresClasses));


        //VALIDER
        Button btnValider = new Button("Valider");
        btnValider.setOnAction(e -> {
            classe.setNom(txtNomClasse.getText());

            // Mettre à jour les attributs
            classe.getAttributs().clear();
            for (String attributString : listAttributs.getItems()) {
                String[] parts = attributString.split(" ");
                String modificateur = parts[0];
                String type = parts[1];
                String nom = parts[2];
                classe.ajouterAttribut(new Attribut(nom, type, modificateur));
            }

            // Mettre à jour les méthodes
            classe.getMethodes().clear();
            for (String methodeString : listMethodes.getItems()) {
                String[] parts = methodeString.split(" ");
                String modificateur = parts[0];
                String retour = parts[1];
                String nom = parts[2].replace("(","");
                int type = 0;
                if(retour.equalsIgnoreCase("abstract")){
                    type = 1;
                    retour = parts[2];
                    nom = parts[3].substring(0, parts[3].length() - 1);
                }else if(retour.equalsIgnoreCase("static")){
                    type = 2;
                    retour = parts[2];
                    nom = parts[3].substring(0, parts[3].length() - 1);
                }

                String parametres = methodeString.substring(methodeString.indexOf("(") + 1, methodeString.indexOf(")"));
                List<String> parametresListe = parametres.isEmpty() ? new ArrayList<>() : List.of(parametres.replaceAll("\\s+", "").split(","));
                classe.ajouterMethode(new Methode(nom, retour, parametresListe, modificateur, type));
            }

            // Mettre à jour les relations
            relations.removeIf(rel -> rel.getDepart().equals(classe));
            relations.addAll(listRelations.getItems());

            stage.close();
        });

        // Organisation des éléments dans le layout
        root.getChildren().addAll(
                lblNomClasse, txtNomClasse,
                lblAttributs, new HBox(10, listAttributs, btnModifierAttribut, btnSupprimerAttribut, btnAjouterAttribut),
                lblMethodes, new HBox(10, listMethodes, btnModifierMethode, btnSupprimerMethode, btnAjouterMethode),
                lblRelations, new HBox(10, listRelations, btnModifierRelation, btnSupprimerRelation, btnAjouterRelation),
                btnValider
        );

        //Affichage
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Modifier une classe");
        stage.showAndWait();

        return classe;
    }

    /**
     * Modification d'une méthode
     * @param listMethodes liste des méthodes concernées
     */
    private void modifierMethode(ListView<String> listMethodes) {
        String selectedMethode = listMethodes.getSelectionModel().getSelectedItem();
        if (selectedMethode != null) {
            String[] parts = selectedMethode.split(" ");
            String modificateur = parts[0];
            String retour = parts[1];
            String nom = parts[2].replace("(","");
            if(retour.equalsIgnoreCase("abstract")){
                retour = parts[2];
                nom = parts[3].substring(0, parts[3].length() - 1);
            }else if(retour.equalsIgnoreCase("static")){
                retour = parts[2];
                nom = parts[3].substring(0, parts[3].length() - 1);
            }
            String parametres = selectedMethode.substring(selectedMethode.indexOf("(") + 1, selectedMethode.indexOf(")"));

            // Fenêtre pour modifier la méthode
            Stage methodeStage = new Stage();
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            if (nom.endsWith("(")) {
                nom = nom.substring(0, nom.length() - 1);
            }
            TextField txtNom = new TextField(nom);

            ComboBox<String> comboRetour = new ComboBox<>();
            comboRetour.getItems().addAll("void", "String", "int", "double", "boolean", "float");
            comboRetour.setValue(retour);

            ComboBox<String> comboModificateur = new ComboBox<>();
            comboModificateur.getItems().addAll("private", "public", "protected");
            comboModificateur.setValue(modificateur);

            TextField txtParametres = new TextField(parametres);

            Button btnValider = new Button("Valider");
            btnValider.setOnAction(e -> {
                listMethodes.getItems().set(
                        listMethodes.getSelectionModel().getSelectedIndex(),
                        comboModificateur.getValue() + " " + comboRetour.getValue() + " " + txtNom.getText() +
                                "(" + txtParametres.getText() + ")"
                );
                methodeStage.close();
            });

            root.getChildren().addAll(new Label("Modifier la méthode"), txtNom, comboRetour, comboModificateur, txtParametres, btnValider);
            Scene scene = new Scene(root, 400, 250);
            methodeStage.setScene(scene);
            methodeStage.showAndWait();
        }
    }

    /**
     * Modification d'un attribut
     * @param listAttributs liste des attributs concernés
     */
    private void modifierAttribut(ListView<String> listAttributs) {
        String selectedAttribut = listAttributs.getSelectionModel().getSelectedItem();
        if (selectedAttribut != null) {
            String[] parts = selectedAttribut.split(" ");
            String modificateur = parts[0];
            String type = parts[1];
            String nom = parts[2];

            Stage attributStage = new Stage();
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            TextField txtNom = new TextField(nom);
            ComboBox<String> comboType = new ComboBox<>();
            comboType.getItems().addAll("String", "int", "double", "boolean", "float");
            comboType.setValue(type);

            ComboBox<String> comboModificateur = new ComboBox<>();
            comboModificateur.getItems().addAll("private", "public", "protected");
            comboModificateur.setValue(modificateur);

            Button btnValider = new Button("Valider");
            btnValider.setOnAction(e -> {
                listAttributs.getItems().set(
                        listAttributs.getSelectionModel().getSelectedIndex(),
                        comboModificateur.getValue() + " " + comboType.getValue() + " " + txtNom.getText()
                );
                attributStage.close();
            });

            root.getChildren().addAll(new Label("Modifier l'attribut"), txtNom, comboType, comboModificateur, btnValider);
            Scene scene = new Scene(root, 300, 200);
            attributStage.setScene(scene);
            attributStage.showAndWait();
        }
    }

    /**
     * Ajout d'un attribut
     * @param listAttributs liste des attributs concernés
     */
    private void ajouterAttribut(ListView<String> listAttributs) {
        Stage attributStage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField txtNom = new TextField();
        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("String", "int", "double", "boolean", "float");

        ComboBox<String> comboModificateur = new ComboBox<>();
        comboModificateur.getItems().addAll("private", "public", "protected");

        Button btnValider = new Button("Valider");
        btnValider.setOnAction(e -> {
            listAttributs.getItems().add(
                    comboModificateur.getValue() + " " + comboType.getValue() + " " + txtNom.getText()
            );
            attributStage.close();
        });
        root.getChildren().addAll(new Label("Ajouter un attribut"), txtNom, comboType, comboModificateur, btnValider);
        Scene scene = new Scene(root, 300, 200);
        attributStage.setScene(scene);
        attributStage.showAndWait();
    }

    /**
     * Ajout d'une méthode
     * @param listMethodes liste des méthodes concernées
     */
    private void ajouterMethode(ListView<String> listMethodes) {
        Stage methodeStage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField txtNom = new TextField();
        ComboBox<String> comboRetour = new ComboBox<>();
        comboRetour.getItems().addAll("void", "String", "int", "double", "boolean", "float");

        ComboBox<String> comboModificateur = new ComboBox<>();
        comboModificateur.getItems().addAll("private", "public", "protected");

        TextField txtParametres = new TextField();
        txtParametres.setPromptText("Paramètres (séparés par des virgules)");

        Button btnValider = new Button("Valider");
        btnValider.setOnAction(e -> {

            listMethodes.getItems().add(
                    comboModificateur.getValue() + " " + comboRetour.getValue() + " " + txtNom.getText() +
                            " (" + txtParametres.getText() + ")"
            );
            methodeStage.close();
        });
        root.getChildren().addAll(new Label("Ajouter une méthode"), txtNom, comboRetour, comboModificateur, txtParametres, btnValider);
        Scene scene = new Scene(root, 400, 250);
        methodeStage.setScene(scene);
        methodeStage.showAndWait();
    }

    /**
     * Ajout d'une relation
     * @param listRelations liste des relations concernées
     * @param autresClasses liste des classes concernées
     */
    private void ajouterRelation(ListView<Relation> listRelations, List<Classe> autresClasses) {
        Stage relationStage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("Association", "Héritage", "Implémentation");

        ComboBox<Classe> comboCible = new ComboBox<>();
        comboCible.getItems().addAll(autresClasses);

        Button btnValider = new Button("Valider");
        btnValider.setOnAction(e -> {
            String type = comboType.getValue();
            Classe classeCible = comboCible.getValue();

            if (classeCible != null) {
                RelationStrategy strategy = RelationStrategyFactory.create(type);
                Relation relation = new Relation(classe, classeCible, strategy);
                listRelations.getItems().add(relation);
                relationStage.close();
            }
        });

        root.getChildren().addAll(new Label("Type de relation"), comboType, new Label("Classe cible"), comboCible, btnValider);
        Scene scene = new Scene(root, 300, 200);
        relationStage.setScene(scene);
        relationStage.showAndWait();
    }

    /**
     * Modification d'une relation
     * @param listRelations liste des relations concernées
     * @param autresClasses liste des classes concernées
     */
    private void modifierRelation(ListView<Relation> listRelations, List<Classe> autresClasses) {
        Relation selectedRelation = listRelations.getSelectionModel().getSelectedItem();
        if (selectedRelation != null) {
            Stage relationStage = new Stage();
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            ComboBox<String> comboType = new ComboBox<>();
            comboType.getItems().addAll("Association", "Héritage", "Implémentation");
            comboType.setValue(selectedRelation.getType().getClass().getSimpleName());

            ComboBox<Classe> comboCible = new ComboBox<>();
            comboCible.getItems().addAll(autresClasses);
            comboCible.setValue(selectedRelation.getDestination());

            Button btnValider = new Button("Valider");
            btnValider.setOnAction(e -> {
                RelationStrategy strategy = RelationStrategyFactory.create(comboType.getValue());
                Relation updatedRelation = new Relation(classe, comboCible.getValue(), strategy);
                listRelations.getItems().set(listRelations.getSelectionModel().getSelectedIndex(), updatedRelation);
                relationStage.close();
            });

            root.getChildren().addAll(new Label("Modifier la relation"), comboType, comboCible, btnValider);
            Scene scene = new Scene(root, 300, 200);
            relationStage.setScene(scene);
            relationStage.showAndWait();
        }
    }

    /**
     * Formattage d'un attribut
     * @param attribut attribut concerné
     * @return le String formaté
     */
    private String formatAttribut(Attribut attribut) {
        return attribut.getModificateur() + " " + attribut.getTypeAttribut() + " " + attribut.getNomAttribut();
    }

    /**
     * Formattage d'une méthode
     * @param methode méthode concernée
     * @return String formatté
     */
    private String formatMethode(Methode methode) {
        String type = switch (methode.getAbstractStatic()) {
            case 1 -> " abstract ";
            case 2 -> " static ";
            default -> " ";
        };
        return methode.getModificateur() + type + methode.getTypeRetour() + " " + methode.getNomMethode() +
                "( " + String.join(", ", methode.getParametres()) + ")";
    }
}

