package test.diagrammes;


import diagrammes.classe.Classe;
import diagrammes.relations.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestRelation {

    @Test
    public void testCreerRelationHeritage() {
        Classe classeDepart = new Classe("ClasseParent");
        Classe classeDestination = new Classe("ClasseEnfant");
        RelationStrategy heritage = new Heritage();

        Relation relation = new Relation(classeDepart, classeDestination, heritage);

        assertEquals(classeDepart, relation.getDepart());
        assertEquals(classeDestination, relation.getDestination());
        assertEquals(heritage, relation.getType());
    }

    @Test
    public void testCreerRelationImplementation() {
        Classe classeDepart = new Classe("ClasseInterface");
        Classe classeDestination = new Classe("ClasseConcrète");
        RelationStrategy implementation = new Implementation();

        Relation relation = new Relation(classeDepart, classeDestination, implementation);

        assertEquals(classeDepart, relation.getDepart());
        assertEquals(classeDestination, relation.getDestination());
        assertEquals(implementation, relation.getType());
    }

    @Test
    public void testCreerRelationAssociation() {
        Classe classeDepart = new Classe("ClasseSource");
        Classe classeDestination = new Classe("ClasseCible");
        RelationStrategy association = new Association();

        Relation relation = new Relation(classeDepart, classeDestination, association);

        assertEquals(classeDepart, relation.getDepart());
        assertEquals(classeDestination, relation.getDestination());
        assertEquals(association, relation.getType());
    }

    @Test
    public void testHeritageCreerLien() throws ClassNotFoundException {
        RelationStrategy heritage = new Heritage();
        heritage.creerLien("java.util.List");

        assertNotNull(heritage);
    }

    @Test
    public void testImplementationCreerLien() throws ClassNotFoundException {
        RelationStrategy implementation = new Implementation();
        implementation.creerLien("java.util.ArrayList");

        assertNotNull(implementation);
    }

    @Test
    public void testAssociationCreerLien() throws ClassNotFoundException {
        RelationStrategy association = new Association();
        association.creerLien("java.lang.String");

        assertNotNull(association);
    }
}