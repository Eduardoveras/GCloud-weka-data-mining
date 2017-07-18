import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by eduardo on 02/07/17.
 */
public class MainWindow extends JFrame {

    public static final int ROWS = 60;
    public static final int COLS = 60;


    public MainWindow() {
        this.setVisible(true);
        this.setSize(200, 200);


        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 728, 537);
        //setBounds(0, 0, 728, 537);

        getContentPane().add(panel);
        trainTheShit();

        JFileChooser chooser = new JFileChooser();

        // Add listener on chooser to detect changes to selected file
        chooser.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY
                        .equals(evt.getPropertyName())) {
                    JFileChooser chooser = (JFileChooser) evt.getSource();
                    File oldFile = (File) evt.getOldValue();
                    File newFile = (File) evt.getNewValue();
                    File curFile = chooser.getSelectedFile();
                    //System.out.println(getBinaryStringFromImage(curFile));
                }
            }
        });
        //panel.add(chooser);

    }


    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static void trainTheShit() {
        File dir = new File("train-data");
        File[] directoryListing = dir.listFiles();

        for (File folder : directoryListing) {
            if (folder.isDirectory()) {
                System.out.println("Directory: " + folder.getName());
                for (File file : folder.listFiles()) {
                    System.out.println(file.getName());

                    /* TODO: HERE WE SHOULD WRITE TO A COPY OF THE WEKA FILE
                    BUT THE IMAGES HAVE TOO MUCH NOISE...
                    */
                    //getBinaryStringFromImage(file);

                }

            }
        }

    }

    public static String getBinaryStringFromImage(File imageFile) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {

        }
        img = resize(img, ROWS, COLS);
        byte[][] pixels = new byte[img.getWidth()][];

        for (int x = 0; x < img.getWidth(); x++) {
            pixels[x] = new byte[img.getHeight()];
            System.out.println();
            for (int y = 0; y < img.getHeight(); y++) {
                pixels[x][y] = (byte) (img.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
                System.out.print(pixels[x][y]);
            }
        }

        String second = matrixToStrings(pixels);
        System.out.println("YADA YADA");
        System.out.println(second);
        System.out.println("YADA YADA");

        return second;
    }

    public static String matrixToStrings(byte[][] pixels) {
        String str = "";

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                str += pixels[i][j] + ",";
            }
            str += "";
        }

        return str;
    }


}

