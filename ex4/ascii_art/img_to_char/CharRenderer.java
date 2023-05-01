package ascii_art.img_to_char;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Adaptations made by Dan Nirel.
 * The class renders (draws) characters to a binary "src.src.image" (2D array of booleans).
 */
public class CharRenderer {
    private static final double X_OFFSET_FACTOR = 0.2;
    private static final double Y_OFFSET_FACTOR = 0.75;

    /**
     * Renders a given character, according to how it looks in the font specified in the
     * constructor, to a square black and white src.image (2D array of booleans),
     * whose dimension in pixels is specified.
     */
    public static boolean[][] getImg(char c, int pixels, String fontName) {
        int key = (pixels << 8) | c;
        return render(c, pixels, fontName);
    }
    private static boolean[][] render(char c, int pixels, String fontName) {
        String charStr = Character.toString(c);
        Font font = new Font(fontName, Font.PLAIN, pixels);
        BufferedImage img = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setFont(font);
        int xOffset = (int)Math.round(pixels*X_OFFSET_FACTOR);
        int yOffset = (int)Math.round(pixels*Y_OFFSET_FACTOR);
        g.drawString(charStr, xOffset, yOffset);
        boolean[][] matrix = new boolean[pixels][pixels];
        for(int y = 0 ; y < pixels ; y++) {
            for(int x = 0 ; x < pixels ; x++) {
                matrix[y][x] = img.getRGB(x, y) == 0; //is the color black
            }
        }
        return matrix;
    }

    /**
     * For debugging
     * @param arr Boolean array representing the char in "black" and "white".
     */
    public static void printBoolArr(boolean[][] arr) {
        for (boolean[] booleans : arr) {
            for (boolean aBoolean : booleans) {
                System.out.print(aBoolean);
            }
            System.out.println();
        }
    }

}
