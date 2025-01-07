package diagrammes.fichier;

import diagrammes.modele.Diagramme;
import diagrammes.classe.Classe;
import diagrammes.classe.Attribut;
import diagrammes.classe.Methode;
import diagrammes.relations.Relation;
import diagrammes.relations.RelationStrategy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ExporterUml implements Exporter {

    @Override
    public void exporter(String path, Object diagramme) throws IOException {
        if (!(diagramme instanceof Diagramme)) {
            throw new IllegalArgumentException("L'objet fourni n'est pas un diagramme valide.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("@startuml\n");

            // Export des classes
            for (Classe classe : ((Diagramme) diagramme).getClasses()) {
                writer.write("class " + classe.getNom() + " {\n");

                // Export des attributs avec niveaux de visibilité
                for (Attribut attribut : classe.getAttributs()) {
                    String visibility = getVisibility(attribut.getModificateur());
                    writer.write("  " + visibility + attribut.getTypeAttribut() + " " + attribut.getNomAttribut() + "\n");
                }

                // Export des méthodes avec niveaux de visibilité
                for (Methode methode : classe.getMethodes()) {
                    String visibility = getVisibility(methode.getModificateur());
                    writer.write("  " + visibility + methode.getTypeRetour() + " " + methode.getNomMethode() + "(");
                    writer.write(String.join(", ", methode.getParametres()));
                    writer.write(")\n");
                }

                writer.write("}\n");
            }

            // Export des relations
            for (Relation relation : ((Diagramme) diagramme).getRelations()) {
                String source = relation.getDepart().getNom();
                String target = relation.getDestination().getNom();
                String type = getRelationArrow(relation.getType());
                writer.write(source + " " + type + " " + target + "\n");
            }

            writer.write("@enduml\n");
        }
    }


    private String getRelationArrow(RelationStrategy type) {
        // Ajoutez ici la logique pour mapper les types de relation à leurs flèches UML
        if (type.equals("heritage")) {
            return "<|--";
        } else if (type.equals("association")) {
            return "-->";
        }
        return "--";
    }

    private String getVisibility(String modificateur) {
        return switch (modificateur.toLowerCase()) {
            case "public" -> "+ ";
            case "private" -> "- ";
            case "protected" -> "# ";
            default -> "~ "; // package-private ou autre
        };
    }
}
