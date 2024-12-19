package diagrammes.relations;

public class Heritage implements RelationStrategy {


    public Heritage() {

    }

    @Override
    public void creerLien() {
        System.out.println("Création d'un lien d'héritage entre les classes.");
        // Logique spécifique pour créer un lien d'héritage
    }

    public static String type(){
        return "Heritage";
    }
}
