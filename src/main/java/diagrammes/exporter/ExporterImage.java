package diagrammes.exporter;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExporterImage implements Exporter {

    @Override
    public void exporter(String chemin, Object contenu) throws Exception {
        if (!(contenu instanceof Node)) {
            throw new IllegalArgumentException("Le contenu doit être un Node JavaFX.");
        }

        Node node = (Node) contenu;

        Platform.runLater(() -> {
            try {
                WritableImage fxImage = node.snapshot(new SnapshotParameters(), null);
                BufferedImage bufferedImage = convertirImageBuffered(fxImage);
                File fichier = new File(chemin);
                File parentDir = fichier.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                ImageIO.write(bufferedImage, "png", fichier);

                System.out.println("Exportation réussie en PNG vers : " + chemin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private BufferedImage convertirImageBuffered(WritableImage fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // Permet de lire les données de couleur d'une image.
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