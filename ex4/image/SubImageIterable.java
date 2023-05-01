package image;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterable class which enables iterating over an Image by SubImage's
 */
public class SubImageIterable implements Iterable<Image> {
    private final Image[][] subImages;

    /**
     * Constructor of the Iterable.
     * @param img The image.
     * @param size The size of each sub-image.
     */
    public SubImageIterable(Image img, int size) {
        subImages = img.divideIntoSubImages(size);
    }

    /**
     * The Iterable's iterator method.
     * @return The Iterable's Iterator.
     */
    @Override
    public Iterator<Image> iterator() {
        class Iter implements Iterator<Image> {
            int row = 0, col =0;

            /**
             * Checks if the Iterator has a next item.
             * @return true: if iterator has next. false: otherwise.
             */
            @Override
            public boolean hasNext() {
                return row < subImages.length;
            }

            /**
             * Moves the Iterator forward to it's next item.
             * @return the next item of the iterator.
             */
            @Override
            public Image next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Image next = subImages[row][col];
                col ++;
                if (col >= subImages[0].length) {
                    col = 0;
                    row ++;
                }
                return next;
            }
        }
        return new Iter();
    }

}
