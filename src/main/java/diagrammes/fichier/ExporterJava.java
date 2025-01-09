package diagrammes.fichier;

import diagrammes.classe.Classe;
import diagrammes.modele.Diagramme;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExporterJava implements Exporter {

    private void exporterClasse(Classe classe, File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Début de la classe
            writer.write("public class " + classe.getNom() + " {\n\n");

            // Export des attributs
            if (classe.getAttributs() != null && !classe.getAttributs().isEmpty()) {
                for (int i = 0; i < classe.getAttributs().size(); i++) {
                    String modificateur = classe.getAttributs().get(i).getModificateur();
                    String typeAttribut = classe.getAttributs().get(i).getTypeAttribut();
                    String nomAttribut = classe.getAttributs().get(i).getNomAttribut();

                    if (modificateur != null && typeAttribut != null && nomAttribut != null) {
                        switch (modificateur) {
                            case "-" -> writer.write("    private " + typeAttribut + " " + nomAttribut + ";\n");
                            case "+" -> writer.write("    public " + typeAttribut + " " + nomAttribut + ";\n");
                            case "#" -> writer.write("    protected " + typeAttribut + " " + nomAttribut + ";\n");
                            default -> writer.write("    " + typeAttribut + " " + nomAttribut + ";\n"); // Aucun modificateur
                        }
                    }
                }
            } else {
                writer.write("    // Aucun attribut\n");
            }

            writer.write("\n");

            // Export des méthodes
            if (classe.getMethodes() != null && !classe.getMethodes().isEmpty()) {
                for (int j = 0; j < classe.getMethodes().size(); j++) {
                    String modificateur = classe.getMethodes().get(j).getModificateur();
                    String typeRetour = classe.getMethodes().get(j).getTypeRetour();
                    String nomMethode = classe.getMethodes().get(j).getNomMethode();

                    if (modificateur != null && typeRetour != null && nomMethode != null) {
                        switch (modificateur) {
                            case "-" -> writer.write("    private " + typeRetour + " " + nomMethode + "() {\n");
                            case "+" -> writer.write("    public " + typeRetour + " " + nomMethode + "() {\n");
                            case "#" -> writer.write("    protected " + typeRetour + " " + nomMethode + "() {\n");
                            default -> writer.write("    " + typeRetour + " " + nomMethode + "() {\n"); // Aucun modificateur
                        }
                        writer.write("        // TODO: Implémentez cette méthode\n");
                        writer.write("    }\n\n");
                    }
                }
            } else {
                writer.write("    // Aucune méthode\n");
            }

            // Fin de la classe
            writer.write("}\n");
        }
    }

    @Override
    public void exporter(String path, Object diagramme) throws Exception {
        if (!(diagramme instanceof Diagramme)) {
            throw new IllegalArgumentException("L'objet fourni n'est pas un diagramme valide.");
        }

        // Convertir le chemin en dossier
        File outputDir = new File(path);
        if (!outputDir.exists()) {
            outputDir.mkdirs(); // Créer le dossier s'il n'existe pas
        } else if (!outputDir.isDirectory()) {
            throw new IllegalArgumentException("Le chemin fourni n'est pas un dossier valide.");
        }

        // Exporter chaque classe dans un fichier distinct
        List<Classe> classes = ((Diagramme) diagramme).getClasses();
        for (Classe c : classes) {
            File classFile = new File(outputDir, c.getNom() + ".java");
            exporterClasse(c, classFile);
        }
    }
}
