package diagrammes.relations;

public class RelationStrategyFactory {
    /**
     * Méthode de création du type Relation
     * @param type type de la relation
     * @return RelationStrategy
     */
    public static RelationStrategy create(String type) {
        String normalizedType = type.toLowerCase().replace("é", "e");
        return switch (normalizedType) {
            case "heritage" -> new Heritage();
            case "implementation" -> new Implementation();
            case "association" -> new Association();
            default -> throw new IllegalArgumentException("Type de relation non reconnu : " + type);
        };
    }

}

