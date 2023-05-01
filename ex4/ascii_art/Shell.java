package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_art.img_to_char.BrightnessMap;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

/**
 * The Shell of the program.
 * Used to configure the program, add and remove chars, adjust the output's resolution, render and exit.
 */
public class Shell {
    private static final String EXIT_COMMAND = "exit";
    private static final String ADD_COMMAND = "add";
    private static final String REMOVE_COMMAND = "remove";
    private static final String RES_COMMAND = "res";
    private static final String CONSOLE_COMMAND = "console";
    private static final String RENDER_COMMAND = "render";
    private static final String CHARS_COMMAND = "chars";
    private static final String ADD_FORMAT_ERROR = "Did not add due to incorrect format";
    private static final String REMOVE_FORMAT_ERROR = "Did not remove due to incorrect format";
    private static final String RES_BOUNDARIES_ERROR = "Did not change due to exceeding boundaries";
    private static final String INCORRECT_COMMAND_ERROR = "Did not executed due to incorrect command";
    private static final String NO_CHARS_TO_RENDER_MSG = "Did not executed due to lack of chars";
    private static final String ALL_COMMAND = "all";
    private static final String SPACE_COMMAND = "space";
    private static final String SET_WIDTH_MSG = "Width set to ";
    private static final String UP_COMMAND = "up";
    private static final String DOWN_COMMAND = "down";
    private static final String SPACE_STRING = " ";
    private static final char SPACE_CHAR = ' ';
    private static final char SEPARATOR = '-';
    private static final String PRE_PRINT = ">>> ";
    private static final String FONT = "Courier New";
    private static final String OUT_HTML = "out.html";
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MULTIPLE_CHARS_COMMAND_LEN = 3;
    private static final int SPACE_ASCII_VAL = 32;
    private static final int FIRST_CHAR_VAL = 33;
    private static final int LAST_CHAR_VAL = 126;
    private static final int ZERO_ASCII_VAL = 48;
    private static final int NINE_ASCII_VAL = 57;
    private static final int COMMAND = 0;
    private static final int INPUT = 1;
    private static final int SPLIT_LIMIT = 2;
    private static final String NULL_STRING = "";
    private final BrightnessMap brightnessMap;
    private final HashSet<Character> chars;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private final BrightnessImgCharMatcher charMatcher;
    private int charsInRow;
    private boolean outputToHTML;

    /**
     * Constructs a Shell instance.
     * Initializes all the default parameters: charsInRow, outputToHTML, BrightnessMap.
     * @param img: Image - The image the program is running with.
     */
    public Shell(Image img) {
        minCharsInRow = Math.max(1, img.getWidth() / img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        outputToHTML = true;
        chars = new HashSet<>();
        brightnessMap = BrightnessMap.getObject();
        for (int c = ZERO_ASCII_VAL; c <= NINE_ASCII_VAL; c++) {
            chars.add((char) c);
            brightnessMap.add((char) c, FONT);
        }
        charMatcher = new BrightnessImgCharMatcher(img, FONT);

    }

    /**
     * Runs the shell until an exit command is inserted.
     * Gets input from user and executes the matching commands.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(PRE_PRINT);
            String[] inputArray = scanner.nextLine().split(SPACE_STRING, SPLIT_LIMIT);
            if (inputArray.length == 1 ||
                    (inputArray.length == 2 && Objects.equals(inputArray[1], NULL_STRING))) {
                if (singleWordCommands(inputArray[0])) return;
            } else {
                multipleWordCommands(inputArray[COMMAND], inputArray[INPUT]);
            }
        }
    }

    /**
     * Parses and executes commands with multiple words.
     * @param command: String - The command.
     * @param input: String - The additional information for the command.
     */
    private void multipleWordCommands(String command, String input) {
        switch (command) {
            case ADD_COMMAND:
                addRemoveCommand(input, true, ADD_FORMAT_ERROR);
                break;
            case REMOVE_COMMAND:
                addRemoveCommand(input, false, REMOVE_FORMAT_ERROR);
                break;
            case RES_COMMAND:
                resolutionCommand(input);
                break;
            default:
                System.out.println(INCORRECT_COMMAND_ERROR);
        }
    }

    /**
     * Parses and executes commands with single word.
     * @param command: String - The command.
     * @return true: if command is "exit".
     *         false: otherwise.
     */
    private boolean singleWordCommands(String command) {
        switch (command) {
            case EXIT_COMMAND:
                return true;
            case CHARS_COMMAND:
                charsCommand();
                break;
            case CONSOLE_COMMAND:
                outputToHTML = false;
                break;
            case RENDER_COMMAND:
                renderCommand();
                break;
            default:
                System.out.println(INCORRECT_COMMAND_ERROR);
        }
        return false;
    }

    /**
     * Executes a render command.
     */
    private void renderCommand() {
        if (chars.isEmpty()) {
//            System.out.println(NO_CHARS_TO_RENDER_MSG);
            return;
        }
        HtmlAsciiOutput htmlOutput = new HtmlAsciiOutput(OUT_HTML, FONT);
        ConsoleAsciiOutput consoleOutput = new ConsoleAsciiOutput();
        Character[] charArray = new Character[chars.size()];
        chars.toArray(charArray);
        char[][] chosenChars = charMatcher.chooseChars(charsInRow, charArray);
        if (outputToHTML) {
            htmlOutput.output(chosenChars);
        } else {
            consoleOutput.output(chosenChars);
        }
    }

    /**
     * Executes a resolution command.
     * If instruction is illegal, prints error message to out.
     * @param instruction: Sting - should be "up" to increase or "down" to decrease.
     */
    private void resolutionCommand(String instruction) {
        if (instruction.equals(UP_COMMAND)) {
            if (charsInRow * 2 > maxCharsInRow) {
                System.out.println(RES_BOUNDARIES_ERROR);
            } else {
                charsInRow *= 2;
                System.out.println(SET_WIDTH_MSG + charsInRow);
            }
        } else if (instruction.equals(DOWN_COMMAND)) {
            if (charsInRow / 2 < minCharsInRow) {
                System.out.println(RES_BOUNDARIES_ERROR);
            } else {
                charsInRow /= 2;
                System.out.println(SET_WIDTH_MSG + charsInRow);
            }
        } else {
            System.out.println(RES_BOUNDARIES_ERROR);
        }
        }

    /**
     * Executes chars command.
     * Prints to out all the current chars.
     */
    private void charsCommand() {
        boolean isFirst = true;
        for (int c = SPACE_ASCII_VAL; c <= LAST_CHAR_VAL; c++) {
            if (chars.contains((char) c)) {
                if (isFirst) {
                    System.out.print((char) c);
                    isFirst = false;
                } else { System.out.print(SPACE_STRING + (char) c); }
            }
        }
        System.out.println();
    }

    /**
     * Executes an add or remove command - adds or removes a char to the pool.
     * Prints error message if command is invalid.
     * @param instruction: String - The additional instruction to the command used for interpretation.
     * @param add: boolean - true: if add command. false: if remover command.
     * @param errorMessage: String - The matching error message to the command type.
     */
    private void addRemoveCommand(String instruction, boolean add, String errorMessage) {
        if (instruction.length() == 1) {
            addRemoveSingleChar(instruction.charAt(0), add, errorMessage, false);
            return;
        }
        if (instruction.equals(ALL_COMMAND)) {
            addRemoveMultiple(SPACE_ASCII_VAL, LAST_CHAR_VAL, add, errorMessage, true);
            return;
        }
        if (instruction.equals(SPACE_COMMAND)) {
            addRemoveSingleChar(SPACE_CHAR, add, errorMessage, true);
            return;
        }
        if (instruction.length() == MULTIPLE_CHARS_COMMAND_LEN && instruction.charAt(1) == SEPARATOR) {
            multipleCharsCommand(instruction, add, errorMessage);
            return;
        }
        System.out.println(errorMessage);

    }

    /**
     * Adds or removes a single char to or from the chars pool.
     * @param key: char - The char to add or remove.
     * @param add: boolean - true: if add command. false: if remover command.
     * @param errorMessage: String - The matching error message to the command type.
     */
    private void addRemoveSingleChar(char key, boolean add, String errorMessage, boolean space) {
        if (isCharValid(key) || ((key == SPACE_CHAR) && space)) {
            if (add) {
                brightnessMap.add(key, FONT);
                chars.add(key);
                return;
            }
            chars.remove(key);
        } else {
            System.out.println(errorMessage);
        }
    }

    /**
     * Executes a single add or remove command - adds or removes a char to the pool.
     * @param instruction: String - The additional instruction to the command used for interpretation.
     * @param add: boolean - true: if add command. false: if remover command.
     * @param errorMessage: String - The matching error message to the command type.
     */
    private void multipleCharsCommand(String instruction, boolean add, String errorMessage) {
        int firstAscii = instruction.charAt(0);
        int lastAscii = instruction.charAt(2);
        if (isCharValid(firstAscii) && isCharValid(lastAscii)) {
            if (firstAscii > lastAscii) {
                int temp = firstAscii;
                firstAscii = lastAscii;
                lastAscii = temp;
            }
            addRemoveMultiple(firstAscii, lastAscii, add, errorMessage, false);
        } else {
            System.out.println(errorMessage);
        }
    }

    /**
     * Checks if given ASCII value is legal.
     * @param asciiValue: int - ASCII value.
     * @return boolean: true: if valid. false: otherwise.
     */
    private static boolean isCharValid(int asciiValue) {
        return asciiValue >= FIRST_CHAR_VAL && asciiValue <= LAST_CHAR_VAL;
    }

    /**
     * Adds or removes multiple chars to or from the chars pool.
     * @param start: int - The ASCII value of the first char to add or remove.
     * @param end: int - The ASCII value of the last char to add or remove.
     * @param add: boolean - true: if add command. false: if remover command.
     * @param errorMessage: String - The matching error message to the command type.
     */
    private void addRemoveMultiple(int start, int end, boolean add, String errorMessage, boolean space) {
        for (int cur = start; cur <= end; cur++) {
            addRemoveSingleChar((char) cur, add, errorMessage, space);
        }
    }
}
