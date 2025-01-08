package diagrammes.relations;

import java.lang.reflect.Modifier;

public class Heritage implements RelationStrategy {
    public Heritage() {}

    @Override
    public void creerLien(String nomClasse) throws ClassNotFoundException{
        System.out.println("Création d'un lien d'héritage entre les classes.");
        Class<?> classe = Class.forName(nomClasse);
        if (Modifier.isAbstract(classe.getModifiers())) {
            System.out.println("La classe " + nomClasse + " est abstraite.");
        } else {
            System.out.println("La classe " + nomClasse + " n'est pas abstraite.");
        }

    }

    /**
     * Retourne le type
     * @return "Heritage"
     */
    public static String type(){
        return "Heritage";
    }
}
