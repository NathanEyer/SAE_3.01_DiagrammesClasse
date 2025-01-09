package diagrammes.relations;

public class Heritage implements RelationStrategy {
    /**
     * Constructeur vide
     */
    public Heritage() {}

    /**
     * Implementation de la méthode creerlien
     * @param nomClasse nom de la classe
     * @throws ClassNotFoundException potentielle exception
     */
    @Override
    public void creerLien(String nomClasse) throws ClassNotFoundException{
        Class<?> classe = Class.forName(nomClasse);
    }

    /**
     * Retourne le type
     * @return "Heritage"
     */
    public static String type(){
        return "Heritage";
    }
}
