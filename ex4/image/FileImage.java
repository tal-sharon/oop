package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package src.image.
 * @author Dan Nirel
 */
public class FileImage implements Image {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private final Color[][] pixelArray;
    private final int newWidth;
    private final int newHeight;

    /**
     * Constructs a FileImage instance.
     * Pads the original Image with white pixels - Completes the width and height to be a power of 2.
     * @param filename      a path to a src.src.image file on disk
     * @throws IOException  Signals that reading the given file resulted an exception to be thrown.
     */
    public FileImage(String filename) throws IOException {
        java.awt.image.BufferedImage im = ImageIO.read(new File(filename));
        int origWidth = im.getWidth(), origHeight = im.getHeight();
        newWidth = nextPowerOfTwo(origWidth);
        newHeight = nextPowerOfTwo(origHeight);
        pixelArray = new Color[newHeight][newWidth];
        fillPixelArray(im, origWidth, origHeight);
    }

    /**
     * Fills the instance's pixelArray.
     * @param im:           BufferedImage - The given src.image.
     * @param origWidth:    int - The original width of the src.image.
     * @param origHeight:   int - The original height of the src.image.
     */
    private void fillPixelArray(BufferedImage im, int origWidth, int origHeight) {
        int leftPad = (newWidth - origWidth) / 2;
        int rightPad = leftPad;
        int upPad = (newHeight - origHeight) / 2;
        int downPad = upPad;
        leftPad = ifNotEven(origWidth, leftPad);
        upPad = ifNotEven(origHeight, upPad);
        for (int row = 0; row < newHeight; row++) {
            for (int col = 0; col < newWidth; col++) {
                if (row < upPad || row >= origHeight + downPad ||
                        col < leftPad || col >= origWidth + rightPad) {
                    pixelArray[row][col] = DEFAULT_COLOR;
                } else {
                    pixelArray[row][col] = new Color(im.getRGB(col - leftPad, row - upPad));
                }
            }
        }
    }

    private static int ifNotEven(int original, int pad) {
        if (original % 2 == 1) {
            pad++;
        }
        return pad;
    }

    /**
     * Returns the new padded width of the FileImage.
     * @return int - The width of the instance.
     */
    @Override
    public int getWidth() {
        return newWidth;
    }

    /**
     * Returns the new padded height of the FileImage.
     * @return int - The height of the instance.
     */
    @Override
    public int getHeight() {
        return newHeight;
    }

    /**
     * Returns the Color of a given pixel with (x, y) coordinates.
     * @param x: int - The x value of the pixel - column.
     * @param y: int - The y value of the pixel - row.
     * @return Color - The color of the pixel.
     *                  if coordinates aren't legal, return default color white.
     */
    @Override
    public Color getPixel(int x, int y) {
        if (x >= 0 && x < newWidth && y >= 0 && y <= newHeight) {
            return pixelArray[y][x];
        }
        return Color.WHITE;
    }

    /**
     * Calculates the smallest power of two which is great or equal to a given integer.
     * @param a: int a given integer.
     * @return The smallest power of two which is great or equal to 'a'.
     */
    private static int nextPowerOfTwo(int a) {
        int b = 1;
        while (b < a) {
            b *= 2;
        }
        return b;
    }
}
