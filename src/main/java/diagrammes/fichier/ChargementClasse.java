package diagrammes.fichier;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

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
    public static String getGoodName(Path path, URLClassLoader urlClassLoader) {
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Le fichier n'existe pas : " + path.toAbsolutePath());
        }

        String className = path.toString();
        className = className.replace(".class", "");
        int nbSeparator = className.split(Pattern.quote(File.separator)).length - 1;

        while (nbSeparator > 0) {
            try {
                String modifiedClassName = className.split(Pattern.quote(File.separator))[className.split(Pattern.quote(File.separator)).length - 1];
                System.out.println(modifiedClassName);
                urlClassLoader.loadClass(modifiedClassName);
                return modifiedClassName;
            } catch (Throwable e) {
                int lastSeparatorIndex = className.lastIndexOf(File.separator);
                if (lastSeparatorIndex != -1) {
                    className = className.substring(0, lastSeparatorIndex) + "." + className.substring(lastSeparatorIndex + 1);
                }
            }
            nbSeparator--;
        }
        return null;
    }
}
