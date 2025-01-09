package diagrammes.relations;

public class Implementation implements RelationStrategy {
    /**
     * Constructeur vide
     */
    public Implementation() {}

    /**
     * Crée le lien
     * @param nomClasse classe concernée
     * @throws ClassNotFoundException potentielle erreur
     */
    @Override
    public void creerLien(String nomClasse)throws ClassNotFoundException {
        Class<?> classe = Class.forName(nomClasse);
    }

    /**
     * Retourne le type
     * @return Implémentation
     */
    public static String type(){
        return "Implementation";
    }
}
