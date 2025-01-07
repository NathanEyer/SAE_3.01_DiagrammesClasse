package diagrammes.classe;

import java.util.ArrayList;
import java.util.List;

/**
 * Modélise une classe
 */
public class Classe {
    /**
     * Attributs d'une classe
     */
    private String nom;
    private List<Attribut> attributs;
    private List<Methode> methodes;

    /**
     * Construit une classe à partir de son nom
     * @param nom nom
     */
    public Classe(String nom) {
        this.nom = nom;
        //Initialise des Listes vides
        this.attributs = new ArrayList<>();
        this.methodes = new ArrayList<>();
    }

    /**
     * Ajoute un attribut à la liste des attributs.
     * @param attribut à ajouter
     */
    public void ajouterAttribut(Attribut attribut) {
        if(attribut != null) {
            attributs.add(attribut);
        }
    }

    /**
     * Supprime un attribut de la liste des attributs
     * @param attribut à supprimer
     */
    public void supprimerAttribut(Attribut attribut) {
        attributs.remove(attribut);
    }

    /**
     * Ajoute une méthode à la liste des méthodes
     * @param methode à ajouter
     */
    public void ajouterMethode(Methode methode) {
        if(methode != null){
            methodes.add(methode);
        }
    }

    /**
     * Supprime une méthode de la liste des méthodes
     * @param methode à supprimer
     */
    public void supprimerMethode(Methode methode) {
        methodes.remove(methode);
    }

    /**
     * Retourne le nom de la classe
     * @return Nom de la classe
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la liste des attributs de la classe
     * @return List<Attribut>
     */
    public List<Attribut> getAttributs() {
        return attributs;
    }

    /**
     * Retourne la liste des méthodes de la classe
     * @return List<Methode>
     */
    public List<Methode> getMethodes() {
        return methodes;
    }

    /**
     * Définit les attributs de la classe.
     * @param attributs Liste d'attributs à définir
     */
    public void setAttributs(List<Attribut> attributs) {
        this.attributs = attributs;
    }

    /**
     * Définit les méthodes de la classe.
     * @param methodes Liste de méthodes à définir
     */
    public void setMethodes(List<Methode> methodes) {
        this.methodes = methodes;
    }
}
