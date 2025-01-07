package diagrammes.modele;

import diagrammes.vue.Observateur;

/**
 * Interface du diagramme
 */
public interface Diagramme {
    void enregistrerObservateur(Observateur observateur);
    void supprimerObservateur(Observateur observateur);
    void notifierObservateur() throws ClassNotFoundException;
}
