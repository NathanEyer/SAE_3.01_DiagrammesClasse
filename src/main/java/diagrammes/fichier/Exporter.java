package diagrammes.fichier;

/**
 * Interface d'exportation
 */
public interface Exporter {
     void exporter(String chemin,Object contenu)throws Exception;
}
