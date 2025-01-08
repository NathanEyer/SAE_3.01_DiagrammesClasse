package diagrammes.relations;


import diagrammes.vue.VueDiagramme;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;



public class Implementation implements RelationStrategy {
    public Implementation() {
    }


    /**
     * @param nomClasse
     * @throws ClassNotFoundException
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
     * @return le type
     */
    public static String type(){
        return "Implementation";
    }
}
