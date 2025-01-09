package diagrammes.vue;

import diagrammes.modele.Diagramme;

/**
 * Interface observateur
 */
public interface Observateur {
    void actualiser(Diagramme diagramme)  ;
}
