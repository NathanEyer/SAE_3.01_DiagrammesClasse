package diagrammes.fichier;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Exporte le diagramme au format png
 */
public class ExporterImage implements Exporter {
    /**
     * Méthode en charge de l'exportation
     * @param chemin String
     * @param contenu Object
     */
    @Override
    public void exporter(String chemin, Object contenu) {
        if (!(contenu instanceof Node node)) {
            throw new IllegalArgumentException("Le contenu doit être un Node JavaFX.");
        }

        Platform.runLater(() -> {
            try {
                WritableImage fxImage = node.snapshot(new SnapshotParameters(), null);
                BufferedImage bufferedImage = convertirImageBuffered(fxImage);
                File fichier = new File(chemin);
                File parentDir = fichier.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    if(parentDir.mkdirs())
                        System.err.println("Impossible ");
                }
                ImageIO.write(bufferedImage, "png", fichier);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Méthode en charge de la création de l'image
     * @param fxImage WritableImage
     * @return BufferedImage
     */
    private BufferedImage convertirImageBuffered(WritableImage fxImage) {
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