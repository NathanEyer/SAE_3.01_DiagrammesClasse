package test.diagrammes;

import diagrammes.classe.Attribut;
import diagrammes.classe.Classe;
import diagrammes.classe.Methode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClasse {

    @Test
    public void test_ajouterAttribut(){
        Classe c = new Classe("Classe1");
        Attribut a = new Attribut("attribut1","String", "private");
        c.ajouterAttribut(a);
        assertEquals(1, c.getAttributs().size());
    }

    @Test
    public void supprimerAttribut(){
        Classe c = new Classe("Classe1");
        Attribut a = new Attribut("attribut1","String", "private");
        c.ajouterAttribut(a);
        c.supprimerAttribut(a);
        assertEquals(0, c.getAttributs().size());
    }

    @Test
    public void test_ajouterMethode(){
        Classe c = new Classe("Classe1");
        List<String> parametres = List.of("param1","param2");
        Methode m = new Methode("methode1","void", parametres, "public");
        c.ajouterMethode(m);
        assertEquals(1, c.getMethodes().size());
    }

    @Test
    public void test_supprimerMethode(){
        Classe c = new Classe("Classe1");
        List<String> parametres = List.of("param1","param2");
        Methode m = new Methode("methode1","void", parametres, "public");
        c.ajouterMethode(m);
        c.supprimerMethode(m);
        assertEquals(0, c.getMethodes().size());
    }

    @Test
    public void test_ajouterMethodeNull(){
        Classe c = new Classe("Classe1");
        Methode m = null;
        c.ajouterMethode(m);
        assertEquals(0, c.getMethodes().size());
    }

    @Test
    public void test_ajouterAttributNull(){
        Classe c = new Classe("Classe1");
        Attribut a = null;
        c.ajouterAttribut(a);
        assertEquals(0, c.getAttributs().size());
    }


}
