package diagrammes.classe;

import java.util.ArrayList;
import java.util.List;

public class Classe {
    // Nom de la classe
    private String nom;
    // Liste des attributs de la classe
    private List<Attribut> attributs;
    // Liste des méthodes de la classe
    private List<Methode> methodes;

    // Constructeur de la classe Classe
    public Classe(String nom) {
        this.nom = nom;
        this.attributs = new ArrayList<Attribut>();
        this.methodes = new ArrayList<Methode>();
    }

    // Méthode qui permet d'ajouter un attribut à la liste d'attributs
    /**
     * Ajoute un attribut à la liste des attributs.
     * @param attribut
     */
    public void ajouterAttribut(Attribut attribut) {
        attributs.add(attribut);
    }

    // Méthode qui permet d'enlever un attribut de la liste d'attributs.
    /**
     * Supprime un attribut de la liste des attributs
     * @param attribut
     */
    public void supprimerAttribut(Attribut attribut) {
        attributs.remove(attribut);
    }
    // Méthode qui permet d'ajouter une méthode à la liste de méthodes.
    /**
     * Ajoute une méthode à la liste des méthodes
     * @param methode
     */
    public void ajouterMethode(Methode methode) {
        methodes.add(methode);
    }
    // Méthode qui permet de supprimer une méthode de la liste de méthodes.

    /**
     * Supprime une méthode de la liste des méthodes
     * @param methode
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
     * @return
     */
    public List<Attribut> getAttributs() {
        return attributs;
    }

    /**
     * Retourne la liste des méthodes de la classe
     * @return Liste des méthodes de la classe
     */
    public List<Methode> getMethodes() {
        return methodes;
    }
}
