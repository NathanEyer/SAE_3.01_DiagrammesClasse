package diagrammes.relations;

public interface RelationStrategy {


    /**
     * @param nomClasse
     * @throws ClassNotFoundException
     */
    void creerLien(String nomClasse) throws ClassNotFoundException;
}
