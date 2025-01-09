package diagrammes.relations;

public class RelationStrategyFactory {

    public static RelationStrategy create(String type) {
        String normalizedType = type.toLowerCase().replace("é", "e");
        switch (normalizedType) {
            case "heritage":
                return new Heritage();
            case "implementation":
                return new Implementation();
            case "association":
                return new Association();
            default:
                throw new IllegalArgumentException("Type de relation non reconnu : " + type);
        }
    }

}

