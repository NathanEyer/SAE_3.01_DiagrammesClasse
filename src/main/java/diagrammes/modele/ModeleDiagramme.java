package diagrammes.modele;

import diagrammes.relations.Relation;
import diagrammes.classe.Classe;
import diagrammes.exporter.Exporter;
import diagrammes.vue.Observateur;


public class ModeleDiagramme implements Diagramme, Exporter {
    public void addClass(Classe classe){}
    public void addRelation(Relation relation){}
    public boolean exporter(String type){return true;}
    public void enregistrerObservateur(Observateur o){}
    public void supprimerObservateur(Observateur o){}
    public void notifierObservateur(){}
}
