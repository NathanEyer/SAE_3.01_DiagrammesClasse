package diagrammes.classe;

public class Attribut {
    String nomAttribut;
    String typeAttribut;

    public Attribut(String nomAttribut, String typeAttribut) {
        this.nomAttribut = nomAttribut;
        this.typeAttribut = typeAttribut;
    }

    /**
     * Retourne le nom de l'attribut
     * @return
     */
    public String getNomAttribut() {
        return nomAttribut;
    }

    @Override
    public String toString() {
        return "Attribut{" +
                "nomAttribut='" + nomAttribut + '\'' +
                ", typeAttribut='" + typeAttribut + '\'' +
                '}';
    }

    public String getTypeAttribut() {
        return typeAttribut;
    }
}
