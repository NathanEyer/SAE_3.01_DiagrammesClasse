package diagramme.diag.modele;

import diagramme.diag.relations.Relation;
import diagramme.diag.classe.Classe;
import diagramme.diag.exporter.Exporter;
import diagramme.diag.vue.Observateur;


public class ModeleDiagramme implements Diagramme, Exporter {
    public void addClass(Classe classe){}
    public void addRelation(Relation relation){}
    public boolean exporter(String type){return true;}
    public void enregistrerObservateur(Observateur o){}
    public void supprimerObservateur(Observateur o){}
    public void notifierObservateur(){}
}
