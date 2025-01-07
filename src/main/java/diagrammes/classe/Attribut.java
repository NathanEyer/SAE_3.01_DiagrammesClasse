package diagrammes.classe;

/**
 * Classe modélisant l'attribut d'une classe
 */
public class Attribut {
    /**
     * Attributs d'un attribut
     */
    String nomAttribut;
    String typeAttribut;

    /**
     * Construit un attribut à partir de son nom et son type
     * @param nomAttribut nom
     * @param typeAttribut type
     */
    public Attribut(String nomAttribut, String typeAttribut) {
        this.nomAttribut = nomAttribut;
        this.typeAttribut = typeAttribut;
    }

    /**
     * Affiche correctement les données
     * @return String
     */
    @Override
    public String toString() {
        return "Attribut{" + "nomAttribut='" + nomAttribut + '\'' + ", typeAttribut='" + typeAttribut + '\'' + '}';
    }

    /**
     * Retourne le nom de l'attribut
     * @return nomAttribut
     */
    public String getNomAttribut() {
        return nomAttribut;
    }

    /**
     * Retourne le type de l'attribut
     * @return typeAttribut
     */
    public String getTypeAttribut() {
        return typeAttribut;
    }
}
