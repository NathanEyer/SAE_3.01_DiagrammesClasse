package diagrammes.classe;

import java.util.List;

public class Methode {
    String nomMethode;
    String typeRetour;
    List<String> parametres;

    public Methode(String nomMethode, String typeRetour, List<String> parametres) {
        this.nomMethode = nomMethode;
        this.typeRetour = typeRetour;
        this.parametres = parametres;
    }

    @Override
    public String toString() {
        return "Methode{" +
                "nomMethode='" + nomMethode + '\'' +
                ", typeRetour='" + typeRetour + '\'' +
                ", parametres=" + parametres +
                '}';
    }

    public String getNomMethode() {
        return nomMethode;
    }
}
