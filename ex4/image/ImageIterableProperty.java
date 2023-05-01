package image;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

/**
 * A package-private class of the package src.src.image.
 * @author Dan Nirel
 */
class ImageIterableProperty<T> implements Iterable<T> {
    private final Image img;
    private final BiFunction<Integer, Integer, T> propertySupplier;

    /**
     * Constructor of the Iterable.
     * @param img The image.
     * @param propertySupplier The BiFunction determines the return value from each iteration.
     */
    public ImageIterableProperty(
            Image img,
            BiFunction<Integer, Integer, T> propertySupplier) {
        this.img = img;
        this.propertySupplier = propertySupplier;
    }

    /**
     * The Iterable's iterator method.
     * @return The Iterable's Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int x = 0, y = 0;

            /**
             * Checks if the Iterator has a next item.
             * @return true: if iterator has next. false: otherwise.
             */
            @Override
            public boolean hasNext() {
                return y < img.getHeight();
            }

            /**
             * Moves the Iterator forward to it's next item.
             * @return the next item of the iterator.
             */
            @Override
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                var next = propertySupplier.apply(x, y);
                x += 1;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += 1;
                }
                return next;
            }
        };
    }
}
