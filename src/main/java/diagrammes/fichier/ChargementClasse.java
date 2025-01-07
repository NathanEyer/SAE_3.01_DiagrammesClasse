package diagrammes.fichier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Chargement du bon nom de classe
 */
public class ChargementClasse extends ClassLoader {
    /**
     * Chemin absolu
     */
    private final Path path;

    /**
     * @param path chemin absolu
     */
    public ChargementClasse(Path path) {
        this.path = path;
    }

    /**
     * Cherche la classe
     * @param name nom de la classe
     * @return Class
     */
    @Override
    protected Class<?> findClass(String name){
        try {
            String pathStr = name.replace(".", File.separator) + ".class";
            Path classPath = path.resolve(pathStr);
            byte[] classe = Files.readAllBytes(classPath);
            return defineClass(name, classe, 0, classe.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renvoie le bon nom de classe à utiliser
     * @param path nom absolu
     * @return nom voulu
     */
    public static String getGoodName(Path path) {
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Le fichier n'existe pas : " + path.toAbsolutePath());
        }

        Path racine = path.getParent();

        Path cheminRelatif = racine.relativize(path);

        return cheminRelatif.toString()
                .replace(File.separator, ".") // Convertir les séparateurs de chemin en '.'
                .replace(".class", "");      // Retirer l'extension .class
    }
}
