package diagrammes.fichier;

import diagrammes.classe.Classe;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ExporterJava {
    public void exporter(Classe classe) throws IOException {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(classe.getNom()+".java"));
            writer.write("public class "+classe.getNom()+" {\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(int i=0; i<classe.getAttributs().size(); i++) {
            if(classe.getAttributs().get(i).getModificateur().equals("-")){
                writer.write("    private "+classe.getAttributs().get(i).getTypeAttribut()+" "+classe.getAttributs().get(i).getNomAttribut()+";\n");
            } else if(classe.getAttributs().get(i).getModificateur().equals("+")){
                writer.write("    public "+classe.getAttributs().get(i).getTypeAttribut()+" "+classe.getAttributs().get(i).getNomAttribut()+";\n");
            } else if(classe.getAttributs().get(i).getModificateur().equals("#")){
                writer.write("    protected "+classe.getAttributs().get(i).getTypeAttribut()+" "+classe.getAttributs().get(i).getNomAttribut()+";\n");
            }
        }
        for(int j=0; j<classe.getMethodes().size(); j++){
            if(classe.getMethodes().get(j).getModificateur().equals("-")){
                writer.write("    private "+classe.getMethodes().get(j).getTypeRetour()+" "+classe.getMethodes().get(j).getNomMethode()+"(){\n");
                writer.write("        // Corps de la méthode\n");
                writer.write("    }\n");
            } else if(classe.getMethodes().get(j).getModificateur().equals("+")){
                writer.write("    public "+classe.getMethodes().get(j).getTypeRetour()+" "+classe.getMethodes().get(j).getNomMethode()+"(){\n");
                writer.write("        // Corps de la méthode\n");
                writer.write("    }\n");
            } else if(classe.getMethodes().get(j).getModificateur().equals("#")){
                writer.write("    protected "+classe.getMethodes().get(j).getTypeRetour()+" "+classe.getMethodes().get(j).getNomMethode()+"(){\n");
                writer.write("        // Corps de la méthode\n");
                writer.write("    }\n");
            }
        }

    }


}
