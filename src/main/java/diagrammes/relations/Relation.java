package diagrammes.relations;

public class Relation {
    private Class depart;
    private Class destination;
    private RelationStrategy type;

    public Relation(Class depart, Class destination, RelationStrategy type) {
        this.depart = depart;
        this.destination = destination;
        this.type = type;
    }


}