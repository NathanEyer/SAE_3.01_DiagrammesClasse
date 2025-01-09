package diagrammes.relations;

public class Heritage implements RelationStrategy {
    public Heritage() {}


    /**
     * Implementation de la méthode créer lien
     * @param nomClasse
     * @throws ClassNotFoundException
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
