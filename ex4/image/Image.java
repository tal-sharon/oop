package image;

import java.awt.*;
import java.io.IOException;

/**
 * Facade for the src.image module and an interface representing a src.image.
 * @author Dan Nirel
 */
public interface Image {
    /**
     * Getter: gets the color of the pixel at (x, y).
     * @param x: int - The column number.
     * @param y: int - The row number.
     * @return Color: the color of the pixel which its coordinates are (x, y).
     */
    Color getPixel(int x, int y);

    /**
     * Getter: get the width of the image in pixels.
     * @return int: The width of the image.
     */
    int getWidth();

    /**
     * Getter: get the height of the image in pixels.
     * @return int: The height of the image.
     */
    int getHeight();

    /**
     * Open a src.image from file. Each dimensions of the returned src.image is guaranteed
     * to be a power of 2, but the dimensions may be different.
     * @param filename a path to a src.image file on disk
     * @return an object implementing Image if the operation was successful,
     * null otherwise
     */
    static Image fromFile(String filename) {
        try {
            return new FileImage(filename);
        } catch(IOException ioe) {
            return null;
        }
    }

    /**
     * Allows iterating the pixels' colors by order (first row, second row and so on).
     * @return an Iterable of Color that can be traversed with a foreach loop
     */
    default Iterable<Color> pixels() {
        return new ImageIterableProperty<>(
                this, this::getPixel);
    }

    /**
     * Allows iterating the image's sub-images by order:
     *      (first row - first->last column)  ->  (second row - first->last column)  ->  (and so on)
     * Divides the image to sub-images of given size.
     * @param size: int - The size of a single sub-image (size x size).
     * @return Iterable of Image: That can be traversed with a foreach loop.
     */
    default Iterable<Image> subImages(int size) {
        return new SubImageIterable(this, size);
    }

    /**
     * Divides and Image to sub-images of given size.
     * Image's width and height should be modulo to sub-image size.
     * @param size: int - The sub-images size - (size x size).
     * @return Image[][]: 2D array of sub-images representing the whole original Image.
     */
    default Image[][] divideIntoSubImages(int size) {
        int numCols = getWidth() / size;
        int numRows = getHeight() / size;
        SubImage[][] subImages = new SubImage[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                buildSubImages(size, subImages, row, col);
            }
        }
        return subImages;
    }

    /**
     * Builds the SubImage array.
     * @param size SubImage size.
     * @param subImages SubImage 2D array representing the original Image.
     * @param row Current row in original Image.
     * @param col Current columns in original Image.
     */
    private void buildSubImages(int size, SubImage[][] subImages, int row, int col) {
        Color[][] subImage = new Color[size][size];
        int topLeftY = row * size;
        int topLeftX = col * size;
        for (int i = 0; i < size; i ++) {
            for (int j = 0; j < size; j++) {
                subImage[j][i] = getPixel(topLeftX + j, topLeftY + i);
            }
        }
        subImages[row][col] = new SubImage(subImage , size);
    }
}
