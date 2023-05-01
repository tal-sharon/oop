package ascii_art.img_to_char;

/**
 * A cache class, storing greyscale values of 12 different SubImage divisions.
 * When gets a new value and cache is full, erases the earliest value.
 */
public class SubImageCache {
    private static final int CACHE_CAPACITY = 12;
    private final Node[] cache;
    private int counter;
    private boolean cacheIsFull;

    /**
     * Cache's constructor.
     * Initializes its parameters, and the cache's data structure.
     */
    public SubImageCache() {
        cache = new Node[CACHE_CAPACITY];
        counter = 0;
        cacheIsFull = false;
    }

    /**
     * Stores new Nodes inside the cache's database. Runs over old ones if necessary.
     * @param res Node's resolution value.
     * @param greys The resolution's matching greyscale values .
     */
    public void store(int res, double[][] greys) {
        if (lookup(res) != null) {
            return;     // res is already in cache
        }
        cache[counter] = new Node(res, greys);
        if (!cacheIsFull && counter == CACHE_CAPACITY - 1) {
            cacheIsFull = true;
        }
        counter = (counter + 1) % CACHE_CAPACITY;
    }

    /**
     * Checks if a given res is store inside the cache.
     * @param res Looked up resolution value.
     * @return if res is in cache: the resolution's greys values. else: null.
     */
    public double[][] lookup(int res) {
        int loopLength = CACHE_CAPACITY;
        if (!cacheIsFull) {
            loopLength = counter;
        }
        for (int i = 0; i < loopLength; i++) {
            if (cache[i].getRes() == res) {
                return cache[i].getGreys();
            }
        }
        return null;
    }

    /**
     * A static nested class, used to represent a Node inside the Cache.
     * Has a resolution value and the matching greyscale values.
     */
    private static class Node {
        int resValue;
        double[][] greys;

        /**
         * Constructor of node.
         * @param res Node's resolution value.
         * @param greys The resolution's matching greyscale values .
         */
        public Node(int res, double[][] greys) {
            resValue = res;
            this.greys = greys;
        }

        /**
         * Getter: gets Node's resolution.
         * @return resolution.
         */
        public int getRes() {
            return resValue;
        }

        /**
         * Getter: gets Node's greys.
         * @return greys.
         */
        public double[][] getGreys() {
            return greys;
        }
    }
}
