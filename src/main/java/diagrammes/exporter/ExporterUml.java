package diagrammes.exporter;

import java.io.File;
import java.io.FileWriter;

public class ExporterUml implements Exporter{


    @Override
    public void exporter(String chemin, Object contenu) throws Exception {
        if (!(contenu instanceof String)) {
            throw new IllegalArgumentException("Le contenu doit être une chaîne représentant le diagramme UML.");
        }
        String uml = (String) contenu;
        File fichier = new File(chemin);
        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write(uml);
        }
        System.out.println("Exportation réussie en UML vers : " + chemin);
    }
}
