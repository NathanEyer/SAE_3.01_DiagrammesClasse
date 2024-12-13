package diagramme.diag.vue;

import diagramme.diag.modele.Diagramme;

public interface Observateur {
    public void actualiser(Diagramme diagramme);
}
