package ascii_art.img_to_char;

import image.FileImage;
import image.Image;
import image.SubImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A class which converts an Image to a 2D array of Characters.
 * The conversion is conducted by matching sub-images to chars with similar brightness values.
 */
public class BrightnessImgCharMatcher {
    private static final char DEFAULT_CHAR = ' ';
    private final FileImage img;
    private final String font;
    private final BrightnessMap brightnessMap;
    private final SubImageCache cache;
    private ArrayList<Character> recentChars;
    private int recentNumChars;
    private char[][] recentChosen;
    private HashMap<Character, Double> recentMap;

    /**
     * Constructs a BrightnessImgCharMatcher instance for an Image and a font.
     * @param img: Image - The image to convert to characters.
     * @param font: String - The font of the characters.
     */
    public BrightnessImgCharMatcher(Image img, String font) {
        this.img = (FileImage) img;
        this.font = font;
        brightnessMap = BrightnessMap.getObject();
        cache = new SubImageCache();
        recentChars = new ArrayList<>();
        recentMap = new HashMap<>();
        recentNumChars = -1;
    }

    /**
     * Chooses the matching characters for the image's conversion.
     * @param numCharsInRow: int - The number of chars in a single row.
     * @param charSet: Character[] - The pool of current legal chars.
     * @return char[][] - A 2D array of the matching chars chosen to represent the image.
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        Arrays.sort(charSet);
        boolean charsEqualsToRecent = charSetIsEqual(charSet);

        // optimization - if charSet and numCharsInRow didn't change, return recent chosenChars.
        if (numCharsInRow == recentNumChars && charsEqualsToRecent) {
            return recentChosen;
        }
        recentNumChars = numCharsInRow;
        recentChars.clear();
        recentChars.addAll(Arrays.asList(charSet));

        // optimization - if charSet didn't change, use recent normalized map.
        HashMap<Character, Double> normalizedMap =
                charsEqualsToRecent ? recentMap : mapAndNormalize(charSet);
        recentMap = new HashMap<Character, Double>(normalizedMap);

        int subImageSize = img.getWidth() / numCharsInRow;
        int height = img.getHeight() / subImageSize;
        char[][] chosenChars = new char[height][numCharsInRow];
        choose(charSet, normalizedMap, subImageSize, chosenChars);

        recentChosen = chosenChars.clone();
        return chosenChars;
    }

    /**
     * Chooses the chars to represent the Image as ASCII art.
     * @param charSet: Character[] - The pool of current legal chars.
     * @param normalizedMap: A HashMap of char->double, with normalized brightness values of charSet's chars.
     * @param subImageSize: int - The size of each SubImage.
     * @param chosenChars: char[][] - A 2D array of the matching chars chosen to represent the image.
     */
    private void choose(Character[] charSet, HashMap<Character, Double> normalizedMap,
                        int subImageSize, char[][] chosenChars) {
        double[][] greys = cache.lookup(subImageSize);
        // if resolution is in cache -> don't need to divide to sub-images, use greys instead.
        if (greys == null) {
            SubImage[][] subImages = (SubImage[][]) img.divideIntoSubImages(subImageSize);
            greys = new double[subImages.length][subImages[0].length];
            for (int row = 0; row < subImages.length; row++) {
                for (int col = 0; col < subImages[0].length; col++) {
                    greys[row][col] = subImages[row][col].getGreyscaleValue();
                    chosenChars[row][col] = machChar(greys[row][col], charSet, normalizedMap);
                }
            }
            cache.store(subImageSize, greys);
        } else {
            for (int row = 0; row < greys.length; row++) {
                for (int col = 0; col < greys[0].length; col++) {
                    chosenChars[row][col] = machChar(greys[row][col], charSet, normalizedMap);
                }
            }
        }
    }

    /**
     * Checks if a set of chars is the same as the previous one which is saved.
     * Assumes both sets are sorted.
     * @param charSet: Character[] - The pool of current legal chars.
     * @return true: if the same, false: otherwise.
     */
    private boolean charSetIsEqual(Character[] charSet) {
        if (charSet.length != recentChars.size()) {
            return false;
        }
        for (int i = 0; i < charSet.length; i++) {
            if (!charSet[i].equals(recentChars.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Matches a char to a single sub-image.
     * @param subImageValue: double - The sub-image's greyscale value.
     * @param charSet: The pool of legal chars.
     * @return char: The matching char.
     */
    private char machChar(double subImageValue, Character[] charSet,
                          HashMap<Character, Double> normalizedMap) {
        double minDistance = 1;
        char matchingChar = DEFAULT_CHAR;
        for (Character c : charSet) {
            if (brightnessMap.containsKey(c)) {
                double distance = Math.abs(normalizedMap.get(c) - subImageValue);
                if (distance < minDistance) {
                    minDistance = distance;
                    matchingChar = c;
                }
            }
        }
        return matchingChar;
    }

    /**
     * Validates all legal chars are already mapped into the BrightnessMap.
     * Gets a normalized map of chars pool (charSet) values.
     * @param charSet The pool of legal chars.
     * @return HashMap<Character, Double>: The normalized brightness map.
     */
    private HashMap<Character, Double> mapAndNormalize(Character[] charSet) {
        for (Character c: charSet) {
            brightnessMap.add(c, font);
        }
        return brightnessMap.normalize(charSet);
    }
}
