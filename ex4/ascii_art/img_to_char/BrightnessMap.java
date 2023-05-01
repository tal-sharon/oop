package ascii_art.img_to_char;

import java.util.HashMap;

/**
 * A class implementing a singleton which maps Characters to their brightness value.
 * Has only one single instance which is constructed by itself.
 * Any object of the program can access this instance with getObject method.
 * Uses a HashMap to store its data.
 */
public class BrightnessMap {
    private static final int DEFAULT_RENDER_RESOLUTION_SIZE = 16;
    private static BrightnessMap brightnessMap;
    private final HashMap<Character, Double> charsMap = new HashMap<>();

    /**
     * Private constructor of a singleton.
     * This implementation allows only the instance itself to conduct initialization.
     */
    private BrightnessMap() {}

    /**
     * Getter: gets the single instance of the BrightnessMap.
     * @return BrightnessMap - The singleton's instance.
     */
    public static BrightnessMap getObject() {
        if (brightnessMap != null) {
            return brightnessMap;
        }
        brightnessMap = new BrightnessMap();
        return brightnessMap;
    }

    /**
     * Adds a Character to the brightness map.
     *  key = char, value = brightness value.
     * Calculates the character's brightness value and puts them in the map.
     * @param key: char - The char to be valued by brightness and map.
     * @param font: String - The char's font, effects the brightness.
     * @return double: The brightness value of the inserted char.
     */
    public double add(char key, String font) {
        if (charsMap.containsKey(key)) {
            return charsMap.get(key);
        }
        boolean[][] charImg =  CharRenderer.getImg(key, DEFAULT_RENDER_RESOLUTION_SIZE, font);
        double whiteCount = 0;
        for (boolean[] booleans:charImg) {
            for (boolean bool:booleans) {
                if (bool) {
                    whiteCount++;
                }
            }
        }
        double charBrightness =
                whiteCount / (DEFAULT_RENDER_RESOLUTION_SIZE * DEFAULT_RENDER_RESOLUTION_SIZE);
        charsMap.put(key, charBrightness);
        return charBrightness;
    }

    /**
     * Getter: gets a key's value.
     * @param key: char - A key to get from the map.
     * @return double: The given key's value.
     */
    public double get(char key) {
        return charsMap.get(key);
    }

    /**
     * Checks if the map contains a given key.
     * @param key: char - A given key.
     * @return boolean: true: if the map contains the key. false: otherwise.
     */
    public boolean containsKey(char key) {
        return charsMap.containsKey(key);
    }

    /**
     * Generates a normalized brightness map according a given Character's array.
     * @param chars: Character[] - A given array to generate the normalized map from.
     * @return A normalized map.
     */
    public HashMap<Character, Double> normalize(Character[] chars) {
        double minValue = 1;
        double maxValue = 0;
        for (Character c: chars) {
            minValue = Math.min(minValue, charsMap.get(c));
            maxValue = Math.max(maxValue, charsMap.get(c));
        }
        HashMap<Character, Double> normalizedMap = new HashMap<>();
        for (Character c: chars) {
            double charBrightness = charsMap.get(c);
            double newCharBrightness = (charBrightness - minValue) / (maxValue - minValue);
            if (minValue != maxValue) {
                normalizedMap.put(c, newCharBrightness);
            } else {
                normalizedMap.put(c, minValue);
            }
        }
        return normalizedMap;
    }
}
