package diagrammes.vue;

import diagrammes.modele.Diagramme;

public interface Observateur {
    void actualiser(Diagramme diagramme);
}
