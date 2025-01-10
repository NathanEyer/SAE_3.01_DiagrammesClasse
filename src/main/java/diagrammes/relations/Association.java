package diagrammes.relations;

import java.lang.reflect.Field;

public class Association implements RelationStrategy{
    /**
     * Constructeur vide
     */
   public Association() {}

    /**
     * Création d'un lien d'association entre les classes.
     */
    public void creerLien(String nomClasse)throws ClassNotFoundException {
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
