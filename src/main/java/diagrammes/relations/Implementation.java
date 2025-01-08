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
        System.out.println("Création d'un lien d'implémentation entre les classes.");
        Class<?> classe = Class.forName(nomClasse);
        if(classe.isInterface()){
            System.out.println("La classe est bien une interface");
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
