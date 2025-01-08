package diagrammes.relations;

import diagrammes.classe.Classe;

public class Relation {
    /**
     * Attributs
     */
    private final Classe depart;
    private final Classe destination;
    private final RelationStrategy type;

    /**
     * Construit une relation
     * @param depart classe de départ
     * @param destination classe de destination
     * @param type type de relation
     */
    public Relation(Classe depart, Classe destination, RelationStrategy type) {
        this.depart = depart;
        this.destination = destination;
        this.type = type;
    }

    /**
     * Retourne la classe de départ
     * @return Classe
     */
    public Classe getDepart() {
        return depart;
    }

    /**
     * Retourne la classe de destination
     * @return Classe
     */
    public Classe getDestination() {
        return destination;
    }


    /**
     * Retourne le type de relation
     *
     * @return RelationStrategy
     */
    public RelationStrategy getType() {
        return type;
    }
}