package diagrammes.vue;

import diagrammes.modele.Diagramme;

/**
 * Interace observateur
 */
public interface Observateur {
    void actualiser(Diagramme diagramme);
}
