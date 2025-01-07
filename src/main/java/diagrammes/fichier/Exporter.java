package diagrammes.fichier;

/**
 * Interface d'exportation
 */
public interface Exporter {
     void exporter(String path, Object diagramme) throws Exception;
}

