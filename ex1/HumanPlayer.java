import java.util.Scanner;

/**
 * A Human Player class - Manages the user's choices of play in the game.
 */
class HumanPlayer implements Player {

    public static final String USER_INPUT_REQUEST_MSG = "Player %s, type coordinates: %n";
    public static final String INVALID_INPUT_MSG = "Invalid coordinates, type again: ";

    /**
     * Places the player's mark on given coordinates.
     * Gets an input of coordinates from user to try and put a mark on to.
     * If the given coordinates are out of board's borders or are taken, asks for another input.
     * @param board: Board - The board which the player is playing with.
     * @param mark: Mark - The mark which represents the player on the board.
     */
    @Override
    public void playTurn (Board board, Mark mark) {
        int size = board.getSize();
        int row = size;
        int col = size;
        Scanner scanner = new Scanner(System.in);
        System.out.printf(USER_INPUT_REQUEST_MSG, mark);
        while (row >= size || col >= size) {

            // get coordinates and split into row and column integers
            char[] coordinates = scanner.nextLine().toCharArray();
            row = Character.getNumericValue(coordinates[0]);
            col = Character.getNumericValue(coordinates[1]);

            // check if coordinates are valid: in board's borders and are blank
            if (row >= size || col >= size) {
                System.out.println(INVALID_INPUT_MSG);
            } else if (!board.putMark(mark, row, col)) {
                row = col = size;
                System.out.println(INVALID_INPUT_MSG);
            }
        }
    }
}
