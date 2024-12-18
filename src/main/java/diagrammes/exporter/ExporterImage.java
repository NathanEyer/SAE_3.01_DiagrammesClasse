package diagrammes.exporter;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExporterImage implements Exporter {

    /**
     * Exporte un Node JavaFX en tant qu'image PNG.
     *
     * @param chemin  Le chemin où enregistrer le fichier PNG.
     * @param contenu Le Node JavaFX à capturer.
     * @throws Exception Si une erreur survient pendant l'exportation.
     */
    @Override
    public void exporter(String chemin, Object contenu) throws Exception {
        if (!(contenu instanceof Node)) {
            throw new IllegalArgumentException("Le contenu doit être un Node JavaFX.");
        }

        Node node = (Node) contenu;

        WritableImage fxImage = node.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = convertWritableImageToBufferedImage(fxImage);
        File fichier = new File(chemin);
        File parentDir = fichier.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        ImageIO.write(bufferedImage, "png", fichier);

        System.out.println("Exportation réussie en PNG vers : " + chemin);
    }

    private BufferedImage convertWritableImageToBufferedImage(WritableImage fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        PixelReader pixelReader = fxImage.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixelReader.getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }
        return bufferedImage;
    }
}