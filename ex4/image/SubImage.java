package image;

import java.awt.*;

/**
 * A SubImage class representing a sub-image of a whole-bigger image.
 * Is squared - width and height are equal.
 * Has an average normalized greyscale value representing the entire sub-image.
 */
public class SubImage implements Image {
    private static final double RED_VALUE = 0.2126;
    private static final double GREEN_VALUE = 0.7152;
    private static final double BLUE_VALUE = 0.0722;
    private final Color[][] subImage;
    private final int size;
    private final double greyscaleValue;
    /**
     * Constructs a SubImage instance, initializes all the sub-images data-members.
     * @param subImage: Color[][] - 2D array representing the sub-image's pixels.
     * @param size: int - The size of the sub-image's width and height.
     */
    public SubImage(Color[][] subImage, int size) {
        this.subImage = subImage;
        this.size = size;
        double greyscaleSum = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                greyscaleSum += pixelToGreyscale(subImage[row][col]);
            }
        }
        greyscaleValue = (greyscaleSum / (size * size)) / 255;
    }

    /**
     * Getter: gets the color of the pixel at (x, y).
     * @param x: int - The column number.
     * @param y: int - The row number.
     * @return Color: the color of the pixel which its coordinates are (x, y).
     *                if coordinates aren't legal, return default color white.
     */
    @Override
    public Color getPixel(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y <= size) {
            return subImage[y][x];
        }
        return Color.WHITE;
    }

    /**
     * Getter: get the width of the image in pixels.
     * @return int: The width of the image.
     */
    @Override
    public int getWidth() {
        return size;
    }

    /**
     * Getter: get the height of the image in pixels.
     * @return int: The height of the image.
     */
    @Override
    public int getHeight() {
        return size;
    }

    /**
     * Getter: gets the normalized greyscale value of the sub-image.
     * @return double: The sub-image's greyscale value.
     */
    public double getGreyscaleValue() {
        return greyscaleValue;
    }

    /**
     * Gets a colour and returns a greyscale value according to a specific formula.
     * @param color: Color - a color of a pixel to convert into greyscale.
     * @return int: representing a greyscale value.
     */
    private static double pixelToGreyscale(Color color) {
        return ((color.getRed() * RED_VALUE) +
                        (color.getGreen() * GREEN_VALUE) +
                        (color.getBlue() * BLUE_VALUE));
    }
}
