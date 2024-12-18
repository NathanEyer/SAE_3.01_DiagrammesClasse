package diagrammes.relations;

public class Implementation implements RelationStrategy {


    public Implementation() {

    }

    @Override
    public void creerLien() {
        System.out.println("Création d'un lien d'implémentation entre les classes.");
        // Logique spécifique pour créer un lien d'implémentation
    }

    public static String type(){
        return "Implementation";
    }
}
