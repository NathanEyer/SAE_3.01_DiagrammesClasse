package diagrammes.classe;

import java.util.List;

/**
 * Modélise la méthode d'une classe
 */
public class  Methode {
    /**
     * Attributs d'une méthode
     */
    private final String nomMethode;
    private final String typeRetour;
    List<String> parametres;
    private final String modificateur;

    /**
     * Construit une méthode à partir de son nom, du type de retour et des parametres
     * @param nomMethode nom
     * @param typeRetour type
     * @param parametres paramètres
     */
    public Methode(String nomMethode, String typeRetour, List<String> parametres, String modificateur) {
        this.nomMethode = nomMethode;
        this.typeRetour = typeRetour;
        this.parametres = parametres;
        this.modificateur = modificateur;
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
                ", parametres=" + parametres + '}';
    }

    /**
     * Retourne le nom de la méthode
     * @return nom
     */
    public String getNomMethode() {
        return nomMethode;
    }

    /**
     * Retourne le type de retour
     * @return typeRetour
     */
    public String getTypeRetour() { return typeRetour; }

    /**
     * Retourne les paramètres
     * @return parametres
     */
    public List<String> getParametres() { return parametres; }

    /**
     * Retourne la visibilité
     * @return modificateur
     */
    public String getModificateur() {
        return modificateur;
    }

}
