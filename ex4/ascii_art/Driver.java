package ascii_art;

import image.Image;

import java.util.logging.Logger;

/**
 * The class running the main function of the program.
 * Loads the Image from CLI arguments and runs the Shell.
 */
public class Driver {
    private static final String USAGE_ERROR_MSG = "USAGE: java asciiArt ";
    private static final String OPEN_IMG_ERROR_MSG = "Failed to open src.src.image file ";

    /**
     * Default constructor.
     */
    public Driver() {}

    /**
     * The main function of the program.
     * Loads the Image of a file address given from CLI and runs the Shell.
     * @param args CLI arguments.
     * @throws Exception if an error occurs while loading the Image from file.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(USAGE_ERROR_MSG);
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe(OPEN_IMG_ERROR_MSG + args[0]);
            return;
        }
        new Shell(img).run();
    }
}