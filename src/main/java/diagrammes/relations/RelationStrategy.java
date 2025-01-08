package diagrammes.relations;

public interface RelationStrategy {
    void creerLien(String nomClasse) throws ClassNotFoundException;
}
