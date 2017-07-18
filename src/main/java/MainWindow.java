import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by eduardo on 02/07/17.
 */
public class MainWindow extends JFrame {
    public MainWindow ()
    {
        this.setVisible(true);
        this.setSize(200,200);
        trainTheShit();
    }
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static void trainTheShit(){
        File dir = new File("/home/eduardo/Documents/weka-java-google/train-data");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                getBinaryStringFromImage(child);
                System.out.print("\n\n\n\n\n");
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
    }

    public static String getBinaryStringFromImage(File imageFile)
    {
        BufferedImage img = null;
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {

        }
        img = resize(img,60,60);

        byte[][] pixels = new byte[img.getWidth()][];
        byte[] second = new byte[img.getWidth()*img.getHeight()];

        for (int x = 0; x < img.getWidth(); x++) {
            pixels[x] = new byte[img.getHeight()];
            System.out.println();
            for (int y = 0; y < img.getHeight(); y++) {
                pixels[x][y] = (byte) (img.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
                second[x*y]=(byte) (img.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
                System.out.print(pixels[x][y]);
            }

        }

        for (Byte B:second
             ) {
            System.out.print(B);
        }
        return second.toString();
    }
}
    
