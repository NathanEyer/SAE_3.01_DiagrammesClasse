package diagrammes.vue;

import diagrammes.modele.Diagramme;

public interface Observateur {
    public void actualiser(Diagramme diagramme);
}
