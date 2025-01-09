package diagrammes.relations;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Association implements RelationStrategy{


   public Association() {}

   @Override
   /**
    * Création d'un lien d'association entre les classes.
    * @return void
    */
    public void creerLien(String nomClasse)throws ClassNotFoundException {
        // Logique spécifique pour créer un lien d'association}
       Class<?> classe = Class.forName(nomClasse);
       Field[] champs = classe.getDeclaredFields();
    }

    /**
     * Retourne le type de la relation
     * @return String
     */
   public static String type(){
      return "Association";
   }
}
