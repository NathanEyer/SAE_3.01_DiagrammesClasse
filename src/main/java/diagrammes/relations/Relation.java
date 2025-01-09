package diagrammes.relations;

import diagrammes.classe.Classe;

public class Relation {
    /**
     * Attributs
     */
    private final Classe depart;
    private final Classe destination;
    private final RelationStrategy type;
    private String attribut;

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
        this.attribut = null;
    }

    /**
     * Retourne l'attribut enregistré pour la relation
     * @return attribut
     */
    public String getAttribut() {
        return attribut;
    }

    /**
     * Permet de modifier l'attribut
     * @param attribut nouvel attribut
     */
    public void setAttribut(String attribut) {
        this.attribut = attribut;
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
     * @return RelationStrategy
     */
    public RelationStrategy getType() {
        return type;
    }

    /**
     * Méthode toString
     * @return String formatté
     */
    @Override
    public String toString() {
        return "Relation: " + type.getClass().getSimpleName() + " entre " + depart.getNom() + " et " + destination.getNom();
    }

}