package diagrammes.relations;

public class Implementation implements RelationStrategy {
    public Implementation() {}

    /**
     * Crée le lien
     * @param nomClasse classe concernée
     * @throws ClassNotFoundException potentielle erreur
     */
    @Override
    public void creerLien(String nomClasse)throws ClassNotFoundException {
        Class<?> classe = Class.forName(nomClasse);
        if(classe.isInterface()){
        }

    }

    /**
     * Retourne le type
     * @return Implémentation
     */
    public static String type(){
        return "Implementation";
    }
}
