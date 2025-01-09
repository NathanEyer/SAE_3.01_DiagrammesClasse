package diagrammes.modele;

import diagrammes.classe.Classe;
import diagrammes.relations.Relation;
import diagrammes.vue.Observateur;

import java.util.List;

/**
 * Interface du diagramme
 */
public interface Diagramme {
    void enregistrerObservateur(Observateur observateur);
    void notifierObservateur() throws ClassNotFoundException;
    List<Classe> getClasses();
    List<Relation> getRelations();
}
