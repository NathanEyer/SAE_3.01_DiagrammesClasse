package diagrammes.relations;

import diagrammes.classe.Classe;

public class Relation {
    private Classe depart;
    private Classe destination;
    private RelationStrategy type;

    public Relation(Classe depart, Classe destination, RelationStrategy type) {
        this.depart = depart;
        this.destination = destination;
        this.type = type;
    }


}