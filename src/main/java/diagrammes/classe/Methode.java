package diagrammes.classe;

import java.util.List;

/**
 * Modélise la méthode d'une classe
 */
public class  Methode {
    /**
     * Attributs d'une méthode
     */
    String nomMethode;
    String typeRetour;
    List<String> parametres;

    /**
     * Construit une méthode à partir de son nom, du type de retour et des parametres
     * @param nomMethode nom
     * @param typeRetour type
     * @param parametres paramètres
     */
    public Methode(String nomMethode, String typeRetour, List<String> parametres) {
        this.nomMethode = nomMethode;
        this.typeRetour = typeRetour;
        this.parametres = parametres;
    }

    /**
     * Affiche correctement les données
     * @return String
     */
    @Override
    public String toString() {
        return "Methode{" +
                "nomMethode='" + nomMethode + '\'' +
                ", typeRetour='" + typeRetour + '\'' +
                ", parametres=" + parametres +
                '}';
    }

    /**
     * Retourne le nom de la méthode
     * @return nom
     */
    public String getNomMethode() {
        return nomMethode;
    }

    public String getTypeRetour() { return typeRetour; }

    public List<String> getParametres() { return parametres; }
}
