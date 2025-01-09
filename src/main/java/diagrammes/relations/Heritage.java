package diagrammes.relations;

public class Heritage implements RelationStrategy {
    public Heritage() {}

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
