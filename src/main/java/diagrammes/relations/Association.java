package diagrammes.relations;

public class Association implements RelationStrategy{

   public Association() {}

   @Override
   /**
    * Création d'un lien d'association entre les classes.
    * @return void
    */
    public void creerLien(String nomClasse)throws ClassNotFoundException {
        System.out.println("Création d'un lien d'association entre les classes.");
        // Logique spécifique pour créer un lien d'association}
    }

    /**
     * Retourne le type de la relation
     * @return String
     */
   public static String type(){
      return "Association";
   }

}
