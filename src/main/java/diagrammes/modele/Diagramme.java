package diagrammes.modele;

import diagrammes.vue.Observateur;

public interface Diagramme {
    public void enregistrerObservateur(Observateur observateur);

    public void supprimerObservateur(Observateur observateur);

    public void notifierObservateur();
}
